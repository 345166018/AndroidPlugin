package com.hongx.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author: fuchenming
 * @create: 2019-11-01 10:23
 */
public class LoginActivity extends Activity {

    EditText name;
    EditText password;
    private String className;
    SharedPreferences share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        share = this.getSharedPreferences("david", MODE_PRIVATE);//实例化
        className = getIntent().getStringExtra("extraIntent");
        if (className != null) {
            ((TextView) findViewById(R.id.text)).setText(" 跳转界面：" + className);
        }
    }

    public void login(View view) {
        if ((name.getText() == null || password.getText() == null)) {
            Toast.makeText(this, "请填写用户名 或密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("fhx".equals(name.getText().toString()) && "123456".equals(password.getText()
                .toString())) {
            SharedPreferences share = super.getSharedPreferences("hongx", MODE_PRIVATE);//实例化
            SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
            editor.putString("name", name.getText().toString());
            editor.putString("sex", password.getText().toString());
            editor.putBoolean("login", true);   //设置保存的数据
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            editor.commit();    //提交数据保存
            if (className != null) {
                ComponentName componentName = new ComponentName(this, className);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
                finish();
            }
        } else {
            SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
            editor.putBoolean("login", false);   //设置保存的数据
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
            editor.commit();    //提交数据保存
        }
    }
}
