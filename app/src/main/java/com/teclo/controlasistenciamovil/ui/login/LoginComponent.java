package com.teclo.controlasistenciamovil.ui.login;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;

import dagger.Component;

/**
 * Created by omhack on 4/4/18.
 */
@ActivityScope
@Component(
        dependencies = OAuthComponent.class,
        modules = LoginModule.class
)
public interface LoginComponent {
    void inject(LoginActivity menuActivity);
    LoginContract.Presenter getPresenter();
}
