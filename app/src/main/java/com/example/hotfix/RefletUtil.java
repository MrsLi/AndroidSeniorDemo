package com.example.hotfix;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;

/**
 * 处理反射工具类
 */
public class RefletUtil {

    static {
        if (SDK_INT> Build.VERSION_CODES.P) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Object[]{new String[]{"L"}});
                Object sVmRuntime = getRuntime.invoke(null);
                setHiddenApiExemptions.invoke(sVmRuntime,new Object[]{new String[]{"L"}});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static Field findField(Object object,String name){
        Class<?> clz = object.getClass();
            while (clz != Object.class) {
                Field field = null;
                try {
                    field = clz.getDeclaredField(name);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if (field != null) {
                    //跳过权限检查
                    field.setAccessible(true);
                    Log.i("RefletUtil", "findField: ");
                    return  field;
                }
                clz = clz.getSuperclass();
                Log.i("RefletUtil", "findField: clz"+clz.toString());
            }
        Log.i("RefletUtil", "findField: null");
        return null;
    }

    public static Method findMethod(Object object, String name,Class<?>... parameterTypes){
        Class<?> clz = object.getClass();

            while (clz != Object.class) {
                Method method = null;
                try {
                    method = clz.getDeclaredMethod(name,parameterTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (method != null) {
                    method.setAccessible(true);
                    return  method;
                }
                clz = clz.getSuperclass();
            }
        return null;
    }
}
