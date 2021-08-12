package com.example.pluginapp;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设置沉浸式状态栏
 */
public class PluginTwoActivity extends FragmentActivity {

    private ConstraintLayout rootVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_plugin_two);
        rootVIew = findViewById(R.id.rootView);
        setStatusBarColor();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("=LPF=PluginTwoActivity", "onResume: ");
        Window window = getWindow();
        View decorView = window.getDecorView();
        Log.d("=LPF=PluginTwoActivity", "windows: "+ window.hashCode());
        Log.d("=LPF=PluginTwoActivity", "decorView: "+ decorView.hashCode());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("=LPF=PluginTwoActivity", "onPause: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("=LPF=PluginTwoActivity", "onRestart: ");
    }
    @Override
    public void onDetachedFromWindow() {
        Log.i("=LPF=PluginTwoActivity", "onDetachedFromWindow: ");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDestroy() {
        Log.d("=LPF=PluginTwoActivity", "onDestroy: ");
        super.onDestroy();

    }

    void setStatusBarColor(){
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            //创建新的view
            View view = new View(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight());
            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.design_default_color_error));
            decorView.addView(view);

            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            for (int i = 0; i < contentView.getChildCount(); i++) {
                View childAt = contentView.getChildAt(i);
                if (childAt instanceof ViewGroup) {//说明是根布局
                    childAt.setFitsSystemWindows(true);
                    ((ViewGroup) childAt).setClipToPadding(true);
                }
            }

        }
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }
}