package com.teclo.controlasistenciamovil.ui.menuinferior;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.ui.listpresence.CaListPresenceContract;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.data.model.Status;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;
import com.teclo.controlasistenciamovil.webservices.response.CaAsistanceSavedResponseOffline;

public class MenuInferiorPresenterImpl implements MenuInferiorContract.Presenter{

    private MenuInferiorContract.View view;
    private TemplateDataSource profileRepository;
    private AppSharePreferences appSharePreferences;

    public MenuInferiorPresenterImpl(MenuInferiorContract.View view,
                                     TemplateDataSource profileRepository, AppSharePreferences appSharePreferences) {
        this.view = view;
        this.profileRepository = profileRepository;
        this.appSharePreferences = appSharePreferences;
    }

    @Override
    public void getMenuDinamico(Long idUser, MenuInferiorContract.OnResponseGetMenuDinmico listener) {

    }

    @Override
    public void getListAllAasistanceOffline(MenuInferiorContract.OnResponeListAllAsistance listener) {
        profileRepository.setLoadLocalData(true);// se coloca la bandera que deacuerdo al eventReceiver se atualiza esta bandera, indicando si existe o no internet

        profileRepository.getAllAsistanceOffline(new BaseCallback<CaRegistroAsistanceVO>() {
            @Override
            public void onSuccess(CaRegistroAsistanceVO caRegistroAsistanceVO) {
                if(caRegistroAsistanceVO != null){
                    listener.onResponseSuccessGetListAllAsistance(caRegistroAsistanceVO);
                }else{
                    listener.onResponseErrorGetListAllAssistance("No se encontraron datos locales");
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                if(serviceError.getStatus() != null){
                    listener.onResponseErrorGetListAllAssistance(serviceError.getStatus().getDescripcion());
                }else {
                    listener.onResponseErrorGetListAllAssistance(serviceError.getClientErrorMessage());
                }
            }
        });
    }

    @Override
    public void saveDataOfflineInServerRemote(CaRegistroAsistanceVO caRegistroAsistanceVO,MenuInferiorContract.OnResponseSaveDataOfflineInServerRemote listener) {
        profileRepository.setLoadLocalData(false);// se coloca la bandera que deacuerdo al evetreceiver se atualiza esta bandera, indicando si existe o no internet

        profileRepository.saveAssitanceOffline(caRegistroAsistanceVO,new BaseCallback<CaAsistanceSavedResponseOffline>() {

            @Override
            public void onSuccess(CaAsistanceSavedResponseOffline caAsistanceSavedResponseOffline) {

                Boolean isSaved =  caAsistanceSavedResponseOffline.getData();
                Status status = caAsistanceSavedResponseOffline.getStatus();
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode){
                    case Constants.STATUS_SUCCESS_SAVED_ASISTANCE_OFFLINE:
                        listener.onSavedSuccess(isSaved);
                        break;
                    case Constants.STATUS_ERROR_SAVED_ASISTANCE_OFFLINE:
                        listener.onSavedErrorr(decr);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(ServiceError serviceError) {
                if(serviceError.getStatus() != null){
                    listener.onSavedErrorr(serviceError.getStatus().getDescripcion());
                }else {
                    listener.onSavedErrorr(serviceError.getClientErrorMessage());
                }
            }
        });
    }

    @Override
    public void getAsistance(boolean isOnline, MenuInferiorContract.OnResponseGetAsistance listenerResponse) {
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

}
