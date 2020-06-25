package com.teclo.controlasistenciamovil.ui.menuinferior;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;

import dagger.Component;

/**
 * @author DanielUitis
 */

@ActivityScope
@Component(
        dependencies = OAuthComponent.class,
        modules = MenuInferiorModule.class
)
public interface MenuInferiorComponent {
    void inject(MenuInferiorActivity menuInferiorActivity);
    MenuInferiorContract.Presenter getPresenter();
}
