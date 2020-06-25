package com.teclo.controlasistenciamovil.data.local;

import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaMoveResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.data.model.CaRegistry;
import com.teclo.controlasistenciamovil.data.model.Status;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.utils.AppConectionManager;
import com.teclo.controlasistenciamovil.utils.AppRealmManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.CaValidationAsistance;
import com.teclo.controlasistenciamovil.utils.UtilsDate;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ServiceError;
import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;
import com.teclo.controlasistenciamovil.webservices.response.CaAsistanceSavedResponseOffline;
import com.teclo.controlasistenciamovil.webservices.response.CaCatJustifyResponse;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

/**
 * Created by omhack on 4/9/18.
 */

public class TemplateLocalDataSource implements TemplateDataSource{
    private AppSharePreferences sharePrefs;
    private AppRealmManager realm;

    @Inject
    public TemplateLocalDataSource(AppSharePreferences sharePreferences,AppRealmManager appRealmManager){
        this.sharePrefs = sharePreferences;// se cambia por sqlLite o Realm
        this.realm=appRealmManager;
    }

    @Override
    public void someMethod(String uniqueId, BaseCallback<Void> callback) {
        Log.d("Teclo", "Loading local data");
    }

    @Override
    public void startLogin(UserMod userMod, BaseCallback<UserResponseMod> callback) {
        Log.d("Teclo", "Loading local data Login");

        UserResponseMod userResponseMod = realm.getUserModel();

        if(userResponseMod != null){
            callback.onSuccess(userResponseMod);
        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo("404");
            status.setDescripcion("No se encontraron resultados");
            serviceError.setStatus(status);
            callback.onFailure(serviceError);
        }

    }

    @Override
    public void getCatJustification(BaseCallback<CaCatJustifyResponse> callback) {
        Log.d("Teclo", "Loading local data getCatJustification");
        RealmResults<CaJustificationResponseMod> listCatJustificacion=realm.getListJustificationModel();

        if(listCatJustificacion.size() >0){
            List<CaJustificationResponseMod> listJust=new ArrayList<>();

            for(CaJustificationResponseMod model:listCatJustificacion){
                listJust.add(model);
            }

            Status status=new Status();
            status.setCodigo(Constants.STATUS_SUCCESS_CAT_JUSTIFY);
            status.setDescripcion(Constants.MSJ_SUCCESS);

            CaCatJustifyResponse caCatJustifyResponse=new CaCatJustifyResponse();
            caCatJustifyResponse.setStatus(status);
            caCatJustifyResponse.setData(listJust);

            callback.onSuccess(caCatJustifyResponse);
        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo(Constants.STATUS_ERROR_CAT_JUTIFY);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            serviceError.setStatus(status);
            callback.onFailure(serviceError);
        }
    }

    // se guardan y obtienen en el sahredPreferences
    @Override
    public void saveJutificSelectedLocalRealm(CaJustificationResponseMod caJustificationResponseMod,BaseCallback<CaJustificationResponseMod> callback) {
        Log.d("Teclo", "saveJutificSelectedLocalRealm");

        boolean isSaved=sharePrefs.saveJustificationModel(caJustificationResponseMod);

        if(isSaved){
            caJustificationResponseMod=sharePrefs.getJustificationModel();
            callback.onSuccess(caJustificationResponseMod);
        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo("409");
            status.setDescripcion("No se gurado el registro");
            serviceError.setStatus(status);
            callback.onFailure(serviceError);
        }
    }

    @Override
    public void getJustificSelectedLocalRealm(BaseCallback<CaJustificationResponseMod> callback) {
        CaJustificationResponseMod caJustificationResponseMod=sharePrefs.getJustificationModel();

        if(caJustificationResponseMod !=null){
            callback.onSuccess(caJustificationResponseMod);
        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo("404");
            status.setDescripcion("No se encontraron resultados");
            serviceError.setStatus(status);
            callback.onFailure(serviceError);
        }
    }

