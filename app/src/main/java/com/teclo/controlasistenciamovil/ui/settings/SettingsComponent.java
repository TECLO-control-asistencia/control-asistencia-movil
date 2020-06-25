package com.teclo.controlasistenciamovil.ui.settings;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;

import dagger.Component;

/**
 * @author DanielGcUnitis
 */
@ActivityScope
@Component(
        dependencies = OAuthComponent.class,
        modules = SettingsModule.class
)
public interface SettingsComponent {
    void inject(SettingsFragment settingsFragment);
    SettingsContract.Presenter getPresenter();
}
