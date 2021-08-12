package com.example.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过Hook AMS, 实现集中登录
 */
public class HookAms {
    Context context;
    public void hookAms(Context context){
        this.context = context;
        try {
            Class<?> aClass = Class.forName("android.app.ActivityManager");
            Field iActivityManagerSingleton = aClass.getDeclaredField("IActivityManagerSingleton");
            iActivityManagerSingleton.setAccessible(true);
            Object iActivityManagerSingletonValue = iActivityManagerSingleton.get(null);

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstance = singletonClass.getDeclaredField("mInstance");
            mInstance.setAccessible(true);

            Object iActivityManagerObject = mInstance.get(iActivityManagerSingletonValue);
            iActivityManagerObject.hashCode();



            //设置AMS动态代理
            Class<?> iActivityManagerIntercept = Class.forName("android.app.IActivityManager");
            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerIntercept}, new AMSInvocationHandler(iActivityManagerObject));
            mInstance.set(iActivityManagerSingletonValue,proxyInstance); //设置代理的值


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class AMSInvocationHandler implements InvocationHandler {
        Object iActivityManagerObject;
        public AMSInvocationHandler(Object iActivityManagerObject){
            this.iActivityManagerObject = iActivityManagerObject;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d("AMS", "invoke:----"+method.getName());
            if ("startActivity".equals(method.getName())) {
                Intent intent=null;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        intent = (Intent) args[i];
                    }
                }
                SharedPreferences loginInfo = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
                if (loginInfo!=null&&!loginInfo.getBoolean("login",false)) {
                    //获取intent,修改intent
                    intent.putExtra("exertIntent",intent.getComponent().getClassName());
                    ComponentName componentName = new ComponentName(context,LoginActivity.class);
                    intent.setComponent(componentName);
                }
            }
            return method.invoke(iActivityManagerObject,args);
        }
    }
}
