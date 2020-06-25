package com.teclo.controlasistenciamovil.dagger.modules;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.scopes.SessionScope;
import com.teclo.controlasistenciamovil.data.local.TemplateLocalDataSource;
import com.teclo.controlasistenciamovil.data.remote.TemplateRemoteDataSource;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.data.repository.TemplateRepository;
import com.teclo.controlasistenciamovil.utils.AppRealmManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by omhack on 4/7/18.
 */
@Module
    public class AuthenticatedRepositoryModule {
    @Provides
    @SessionScope
    @Named(Constants.DATA_SOURCE_REPOSITORY)
    TemplateDataSource provideTemplateRepository(@Named(Constants.DATA_SOURCE_LOCAL) TemplateDataSource localDataSource,
                                                @Named(Constants.DATA_SOURCE_REMOTE) TemplateDataSource remoteDataSource){
        return new TemplateRepository(localDataSource,remoteDataSource);
    }

    @Provides @SessionScope @Named(Constants.DATA_SOURCE_LOCAL)
    TemplateDataSource provideTemplateLocalSource(AppSharePreferences sharePreferences, AppRealmManager appRealmManager){
        return new TemplateLocalDataSource(sharePreferences,appRealmManager);
    }

    @Provides @SessionScope @Named(Constants.DATA_SOURCE_REMOTE)
    TemplateDataSource provideTemplateRemoteSource(@Named(Constants.OAUTH_MS_SERVICE) Retrofit retrofit){
        return new TemplateRemoteDataSource(retrofit);
    }
}
