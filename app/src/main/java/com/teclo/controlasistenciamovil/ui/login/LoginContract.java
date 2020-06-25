package com.teclo.controlasistenciamovil.ui.login;

import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

/**
 * Created by DanielGC on 6/5/19.
 */
public interface LoginContract {
    interface View{

    }

    interface Presenter{
        void starLogin(UserMod userMod, OnResponseLoginUser listener);
       // void
    }

    interface OnResponseLoginUser {
        void onSaveLoginValidationSuccess(UserResponseMod userResponseMod);
        void onSaveLoginValidationError(String message);
    }
}
