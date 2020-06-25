package com.teclo.controlasistenciamovil.dagger.modules;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teclo.controlasistenciamovil.utils.AppRealmManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by omhack on 4/6/18.
 */

@Module
public class SicnApplicationModule {

    private Application application;

    public SicnApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides @Singleton
    AppSharePreferences provideSharePreferences() {
        return new AppSharePreferences(application);
    }


    @Provides @Singleton
    AppRealmManager providerAppRealmManager(){
        return new AppRealmManager(application);
    }


    @Provides @Singleton
    Gson provideGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}
