package com.example.hotfix;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HotFixUtil {

    /**
     *
     * @param application
     * @param pathFile   path.dex补丁文件的File对象
     */
    public static void loadDex(Application application, File pathFile){
        //1.通过反射,获取到PathClassLoader类
        ClassLoader classLoader = application.getClassLoader(); ///改classLoader就是PathClassLoader

        Log.i("HotFixUtil", "loadDex: classLoader:"+classLoader.getClass().toString());
        Field pathList = RefletUtil.findField(classLoader, "pathList");
        try {
            Object dexPathList = pathList.get(classLoader);
            Field dexElementField = RefletUtil.findField(dexPathList, "dexElements");
            Object[] dexElements = ((Object[]) dexElementField.get(dexPathList));   //已有的dexElements

            //3.合并补丁包path.dex文件到dexElement
//            ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
//            this.dexElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory,
//                    suppressedExceptions, definingContext, isTrusted);
            ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
            ArrayList<File> files = new ArrayList<>();
            files.add(pathFile);
            File optimizedDirectory = application.getCodeCacheDir();

            Method makeDexElementsMethod = RefletUtil.findMethod(dexPathList, "makeDexElements", List.class, File.class, List.class, ClassLoader.class, boolean.class);
            //新的Elements;
            Object[] pathElements = (Object[]) makeDexElementsMethod.invoke(null, files, optimizedDirectory, suppressedExceptions, classLoader, false);


            //4.合并两个Elements数组;
            Object[] newElements = (Object[]) Array.newInstance(pathElements.getClass().getComponentType(), pathElements.length + dexElements.length);

            System.arraycopy(pathElements,0,newElements,0,pathElements.length);
            System.arraycopy(dexElements,0,newElements,0,pathElements.length+dexElements.length);

            dexElementField.set(dexPathList,newElements);


        } catch (Exception e) {
            Log.e("HotFixUtil", "loadDex: exception"+e.getMessage().toString());
            e.printStackTrace();
        }
    }
}
