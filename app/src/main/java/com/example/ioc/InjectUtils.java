package com.example.ioc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ioc.annotations.ContentView;
import com.example.ioc.annotations.EventTag;
import com.example.ioc.annotations.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {


    static void inject(Object context){
            injectLayout(context);
            injectView(context);
            injectClick(context);
    }

    private static void injectClick1(Object context) {
        Class<?> aClass = context.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> clazz = annotation.annotationType();
                EventTag eventTag = clazz.getAnnotation(EventTag.class);
                if (eventTag != null) {
                    //1.开始获取EventTag注解中的参数;
                    String listenerSetter = eventTag.listenerSetter();
                    Class<?> listenerType = eventTag.listenerType();
                    String callBackMethod = eventTag.callBackMethod();

                    //2.根据参数,动态代理设置
                    try {
                        Method valueMethod = clazz.getDeclaredMethod("value");
                        int[] viewId = (int[]) valueMethod.invoke(annotation);
                        for (int id : viewId) {
                            Method findViewById = aClass.getMethod("findViewById",int.class);
                            View view = (View) findViewById.invoke(context, id);
                            Class<? extends View> viewClass = view.getClass();
                            Method setListener = viewClass.getMethod(listenerSetter, listenerType);
                            Object newProxyInstance = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, new ListenerInvokeHandler(context, method));
                            setListener.invoke(view,newProxyInstance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private static  void injectClick(Object context){
        Class<?> aClass = context.getClass();
        for (Method method : aClass.getDeclaredMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                EventTag eventTag = annotationType.getAnnotation(EventTag.class);
                if (eventTag != null) {
                    String listenerSetter = eventTag.listenerSetter();
                    Class<?> listenerType = eventTag.listenerType();
                    String callBackMethod = eventTag.callBackMethod();

                    try {
                        Method valueMethod = annotationType.getMethod("value");
                        int[] valueId = (int[]) valueMethod.invoke(annotation);
                        for (int id : valueId) {
                            Method findViewById = aClass.getMethod("findViewById",int.class);
                            View view = (View) findViewById.invoke(context, id);
                            Class<? extends View> viewClass = view.getClass();
                            Method setListenerMethod = viewClass.getMethod(listenerSetter, listenerType);
                            Object proxyInstance = Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{listenerType}, new ListenerInvokeHandler(context, method));
                            setListenerMethod.invoke(view,proxyInstance);
                        }

                    } catch (Exception e) {
                        Log.d("InjectUtils", "injectClick: e:"+e.getMessage().toLowerCase());
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    private static void injectView(Object context) {
        Class<?> aClass = context.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            InjectView injectViewAnnotation = field.getAnnotation(InjectView.class);
            if (injectViewAnnotation == null) {
                continue;
            }
            int viewId = injectViewAnnotation.value();
            try {
                Method findViewByIdMethod = aClass.getMethod("findViewById", int.class);
                View view = (View) findViewByIdMethod.invoke(context,viewId);

                //给变量设置值;
                field.setAccessible(true);
                field.set(context,view);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private static void injectLayout(Object context){
        int layoutId = 0;
        Class<?> aClass = context.getClass();
        ContentView contentView = aClass.getAnnotation(ContentView.class);
        if (contentView == null) {
            return;
        }
        layoutId = contentView.value();
        Method setContentView = null;
        try {
            setContentView = context.getClass().getMethod("setContentView", int.class);
            setContentView.invoke(context, layoutId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
