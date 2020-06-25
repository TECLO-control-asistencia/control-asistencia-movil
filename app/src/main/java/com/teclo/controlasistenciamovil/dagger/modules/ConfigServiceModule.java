package com.teclo.controlasistenciamovil.dagger.modules;

import android.app.Application;

import com.google.gson.Gson;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.webservices.ClientProfile;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by omhack on 4/6/18.
 */

@Module
public class ConfigServiceModule {
    String baseUrl;

    public ConfigServiceModule(String baseUrl){
        this.baseUrl = baseUrl;
    }

    @Provides
    @Named(Constants.PUBLIC_SERVICE)
    @Singleton
    OkHttpClient provideConfigOkHttpClient(Application app){
        ClientProfile.Builder clientProfileBuilder = new ClientProfile.Builder();
        return clientProfileBuilder.addMetaHeader(app).build().getOkHttpClient();
    }

    @Provides @Singleton
    @Named(Constants.PUBLIC_SERVICE)
    Retrofit provideConfigRetrofit(Gson gson, @Named(Constants.PUBLIC_SERVICE) OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }
}
