package com.teclo.controlasistenciamovil.ui.menuinferior;

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
public class MenuInferiorModule {
    MenuInferiorContract.View view;

    public MenuInferiorModule(MenuInferiorContract.View view){
        this.view=view;
    }

    @Provides
    @ActivityScope
    public MenuInferiorContract.Presenter providePresenter(@Named(Constants.DATA_SOURCE_REPOSITORY) TemplateDataSource templateDataSource,
                                                   AppSharePreferences appSharePreferences) {
        return new MenuInferiorPresenterImpl(view, templateDataSource, appSharePreferences);
    }

}
