package com.example.ioc.annotations;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventTag(listenerSetter = "setOnLongClickListener",listenerType = View.OnLongClickListener.class,callBackMethod = "onLongClick")
public @interface InjectOnLongClick {
    int[] value();
}
