package com.teclo.controlasistenciamovil.ui.settings;

public class SettingsContract {
    interface View{
        void showProgressBar(boolean show);
    }

    interface Presenter{
        void someMethodCall();
    }

    interface OnResponseGetPetition{}

}
