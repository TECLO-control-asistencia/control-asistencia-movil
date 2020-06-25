package com.teclo.controlasistenciamovil.application;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;

import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.dagger.components.ApplicationComponent;
import com.teclo.controlasistenciamovil.dagger.components.DaggerApplicationComponent;
import com.teclo.controlasistenciamovil.dagger.modules.ConfigServiceModule;
import com.teclo.controlasistenciamovil.dagger.modules.SicnApplicationModule;
import com.teclo.controlasistenciamovil.utils.SicnAppLifecycleObserver;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by omhack on 4/4/18.
 */

public class SicnApp extends Application {

    private static ApplicationComponent component;
    public static boolean IN_APP;

    @Override
    public void onCreate() {
        super.onCreate();
        configDagger();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new SicnAppLifecycleObserver(component.getAppSharePreference(),component.getAppRealmManager()));
    }

    private void configDagger() {
        component = DaggerApplicationComponent.builder()
                .sicnApplicationModule(new SicnApplicationModule(this))
                .configServiceModule(new ConfigServiceModule(getString(R.string.network_config_base_url)))
                .build();
    }

    public static ApplicationComponent getAppComponent() {
        return component;
    }

}
