package com.hongx.hook;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: fuchenming
 * @create: 2019-10-30 20:25
 */
public class HookUtil {

    private Context context;


    public void hookMh(Context context) {
        try {
            Class<?> mActivityThreadCls = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = mActivityThreadCls.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object mActivityThreadObj = sCurrentActivityThreadField.get(null);
            Field mHField = mActivityThreadCls.getDeclaredField("mH");
            mHField.setAccessible(true);

            Handler mH = (Handler) mHField.get(mActivityThreadObj);
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new ActivityMH(mH));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ActivityMH implements Handler.Callback {
        private Handler mH;

        public ActivityMH(Handler mH) {
            this.mH = mH;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100) {//LAUNCH_ACTIVITY ==100
                //加工完一定丢给系统
                handleLuachActivity(msg);
            }
            //做了真正的跳转
            mH.handleMessage(msg);
            return true;
        }
    }

    private void handleLuachActivity(Message msg) {
        Object obj = msg.obj;
        try {
            Field intentField = obj.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent realIntent = (Intent) intentField.get(obj);
            Intent oldIntent = realIntent.getParcelableExtra("oldIntent");
            if (oldIntent != null) {
                // 集中式登录
                SharedPreferences share = context.getSharedPreferences("hongx", Context.MODE_PRIVATE);
                if (share.getBoolean("login", false) ||
                        oldIntent.getComponent().getClassName().equals(SecondActivity.class.getName())) {
                    // 还原原有的意图
                    realIntent.setComponent(oldIntent.getComponent());
                } else {
                    ComponentName componentName = new ComponentName(context, LoginActivity.class);
                    realIntent.putExtra("extraIntent", oldIntent.getComponent().getClassName());
                    realIntent.setComponent(componentName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hookStartActivity(Context context) {
        this.context = context;
        Object defaltValue;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Oreo  8.0	26
                Field IActivityManagerSingleton = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
                IActivityManagerSingleton.setAccessible(true);
                defaltValue = IActivityManagerSingleton.get(null);
            } else {
                Class<?> ActivityManagerNativecls = Class.forName("android.app.ActivityManagerNative");
                Field gDefault = ActivityManagerNativecls.getDeclaredField("gDefault");
                gDefault.setAccessible(true);
                //因为是静态变量  所以获取的到的是系统值
                defaltValue = gDefault.get(null);

            }
            //mInstance对象
            Class<?> SingletonClass = Class.forName("android.util.Singleton");

            Field mInstance = SingletonClass.getDeclaredField("mInstance");
            //还原 IactivityManager对象  系统对象
            mInstance.setAccessible(true);

            //真的IActivityManager对象
            Object iActivityManagerObject = mInstance.get(defaltValue);

            //通过代理实现一个IActivityManager代理对象
            Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");

            MyInvocationHandler startActivtyMethod = new MyInvocationHandler(iActivityManagerObject);

            //(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
            Object oldIActivityManager = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{IActivityManagerIntercept,View.OnClickListener.class},
                    startActivtyMethod
            );

            //将系统的iActivityManager 替换成 自己通过动态代理实现的对象
            //oldIactivityManager对象 实现了 IActivityManager这个接口的所有方法
            mInstance.set(defaltValue, oldIActivityManager);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    class MyInvocationHandler implements InvocationHandler {

        private Object iActivityManagerObject;

        public MyInvocationHandler(Object iActivityManagerObject) {
            this.iActivityManagerObject = iActivityManagerObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.i("hongxue", "invoke = " + method.getName());
            if ("startActivity".equals(method.getName())) {
                Log.i("hongxue", "-----------android 8 以下版本 startActivity hook 成功--------------");

                //                寻找传进来的intent
                Intent intent = null;
                int index = 0;
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Intent) {
                        intent = (Intent) args[i];
                        index = i;
                    }
                }

                Intent newIntent = new Intent();
                ComponentName componentName = new ComponentName(context, ProxyActivity.class);
                newIntent.setComponent(componentName);
                //真实的意图被我隐藏到了键值对中
                newIntent.putExtra("oldIntent", intent);
                args[index] = newIntent;

            }

            return method.invoke(iActivityManagerObject, args);
        }
    }

}
