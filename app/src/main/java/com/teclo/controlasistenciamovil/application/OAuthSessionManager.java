package com.teclo.controlasistenciamovil.application;

import com.teclo.controlasistenciamovil.dagger.components.DaggerOAuthComponent;
import com.teclo.controlasistenciamovil.dagger.components.OAuthComponent;
import com.teclo.controlasistenciamovil.dagger.modules.OAuthModule;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by omhack on 4/7/18.
 */
@Singleton
public class OAuthSessionManager {
    private OAuthComponent oAuthComponent;

    @Inject
    public OAuthSessionManager() {
    }

    public void createOAuthSession(String baseMSUrl, String token){
        oAuthComponent = DaggerOAuthComponent.builder()
                .applicationComponent(SicnApp.getAppComponent())
                .oAuthModule(new OAuthModule(baseMSUrl, token))
                .build();
    }

    public void logout(){
        oAuthComponent = null;
    }

    public boolean isAuthenticated(){
        return oAuthComponent !=null;
    }

    public OAuthComponent getOAuthComponent(){
        return oAuthComponent;
    }
}
