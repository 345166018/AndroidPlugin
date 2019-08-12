package com.hongx.taopiaopiao;

import android.util.Log;

public class OneService extends BaseService{
    private static final String TAG = "hongxue OneService";
    private int i = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    Log.i(TAG, "run: "+(i++));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
