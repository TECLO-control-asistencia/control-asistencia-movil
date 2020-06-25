package com.teclo.controlasistenciamovil.ui.listpresence;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;

import dagger.Component;

/**
 * @author DanielGcUnitis
 */
@ActivityScope
@Component(
        dependencies = OAuthComponent.class,
        modules = CaListPresenceModule.class
)
public interface CaListPresenceComponent {
    void inject(CaListPresenceFragment caListPresenceFragment);
    CaListPresenceContract.Presenter getPresenter();
}
