package com.teclo.controlasistenciamovil.ui.register;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;

import dagger.Component;

/**
 * @author DanielGcUnitis
 */
@ActivityScope
@Component(
        dependencies = OAuthComponent.class,
        modules = CaPresenceRegisterModule.class
)
public interface CaPresenceRegisterComponent {
    void inject(CaPresenceRegisterFragment caPresenceRegisterFragment);
    CaPresenceRegisterContract.Presenter getPresenter();
}