    @Override
    public void saveAssitance(boolean isRemoteResponse,CaRegAssistanceResponseMod asistanceLocal,
                              BaseCallback<AsistanceResponse> callback) {

        Log.d("Teclo", "Saving local data saveAssitance");
        UserResponseMod userMod=sharePrefs.getDateUserLogin();
        String fhRegisterValid=UtilsDate.getDate("dd/MM/yyyy",new Date());
        asistanceLocal.setCdUser(userMod.getPoliceBadge());
        asistanceLocal.setFhRegister(fhRegisterValid);
        CaRegAssistanceResponseMod lastAsistanceRegister=sharePrefs.getAsistaneLastSavedOffline();

        boolean isEqualsNewRegisterAndNewRegister=CaValidationAsistance.isEquals(lastAsistanceRegister,asistanceLocal);

        if(isRemoteResponse && (lastAsistanceRegister == null
                || !isEqualsNewRegisterAndNewRegister)){// se revalida la coneccion debido a posiblemente se ejecute de manera manual este metodo local
            asistanceLocal=sharePrefs.saveAssistanceModel(asistanceLocal);

        }else if(!isRemoteResponse){

            Date fhRegister= new Date();

            for(int i=0; i<asistanceLocal.getListAsistance().size(); i++){
                if(asistanceLocal.getIdMovNextRegister().equals(asistanceLocal.getListAsistance().get(i).getId())){
                    String cadenFh=UtilsDate.getDate("dd/MM/yyyy",fhRegister);
                    String cadenHR=UtilsDate.getDate("HH:mm:ss",fhRegister);
                    asistanceLocal.getListAsistance().get(i).setMovDate(cadenFh);
                    asistanceLocal.getListAsistance().get(i).setMovHour(cadenHR);
                    asistanceLocal.getListAsistance().get(i).setLongitud(asistanceLocal.getLongitude());
                    asistanceLocal.getListAsistance().get(i).setLatitud(asistanceLocal.getLatitude());
                    asistanceLocal.getListAsistance().get(i).setDataMovileOn(asistanceLocal.getDataMovilOn());
                    asistanceLocal.getListAsistance().get(i).setWifiOn(asistanceLocal.getWifiOn());
                    asistanceLocal.getListAsistance().get(i).setDateOffline(true);
                    asistanceLocal.getListAsistance().get(i).setCdUser(userMod.getPoliceBadge());
                    break;
                }
            }

            asistanceLocal=sharePrefs.saveAssistanceModel(asistanceLocal);

            if (asistanceLocal.getTpTurno().equals(Constants.NIGHT_SHIFT))
                CaValidationAsistance.validAsistanceNightShift(asistanceLocal);
            else if (asistanceLocal.getTpTurno().equals(Constants.DAY_SHIFT))
                CaValidationAsistance.validAsistanceDayShift(asistanceLocal);
        }

        if(asistanceLocal!=null){


            Status status=new Status();
            status.setCodigo(Constants.STATUS_SUCCESS_SAVED_ASISTANCE);
            status.setDescripcion(Constants.MSJ_CREATE);

            AsistanceResponse asistanceResponse=new AsistanceResponse();
            asistanceResponse.setStatus(status);
            asistanceResponse.setData(asistanceLocal);


            callback.onSuccess(asistanceResponse);
        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo(Constants.STATUS_ERROR_SAVED_ASISTANCE);
            status.setDescripcion(Constants.MSJ_NOT_CREATE);
            serviceError.setStatus(status);
            callback.onFailure(serviceError);
        }
    }

    @Override
    public void saveAssitanceOffline(CaRegistroAsistanceVO caRegistroAsistanceVO, BaseCallback<CaAsistanceSavedResponseOffline> callback) {
        Log.d("Teclo", "Loading local data saveAssitanceOffline");
        ServiceError serviceError = new ServiceError();
        serviceError.setClientErrorMessage("No se guarda en local");
        callback.onFailure(serviceError);
    }

