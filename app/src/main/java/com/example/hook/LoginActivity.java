package com.example.hook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.mrslibutterknife.R;

public class LoginActivity extends AppCompatActivity {

    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        className = getIntent().getStringExtra("exertIntent");
    }

    public void login(View view) {
        SharedPreferences loginInfo = getSharedPreferences("login_info", MODE_PRIVATE);
        loginInfo.edit().putBoolean("login",true).apply();
        Intent intent = new Intent();
        ComponentName component = new ComponentName(this,className);
        intent.setComponent(component);
        startActivity(intent);
        finish();
    }
}