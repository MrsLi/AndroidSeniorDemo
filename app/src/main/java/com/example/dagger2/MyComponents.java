package com.example.dagger2;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = CatModule.class)
public interface MyComponents {
    void injectCat(Dagger2MainActivity dagger2MainActivity);
}
