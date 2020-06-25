package com.teclo.controlasistenciamovil.dagger.components;

import android.content.Context;

import com.google.gson.Gson;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.modules.PublicMSModule;
import com.teclo.controlasistenciamovil.dagger.scopes.PublicMSScope;
import com.teclo.controlasistenciamovil.utils.AppRealmManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Named;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by omhack on 4/7/18.
 */
@Component(modules = {PublicMSModule.class}, dependencies= ApplicationComponent.class)
@PublicMSScope
public interface PublicMSComponent {

    @Named(Constants.PUBLIC_MS_SERVICE)
    Retrofit getPublicMSService();

    Context getContext();

    Gson getGson();

    AppSharePreferences getSharedPreference();

    AppRealmManager getAppRealmManager();

}
