package com.hongx.hook;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author: fuchenming
 * @create: 2019-10-31 16:41
 */
public class ProxyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

    }
}
