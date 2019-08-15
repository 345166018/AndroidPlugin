package com.hongx.pluginstand;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public interface PayInterfaceActivity {
    void attach(Activity proxyActivity);

    /**
     * 生命周期
     */
    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    boolean onTouchEvent(MotionEvent event);

    void onBackPressed();

}
