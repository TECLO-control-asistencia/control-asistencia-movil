package com.teclo.controlasistenciamovil.dagger.components;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.teclo.controlasistenciamovil.application.OAuthSessionManager;
import com.teclo.controlasistenciamovil.application.PublicMSSessionManager;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.modules.ConfigServiceModule;
import com.teclo.controlasistenciamovil.dagger.modules.SicnApplicationModule;
import com.teclo.controlasistenciamovil.ui.splash.SplashComponent;
import com.teclo.controlasistenciamovil.ui.splash.SplashModule;
import com.teclo.controlasistenciamovil.utils.AppRealmManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by omhack on 4/6/18.
 */

@Singleton
@Component(modules = {SicnApplicationModule.class, ConfigServiceModule.class})
public interface ApplicationComponent {

    SplashComponent getSplashComponet(SplashModule module);

    Context getContext();

    Application getApplication();

    Gson getGson();

    OAuthSessionManager getOauthSessionManager();

    PublicMSSessionManager getPublicMSSessionManager();

    AppSharePreferences getAppSharePreference();

    AppRealmManager getAppRealmManager();

    @Named(Constants.PUBLIC_SERVICE)
    Retrofit getConfigService();

}
