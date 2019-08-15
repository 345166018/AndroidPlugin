package com.hongx.taopiaopiao;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TaoMainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.registerBroad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //d动态注册广播
                IntentFilter intentFilter  = new IntentFilter();
                intentFilter.addAction("com.hongx.taopiaopiao.TaoMainActivity");
                registerReceiver(new MyReceiver(), intentFilter);
            }
        });

        findViewById(R.id.sendBroad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.hongx.taopiaopiao.TaoMainActivity");
                sendBroadcast(intent);
            }
        });
    }
}
