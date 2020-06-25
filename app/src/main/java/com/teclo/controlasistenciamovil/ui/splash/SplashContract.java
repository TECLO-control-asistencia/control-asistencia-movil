package com.teclo.controlasistenciamovil.ui.splash;

/**
 * Created by omhack on 1/24/18.
 */

public interface SplashContract {

    interface View{
        void onError(String message);
        void onSuccess();
    }

    interface Presenter{
        void loadNetworkConfig(LoadConfigCallBack callBack);
    }

    interface LoadConfigCallBack{
        void onError(String message);
        void onSuccess();
    }
}
