package com.example.mainapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import dalvik.system.DexClassLoader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private Button loadPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPlugin = (Button) findViewById(R.id.load_plugin);
        loadPlugin.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ //表示未授权时
                //进行授权
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else{
                loadPluginMethod("我是插件中的方法");
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //同意权限申请
                    loadPluginMethod("我是插件中的方法");
                }else { //拒绝权限申请
                    Toast.makeText(this,"权限被拒绝了",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void loadPluginMethod(String str) {

        File f = new File(getCacheDir() + "/pluginapp-debug.apk");
        if (!f.exists()) {
            try {
                InputStream is = getAssets().open("apk/pluginapp-debug.apk");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        String dexPath = f.getPath();
        DexClassLoader classLoader = new DexClassLoader(dexPath,getCodeCacheDir().getPath(),null,this.getClassLoader());
        try {

            Class oldClass =
                    classLoader.loadClass("com.example.pluginapp.PluginUtil");
            Constructor utilConstructor = oldClass.getDeclaredConstructors()[0];
            utilConstructor.setAccessible(true);
            Object util = utilConstructor.newInstance();
            Method shoutMethod = oldClass.getDeclaredMethod("log",String.class);
            shoutMethod.setAccessible(true);
            shoutMethod.invoke(util,str);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}