    @Override
    public void getAssistance(boolean isOnline,BaseCallback<AsistanceResponse> callback) {

        CaRegAssistanceResponseMod asistanceLocal;
        asistanceLocal=sharePrefs.getAsistaneLastSavedOffline();

        if(asistanceLocal != null && asistanceLocal.getListAsistance() != null){
            // se coloca validacion para mostrar los mov registrados y pendientes por registrar asi como mostrar los componentes ativos o inactivos en UI

            if(!isOnline){

                if(asistanceLocal.getTpTurno().equals(Constants.DAY_SHIFT)){
                    CaValidationAsistance.validAsistanceDayShift(asistanceLocal);
                }else if(asistanceLocal.getTpTurno().equals(Constants.NIGHT_SHIFT)){
                    CaValidationAsistance.validAsistanceNightShift(asistanceLocal);
                }

            }

            Status status=new Status();
            status.setCodigo(Constants.STATUS_SUCCESS_LIST_ASISTANCE);
            status.setDescripcion(Constants.MSJ_SUCCESS);

            AsistanceResponse asistanceResponse=new AsistanceResponse();
            asistanceResponse.setStatus(status);
            asistanceResponse.setData(asistanceLocal);

            Log.d("Teclo", "onSuccess data Asistance Offline");
            callback.onSuccess(asistanceResponse);

        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo(Constants.STATUS_ERROR_LIST_ASISTANCE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            serviceError.setStatus(status);
            Log.d("Teclo", "onFailure data Asistance Offline");
            callback.onFailure(serviceError);
        }
    }

    @Override
    public void getAllAsistanceOffline(BaseCallback<CaRegistroAsistanceVO> callback) {

        CaRegistroAsistanceVO caRegistroAsistanceVO=new CaRegistroAsistanceVO();

        List<CaRegAssistanceResponseMod> listAsistanceOffline=sharePrefs.getAsistaneAllSavedOffline();

        if(listAsistanceOffline != null && !listAsistanceOffline.isEmpty()){

            List<CaRegistry> listAisistance = new ArrayList<>();
            CaRegistry caRegistry;

            for(CaRegAssistanceResponseMod dataOffline: listAsistanceOffline){
                if(dataOffline.getListAsistance() != null){
                    for(CaMoveResponseMod mov:dataOffline.getListAsistance()){

                        if(mov.isDateOffline()){

                            caRegistry = new CaRegistry();
                            caRegistry.setCdUser(mov.getCdUser());
                            caRegistry.setDateRegister(mov.getMovDate()+" "+mov.getMovHour());
                            caRegistry.setIdMov(mov.getId());
                            caRegistry.setLatitud(mov.getLatitud());
                            caRegistry.setLongitud(mov.getLongitud());
                            caRegistry.setDataMovileOn(mov.isDataMovileOn());
                            caRegistry.setIdCat(dataOffline.getIdCat());
                            caRegistry.setDescripcion(dataOffline.getDescripcion());
                            caRegistry.setWifiOn(mov.isWifiOn());

                            listAisistance.add(caRegistry);
                        }
                    }
                }
            }

            if(listAisistance != null && !listAisistance.isEmpty()){

                caRegistroAsistanceVO.setListAisistance(listAisistance);
                callback.onSuccess(caRegistroAsistanceVO);

            }else{

                ServiceError serviceError = new ServiceError();
                Status status=new Status();
                status.setCodigo("404");
                status.setDescripcion("No se encontraron resultados locales for savedOffline");
                serviceError.setStatus(status);
                callback.onFailure(serviceError);
            }

        }else{
            ServiceError serviceError = new ServiceError();
            Status status=new Status();
            status.setCodigo("404");
            status.setDescripcion("No se encontraron resultados locales for savedOffline");
            serviceError.setStatus(status);
            callback.onFailure(serviceError);
        }
    }
    //  Fin metodos que se guardan y obtienen en el sahredPreferences

    @Override
    public void setLoadLocalData(boolean loadLocalData) {

    }
}