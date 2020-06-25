package com.teclo.controlasistenciamovil.ui.listpresence;

import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.data.model.Status;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.ui.register.CaPresenceRegisterContract;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;

public class CaListPresencePresenterImpl implements CaListPresenceContract.Presenter{

    private CaListPresenceContract.View view;
    private TemplateDataSource profileRepository;
    private AppSharePreferences appSharePreferences;

    public CaListPresencePresenterImpl(CaListPresenceContract.View view,
                                       TemplateDataSource profileRepository, AppSharePreferences appSharePreferences) {
        this.view = view;
        this.profileRepository = profileRepository;
        this.appSharePreferences = appSharePreferences;
    }

    @Override
    public void getAsistance(boolean isOnline, CaListPresenceContract.OnResponseGetAsistance listenerResponse) {
        profileRepository.setLoadLocalData(!isOnline);// si no esta con acceso a internet se obtienen datos locales
        profileRepository.getAssistance(isOnline,new BaseCallback<AsistanceResponse>() {

            @Override
            public void onSuccess(AsistanceResponse asistanceResponse) {

                CaRegAssistanceResponseMod caRegAssistanceResponseMod =  asistanceResponse.getData();
                Status status = asistanceResponse.getStatus();
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode){
                    case Constants.STATUS_SUCCESS_LIST_ASISTANCE:
                        listenerResponse.onSuccessGetAsistance(caRegAssistanceResponseMod,isOnline);
                        break;
                    case Constants.STATUS_ERROR_LIST_ASISTANCE:
                        listenerResponse.onErrorGetAsistane(decr,isOnline);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                if(serviceError.getStatus() != null){
                    listenerResponse.onErrorGetAsistane(serviceError.getStatus().getDescripcion(),isOnline);
                }else {
                    listenerResponse.onErrorGetAsistane(serviceError.getClientErrorMessage(),isOnline);
                }
            }
        });
    }


    @Override
    public void savePresence(boolean isOnline, CaRegAssistanceResponseMod caRegAssistanceResponseMod) {
        profileRepository.setLoadLocalData(!isOnline);// si no esta con acceso a internet se obtienen datos locales

        profileRepository.saveAssitance(isOnline,caRegAssistanceResponseMod,
                new BaseCallback<AsistanceResponse>() {

            @Override
            public void onSuccess(AsistanceResponse asistanceResponse) {

                CaRegAssistanceResponseMod caRegAssistanceResponseMod =  asistanceResponse.getData();
                Status status = asistanceResponse.getStatus();
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode){
                    case Constants.STATUS_SUCCESS_SAVED_ASISTANCE:
                        Log.d("Teclo","Save listAsistance in Local");
                        break;
                    case Constants.STATUS_ERROR_LIST_ASISTANCE:
                        Log.d("Teclo","Save error listAsistance in Local");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                if(serviceError.getStatus() != null){
                    Log.d("Teclo","Save error listAsistance in Local");
                }else {
                    Log.d("Teclo","Save error listAsistance in Local");
                }
            }
        });
    }

}
