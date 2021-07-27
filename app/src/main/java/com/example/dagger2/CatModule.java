package com.example.dagger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class CatModule {

    @Singleton
    @Provides
    public Cat providesCat(){
        return new Cat();
    }
}
