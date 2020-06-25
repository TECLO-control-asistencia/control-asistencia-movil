package com.teclo.controlasistenciamovil.ui.register;

import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaMoveResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.data.model.Status;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.AsyncTaskGetListAsistance;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;

import io.realm.RealmList;

public class CaPresenceRegisterPresenterImpl implements CaPresenceRegisterContract.Presenter{

    private String TAG = CaPresenceRegisterPresenterImpl.class.getSimpleName();

    private CaPresenceRegisterContract.View view;
    private TemplateDataSource profileRepository;
    private AppSharePreferences appSharePreferences;

    public CaPresenceRegisterPresenterImpl(CaPresenceRegisterContract.View view,
                                           TemplateDataSource profileRepository, AppSharePreferences appSharePreferences) {
        this.view = view;
        this.profileRepository = profileRepository;
        this.appSharePreferences = appSharePreferences;
    }

    @Override
    public void getAsistance(final boolean isOnline, final CaPresenceRegisterContract.OnResponseGetAsistance listenerResponse) {
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
    public void savePresence(boolean isRemoteResponse,CaRegAssistanceResponseMod caRegAssistanceResponseMod) {
        profileRepository.setLoadLocalData(true);

        profileRepository.saveAssitance(isRemoteResponse,caRegAssistanceResponseMod,
                new BaseCallback<AsistanceResponse>() {

            @Override
            public void onSuccess(AsistanceResponse asistanceResponse) {

                CaRegAssistanceResponseMod caRegAssistanceResponseMod =  asistanceResponse.getData();
                Status status = asistanceResponse.getStatus();
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode){
                    case Constants.STATUS_SUCCESS_SAVED_ASISTANCE:
                        Log.e(TAG, "Saved asistance in local");
                        break;
                    case Constants.STATUS_ERROR_LIST_ASISTANCE:
                        Log.e(TAG, "Not saved asistance in local");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                if(serviceError.getStatus() != null){
                    Log.e(TAG, "Not saved asistance in local");
                }else {
                    Log.e(TAG, "Not saved asistance in local");
                }
            }
        });
    }

    @Override
    public void getJustify(CaPresenceRegisterContract.OnResponseGetJustify listenerResponce) {
        profileRepository.setLoadLocalData(true);// si no esta con acceso a internet se obtienen datos locales

        profileRepository.getJustificSelectedLocalRealm(new BaseCallback<CaJustificationResponseMod>() {

            @Override
            public void onSuccess(CaJustificationResponseMod caJustificationResponseMod) {
                if(caJustificationResponseMod != null){
                    listenerResponce.onSuccessGetJustify(caJustificationResponseMod);
                }else{
                    listenerResponce.onErrorGetJustify("No se encontraron resultados");
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                if(serviceError.getStatus() != null){
                    listenerResponce.onErrorGetJustify(serviceError.getStatus().getDescripcion());
                }else {
                    listenerResponce.onErrorGetJustify(serviceError.getClientErrorMessage());
                }
            }
        });
    }
}
