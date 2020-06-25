package com.teclo.controlasistenciamovil.ui.justificacion;

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
public class CaJustificModule {

    CaJustificContract.View view;

    public CaJustificModule(CaJustificContract.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    public CaJustificContract.Presenter providePresenter(@Named(Constants.DATA_SOURCE_REPOSITORY) TemplateDataSource templateDataSource,
                                                    AppSharePreferences appSharePreferences) {
        return new CaJustificPresenterImpl(view, templateDataSource, appSharePreferences);
    }

}
