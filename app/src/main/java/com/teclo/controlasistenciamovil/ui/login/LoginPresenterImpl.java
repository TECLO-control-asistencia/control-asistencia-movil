package com.teclo.controlasistenciamovil.ui.login;

import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;
import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

public class LoginPresenterImpl implements LoginContract.Presenter{

    private LoginContract.View view;
    private TemplateDataSource profileRepository;
    private AppSharePreferences appSharePreferences;

    public LoginPresenterImpl(LoginContract.View view,
                             TemplateDataSource profileRepository, AppSharePreferences appSharePreferences) {
        this.view = view;
        this.profileRepository = profileRepository;
        this.appSharePreferences = appSharePreferences;
    }

    @Override
    public void starLogin(UserMod userMod, LoginContract.OnResponseLoginUser listener) {
        profileRepository.setLoadLocalData(false);// se coloca la bandera que deacuerdo al evetreceiver se atualiza esta bandera, indicando si existe o no internet
        profileRepository.startLogin(userMod, new BaseCallback<UserResponseMod>() {

            @Override
            public void onSuccess(UserResponseMod userResponseMod) {


                if(userResponseMod.getToken() == null){

                    listener.onSaveLoginValidationError("Usuario o Contraseña incorrecto");

                }else{

                    JWT jwt = new JWT(userResponseMod.getToken());
                    userResponseMod.setFirstName(jwt.getClaim("name").asString());
                    userResponseMod.setUserName(jwt.getClaim("sub").asString());
                    listener.onSaveLoginValidationSuccess(userResponseMod);
                }

            }

            @Override
            public void onFailure(ServiceError serviceError) {
                Log.d("Creepy","Error en Login");
                if(serviceError.getStatus() != null){
                    listener.onSaveLoginValidationError(serviceError.getStatus().getDescripcion());
                }else {

                    listener.onSaveLoginValidationError(Constants.SERVICE_NO_DIPONIBLE);
                }
            }
        });
    }





}
