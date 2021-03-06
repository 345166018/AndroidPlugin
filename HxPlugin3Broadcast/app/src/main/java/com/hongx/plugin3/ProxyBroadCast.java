package com.hongx.plugin3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.hongx.pluginstand.PayInterfaceBroadcast;

import java.lang.reflect.Constructor;

public class ProxyBroadCast extends BroadcastReceiver {
    //需要加载插件的全类名
    private String className;
    private PayInterfaceBroadcast payInterfaceBroadcast;

    public ProxyBroadCast(String className, Context context) {
        this.className = className;//com.hongx.taopiaopiao.MyReceiver

        try {
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor constructor = aClass.getConstructor(new Class[]{});
            Object in = constructor.newInstance(new Object[]{});
            payInterfaceBroadcast = (PayInterfaceBroadcast) in;//payInterfaceBroadcast: com.hongx.taopiaopiao.MyReceiver
            payInterfaceBroadcast.attach(context);//context： com.hongx.plugin3.ProxyActivity//-className:com.hongx.taopiaopiao.TaoMainActivity
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        payInterfaceBroadcast.onReceive(context, intent);
    }
}
