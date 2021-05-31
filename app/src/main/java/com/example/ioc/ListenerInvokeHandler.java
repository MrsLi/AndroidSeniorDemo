package com.example.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerInvokeHandler implements InvocationHandler {
    private Object context;
    private Method activityMethod;

    public ListenerInvokeHandler(Object context, Method activityMethod) {
        this.context = context;
        this.activityMethod = activityMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return activityMethod.invoke(context);
    }
}
