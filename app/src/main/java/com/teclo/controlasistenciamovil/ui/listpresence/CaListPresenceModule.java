package com.teclo.controlasistenciamovil.ui.listpresence;

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
public class CaListPresenceModule {

    CaListPresenceContract.View view;

    public CaListPresenceModule(CaListPresenceContract.View view){
        this.view=view;
    }

    @Provides
    @ActivityScope
    public CaListPresenceContract.Presenter providePresenter(@Named(Constants.DATA_SOURCE_REPOSITORY) TemplateDataSource templateDataSource,
                                                             AppSharePreferences appSharePreferences) {
        return new CaListPresencePresenterImpl(view, templateDataSource, appSharePreferences);
    }

}
