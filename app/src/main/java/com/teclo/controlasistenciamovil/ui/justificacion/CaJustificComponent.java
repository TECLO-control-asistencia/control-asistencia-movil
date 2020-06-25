package com.teclo.controlasistenciamovil.ui.justificacion;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;

import dagger.Component;

/**
 * Created by DanielGC on 9/5/19.
 */
@ActivityScope
@Component(
        dependencies = OAuthComponent.class,
        modules = CaJustificModule.class
)
public interface CaJustificComponent {
    void inject(CaJustificActivity caJustificActivity);
    CaJustificContract.Presenter getPresenter();
}
