package com.teclo.controlasistenciamovil.application;

import com.teclo.controlasistenciamovil.dagger.components.DaggerPublicMSComponent;
import com.teclo.controlasistenciamovil.dagger.components.PublicMSComponent;
import com.teclo.controlasistenciamovil.dagger.modules.PublicMSModule;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by omhack on 4/7/18.
 */
@Singleton
public class PublicMSSessionManager {

    private PublicMSComponent publicMSComponent;

    @Inject
    public PublicMSSessionManager() {
    }

    public void createMSSession(){
        publicMSComponent = DaggerPublicMSComponent.builder()
                .applicationComponent(SicnApp.getAppComponent())
                .publicMSModule(new PublicMSModule())
                .build();
    }

    public PublicMSComponent getPublicMSComponent(){
        return publicMSComponent;
    }

}
