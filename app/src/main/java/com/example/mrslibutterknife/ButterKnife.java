package com.example.mrslibutterknife;

import java.lang.reflect.Constructor;

public class ButterKnife {

    public static void bind(Object activity){
        String name = activity.getClass().getName();
        String binderName = name + "$$ViewBinder";
        try {
            Class<?> aClass = Class.forName(binderName);
            Constructor<?> constructor = aClass.getConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
