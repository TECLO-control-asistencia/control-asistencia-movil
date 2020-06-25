package com.teclo.controlasistenciamovil.ui.splash;

import com.teclo.controlasistenciamovil.application.ActivityScope;
import com.teclo.controlasistenciamovil.application.PublicMSSessionManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by omhack on 1/24/18.
 */
@Module
public class SplashModule {

    SplashContract.View view;
    public SplashModule(SplashContract.View view) {
        this.view = view;
    }

    @Provides @ActivityScope
    public SplashContract.Presenter providePresenter(PublicMSSessionManager sessionManager) {
        return new SplashPresenterImpl(view, sessionManager);
    }


   /* @Provides @ActivityScope
    ConfigDataRepository providesConfigRepository(IConfigAPI iConfigAPI, AppSharePreferences preferences){
        return new ConfigDataRepository(new ConfigRemoteDataSource(iConfigAPI), new ConfigLocalDataSource(preferences));
    }

    @Provides @ActivityScope
    IConfigAPI provideConfigService(@Named(Constants.PUBLIC_SERVICE) Retrofit retrofit){
        return retrofit.create(IConfigAPI.class);
    }*/

}
