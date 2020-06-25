package com.teclo.controlasistenciamovil.data.repository;

import android.util.Log;

import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;
import com.teclo.controlasistenciamovil.webservices.response.CaAsistanceSavedResponseOffline;
import com.teclo.controlasistenciamovil.webservices.response.CaCatJustifyResponse;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

/**
 * Created by omhack on 4/9/18.
 */

public class TemplateRepository implements TemplateDataSource{
    private TemplateDataSource localDataSource;
    private TemplateDataSource remoteDataSource;
    private boolean loadLocalData;

    public TemplateRepository(TemplateDataSource localDataSource, TemplateDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.loadLocalData = false;
    }

    @Override
    public void someMethod(String uniqueId, BaseCallback<Void> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.someMethod(uniqueId,callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.someMethod(uniqueId, callback);
        }
    }

    @Override
    public void startLogin(UserMod userMod, BaseCallback<UserResponseMod> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.startLogin(userMod,callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.startLogin(userMod, callback);
        }
    }

    @Override
    public void getCatJustification(BaseCallback<CaCatJustifyResponse> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.getCatJustification(callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.getCatJustification(callback);
        }
    }

    @Override
    public void saveJutificSelectedLocalRealm(CaJustificationResponseMod caJustificationResponseMod,BaseCallback<CaJustificationResponseMod> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.saveJutificSelectedLocalRealm(caJustificationResponseMod,callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.saveJutificSelectedLocalRealm(caJustificationResponseMod,callback);
        }
    }

    @Override
    public void getJustificSelectedLocalRealm(BaseCallback<CaJustificationResponseMod> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.getJustificSelectedLocalRealm(callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.getJustificSelectedLocalRealm(callback);
        }
    }

    @Override
    public void saveAssitance(boolean isRemoteResponse,CaRegAssistanceResponseMod asistanceLocal,
                              BaseCallback<AsistanceResponse> callback) {

        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.saveAssitance(isRemoteResponse,asistanceLocal,callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.saveAssitance(isRemoteResponse,asistanceLocal,callback);
        }

    }

    @Override
    public void saveAssitanceOffline(CaRegistroAsistanceVO caRegistroAsistanceVO, BaseCallback<CaAsistanceSavedResponseOffline> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.saveAssitanceOffline(caRegistroAsistanceVO,callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.saveAssitanceOffline(caRegistroAsistanceVO,callback);
        }
    }

    @Override
    public void getAssistance(boolean isOnline,BaseCallback<AsistanceResponse> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            Log.d("Teclo","Datos Locales");
            localDataSource.getAssistance(isOnline,callback);
        }else{
            //cargar los datos remotos
            Log.d("Teclo","Datos remotos");
            remoteDataSource.getAssistance(isOnline,callback);
        }
    }

    @Override
    public void getAllAsistanceOffline(BaseCallback<CaRegistroAsistanceVO> callback) {
        if (loadLocalData) {
            //cargar los datos locales
            //Log.d("Teclo","Datos Locales");
            localDataSource.getAllAsistanceOffline(callback);
        }else{
            //cargar los datos remotos
            //Log.d("Teclo","Datos remotos");
            remoteDataSource.getAllAsistanceOffline(callback);
        }
    }

    @Override
    public void setLoadLocalData(boolean loadLocalData) {
        this.loadLocalData = loadLocalData;
    }
}
