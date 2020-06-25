package com.teclo.controlasistenciamovil.ui.register;

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
public class CaPresenceRegisterModule {

    CaPresenceRegisterContract.View view;

    public CaPresenceRegisterModule(CaPresenceRegisterContract.View view){
        this.view=view;
    }

    @Provides
    @ActivityScope
    public CaPresenceRegisterContract.Presenter providePresenter(@Named(Constants.DATA_SOURCE_REPOSITORY) TemplateDataSource templateDataSource,
                                                                 AppSharePreferences appSharePreferences) {
        return new CaPresenceRegisterPresenterImpl(view, templateDataSource, appSharePreferences);
    }

}
