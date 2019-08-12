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

        findViewById(R.id.startService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(that,"插件",Toast.LENGTH_SHORT).show();
                startService(new Intent(that, OneService.class));
            }
        });

        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(that,"插件",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(that, SceondActivity.class));
                startService(new Intent(that, OneService.class));

                //d动态注册广播
//                IntentFilter intentFilter  = new IntentFilter();
//                intentFilter.addAction("com.hongx.taopiaopiao.TaoMainActivity");
//                registerReceiver(new MyReceiver(), intentFilter);
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
