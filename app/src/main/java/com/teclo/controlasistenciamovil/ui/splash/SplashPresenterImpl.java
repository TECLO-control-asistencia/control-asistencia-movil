package com.teclo.controlasistenciamovil.ui.splash;


import com.teclo.controlasistenciamovil.application.PublicMSSessionManager;
import javax.inject.Inject;

/**
 * Created by omhack on 1/24/18.
 */

public class SplashPresenterImpl implements SplashContract.Presenter, SplashContract.LoadConfigCallBack {

    private SplashContract.View view;
    private PublicMSSessionManager publicMSSessionManager;

    @Inject
    public SplashPresenterImpl(SplashContract.View view, PublicMSSessionManager sessionManager) {
        this.view = view;
        this.publicMSSessionManager = sessionManager;
    }

    @Override
    public void loadNetworkConfig(SplashContract.LoadConfigCallBack callBack) {

    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }

    @Override
    public void onSuccess() {
        publicMSSessionManager.createMSSession();
        view.onSuccess();
    }
}
