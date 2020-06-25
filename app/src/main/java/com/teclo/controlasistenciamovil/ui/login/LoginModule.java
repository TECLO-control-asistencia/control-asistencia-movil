package com.teclo.controlasistenciamovil.ui.login;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DanielGC on 6/5/19.
 */
@Module
public class LoginModule {
    LoginContract.View view;

    public LoginModule(LoginContract.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    public LoginContract.Presenter providePresenter(@Named(Constants.DATA_SOURCE_REPOSITORY) TemplateDataSource templateDataSource,
                                                   AppSharePreferences appSharePreferences) {
        return new LoginPresenterImpl(view, templateDataSource, appSharePreferences);
    }
}
