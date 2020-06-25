package com.teclo.controlasistenciamovil.ui.splash;


import com.teclo.controlasistenciamovil.application.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by Omar Tovías on 1/25/18.
 */
@ActivityScope
@Subcomponent(modules = SplashModule.class)
public interface SplashComponent {

    void inject(SplashActivity splashActivity);

    SplashContract.Presenter getPresenter();

}
