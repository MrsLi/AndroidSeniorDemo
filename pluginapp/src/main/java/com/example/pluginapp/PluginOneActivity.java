package com.example.pluginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PluginOneActivity extends AppCompatActivity {

    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_plugin_one);
        View jumpBtn = findViewById(R.id.jump);
        layout = findViewById(R.id.layout);
        jumpBtn.setOnClickListener(view->{startActivity(new Intent(this,PluginTwoActivity.class));});
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("=LPF=PluginOneActivity", "onResume: ");
        Window window = getWindow();
        View decorView = window.getDecorView();
        Log.d("=LPF=PluginOneActivity", "windows: "+ window.hashCode());
        Log.d("=LPF=PluginOneActivity", "decorView: "+ decorView.hashCode());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("=LPF=PluginOneActivity", "onPause: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("=LPF=PluginOneActivity", "onRestart: ");
    }
}