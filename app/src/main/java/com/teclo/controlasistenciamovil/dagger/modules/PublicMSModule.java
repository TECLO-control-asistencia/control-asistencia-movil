package com.teclo.controlasistenciamovil.dagger.modules;

import android.app.Application;

import com.google.gson.Gson;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.scopes.PublicMSScope;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.ClientProfile;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by omhack on 4/7/18.
 */
@Module
public class PublicMSModule {
    public PublicMSModule(){
    }

    @Provides
    @Named(Constants.PUBLIC_MS_SERVICE)
    @PublicMSScope
    OkHttpClient providePublicMSOkHttpClient(Application app){
        ClientProfile.Builder clientProfileBuilder = new ClientProfile.Builder();
        return clientProfileBuilder.addMetaHeader(app).build().getOkHttpClient();
    }

    @Provides
    @Named(Constants.PUBLIC_MS_SERVICE)
    @PublicMSScope
    Retrofit providePublicMSRetrofit(Gson gson, @Named(Constants.PUBLIC_MS_SERVICE) OkHttpClient okHttpClient, @Named("ms_base_url") String baseUrl){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @PublicMSScope
    @Named("ms_base_url")
    String provideMSBaseUrl(AppSharePreferences sharePreferences){
        return sharePreferences.getNetworkConfig().getBaseNonSecureURL();
    }
}
