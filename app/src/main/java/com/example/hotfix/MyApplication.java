package com.example.hotfix;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;

import androidx.annotation.RequiresApi;

public class MyApplication extends Application {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //获取补丁dex文件, 引用热修复工具;

//        Log.i("MyApplication", "attachBaseContext: dataPath:"+getFilesDir().getAbsolutePath() );
//        File file = new File("/sdcrad/path.dex");
//        HotFixUtil.loadDex(this,file);

    }
}
