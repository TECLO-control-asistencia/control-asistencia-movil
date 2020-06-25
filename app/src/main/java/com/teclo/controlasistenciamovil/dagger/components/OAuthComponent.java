package com.teclo.controlasistenciamovil.dagger.components;

import android.content.Context;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.modules.AuthenticatedRepositoryModule;
import com.teclo.controlasistenciamovil.dagger.modules.OAuthModule;
import com.teclo.controlasistenciamovil.dagger.scopes.SessionScope;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppRealmManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Named;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by omhack on 4/7/18.
 */

@Component(modules = {OAuthModule.class, AuthenticatedRepositoryModule.class},
        dependencies= ApplicationComponent.class)
@SessionScope
public interface OAuthComponent {

    @Named(Constants.OAUTH_MS_SERVICE)
    Retrofit getOauthMSService();

    AppSharePreferences getAppSharePreferences();

    AppRealmManager getAppRealmManager();

    Context getContext();

    @Named(Constants.DATA_SOURCE_REPOSITORY)
    TemplateDataSource getTemplateRepository();
}