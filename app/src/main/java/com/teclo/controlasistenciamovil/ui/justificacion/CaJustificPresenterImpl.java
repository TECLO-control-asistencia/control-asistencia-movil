package com.teclo.controlasistenciamovil.ui.justificacion;

import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.Status;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.ui.login.LoginContract;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;
import com.teclo.controlasistenciamovil.webservices.response.CaCatJustifyResponse;

import java.util.List;

public class CaJustificPresenterImpl implements CaJustificContract.Presenter{

    private CaJustificContract.View view;
    private TemplateDataSource profileRepository;
    private AppSharePreferences appSharePreferences;

    public CaJustificPresenterImpl(CaJustificContract.View view,
                              TemplateDataSource profileRepository, AppSharePreferences appSharePreferences) {
        this.view = view;
        this.profileRepository = profileRepository;
        this.appSharePreferences = appSharePreferences;
    }


    @Override
    public void getCatJustific(CaJustificContract.OnResponseCatJustific listener) {
        profileRepository.setLoadLocalData(false);
        profileRepository.getCatJustification(new BaseCallback<CaCatJustifyResponse>() {

            @Override
            public void onSuccess(CaCatJustifyResponse responseCatJustify) {

                List<CaJustificationResponseMod> listCatJustificacion =  responseCatJustify.getData();
                Status status = responseCatJustify.getStatus();
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode){
                    case Constants.STATUS_SUCCESS_CAT_JUSTIFY:
                        listener.onGetCatJustificValidationSuccess(listCatJustificacion);
                        break;
                    case Constants.STATUS_ERROR_CAT_JUTIFY:
                        listener.onGetCatJustificValidationError(decr);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                Log.d("Teclo","Error en getCatJustificacion");
                if(serviceError.getStatus() != null){
                    listener.onGetCatJustificValidationError(serviceError.getStatus().getDescripcion());
                }else {
                    listener.onGetCatJustificValidationError(serviceError.getClientErrorMessage());
                }
            }
        });
    }

    @Override
    public void saveJutificSelectedLocalRealm(CaJustificationResponseMod caJustificationResponseMod, CaJustificContract.OnResponseCatJustific listener) {
        profileRepository.setLoadLocalData(true);
        profileRepository.saveJutificSelectedLocalRealm(caJustificationResponseMod,new BaseCallback<CaJustificationResponseMod>() {

            @Override
            public void onSuccess(CaJustificationResponseMod catJusSelected) {

                if(catJusSelected != null){
                    listener.onSaveJustificSelectedSuccess(catJusSelected);
                }else{
                    listener.onSaveJustificSelectedError("No se guardo el registro");
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                Log.d("Teclo","Error en save justific en realm");
                if(serviceError.getStatus() != null){
                    listener.onSaveJustificSelectedError(serviceError.getStatus().getDescripcion());
                }else {
                    listener.onSaveJustificSelectedError(serviceError.getClientErrorMessage());
                }
            }
        });
    }
}
