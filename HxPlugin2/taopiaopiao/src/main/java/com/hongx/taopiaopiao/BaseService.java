package com.hongx.taopiaopiao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hongx.pluginstand.PayInterfaceService;


public class BaseService extends Service implements PayInterfaceService {
    private Service that;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attach(Service proxyService) {
        this.that = proxyService;
    }

    @Override
    public void onCreate() {

    }
}
