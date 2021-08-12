package com.example.hook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.mrslibutterknife.R;
/**
 * 通过Hook AMS, 实现集中登录
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        HookAms hookAms = new HookAms();
        hookAms.hookAms(this);
    }

    public void jump2(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void outLogin(View view) {
        SharedPreferences loginInfo = getSharedPreferences("login_info", MODE_PRIVATE);
        loginInfo.edit().clear().apply();
    }
}