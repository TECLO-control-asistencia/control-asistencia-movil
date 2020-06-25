package com.teclo.controlasistenciamovil.ui.settings;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author DanielUitis
 */

@Module
public class SettingsModule {

    SettingsContract.View view;

    public SettingsModule(SettingsContract.View view){
        this.view=view;
    }

    @Provides
    @ActivityScope
    public SettingsContract.Presenter providePresenter(@Named(Constants.DATA_SOURCE_REPOSITORY) TemplateDataSource templateDataSource,
                                                       AppSharePreferences appSharePreferences) {
        return new SettingsPresenterImpl(view, templateDataSource, appSharePreferences);
    }

}
