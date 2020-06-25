package com.teclo.controlasistenciamovil.ui.settings;

import android.util.Log;

import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;

public class SettingsPresenterImpl implements SettingsContract.Presenter{

    private SettingsContract.View view;
    private TemplateDataSource profileRepository;
    private AppSharePreferences appSharePreferences;

    public SettingsPresenterImpl(SettingsContract.View view,
                                 TemplateDataSource profileRepository, AppSharePreferences appSharePreferences) {
        this.view = view;
        this.profileRepository = profileRepository;
        this.appSharePreferences = appSharePreferences;
    }

    @Override
    public void someMethodCall() {
        profileRepository.setLoadLocalData(false);
        profileRepository.someMethod("", new BaseCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Creepy","");
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                Log.d("Creepy","");
            }
        });
    }

}
