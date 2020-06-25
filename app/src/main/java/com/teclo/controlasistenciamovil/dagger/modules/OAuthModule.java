package com.teclo.controlasistenciamovil.dagger.modules;

import android.app.Application;

import com.google.gson.Gson;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.scopes.SessionScope;
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
public class OAuthModule {

    @Named("token") String token;
    @Named("oAuthBaseUrl") String baseMSUrl;

    public OAuthModule(String baseMSUrl, String token) {
        this.token = token;
        this.baseMSUrl = baseMSUrl;
    }

    @Provides
    @Named(Constants.OAUTH_MS_SERVICE)
    @SessionScope
    OkHttpClient provideOAuthOkHttpClient(Application app) {
        ClientProfile.Builder clientProfileBuilder = new ClientProfile.Builder();
        return clientProfileBuilder
                .addOAuth(token)
                .addMetaHeader(app)
                .setTimeOut(13)
                .build().getOkHttpClient();
    }

    @Provides @SessionScope
    @Named(Constants.OAUTH_MS_SERVICE)
    Retrofit provideOAuthMSRetrofit(Gson gson, @Named(Constants.OAUTH_MS_SERVICE)OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseMSUrl)
                .client(okHttpClient)
                .build();
    }
}
