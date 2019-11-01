package com.hongx.hook;

import android.app.Application;

/**
 * @author: fuchenming
 * @create: 2019-10-30 20:25
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HookUtil hookUtil = new HookUtil();
        hookUtil.hookStartActivity(this);
        hookUtil.hookMh(this);
    }
}
