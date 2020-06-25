package com.teclo.controlasistenciamovil.data.remote;

import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.data.repository.TemplateDataSource;
import com.teclo.controlasistenciamovil.webservices.BaseCallback;
import com.teclo.controlasistenciamovil.webservices.ITemplateAPI;
import com.teclo.controlasistenciamovil.webservices.RetrofitCallback;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistryOnLine;
import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;
import com.teclo.controlasistenciamovil.webservices.response.CaAsistanceSavedResponseOffline;
import com.teclo.controlasistenciamovil.webservices.response.CaCatJustifyResponse;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by omhack on 4/9/18.
 */

public class TemplateRemoteDataSource implements TemplateDataSource{
    private Retrofit retrofit;

    @Inject
    public TemplateRemoteDataSource(@Named(Constants.OAUTH_MS_SERVICE) Retrofit retrofit) {
        this.retrofit = retrofit;
    }



    @Override
    public void someMethod(String uniqueId, BaseCallback<Void> callback) {
        Log.d("Teclo", "Making retrofit call");
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);
        Call<Void> userCall = service.updateProfile(uniqueId);
        userCall.enqueue(new RetrofitCallback<>(callback));

    }

    @Override
    public void startLogin(UserMod userMod, BaseCallback<UserResponseMod> callback) {
        Log.d("Teclo", "startLogin retrofit call");
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);
        Call<UserResponseMod> userCall = service.startLogin(userMod);
        userCall.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getCatJustification(BaseCallback<CaCatJustifyResponse> callback) {
        Log.d("Teclo", "getCatJustification retrofit call");
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);
        Call<CaCatJustifyResponse> userCall = service.getCatJustification();
        userCall.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void saveJutificSelectedLocalRealm(CaJustificationResponseMod caJustificationResponseMod,BaseCallback<CaJustificationResponseMod> callback) {
        Log.d("Teclo", "retrofit call");
    }

    @Override
    public void getJustificSelectedLocalRealm(BaseCallback<CaJustificationResponseMod> callback) {
        Log.d("Teclo", "retrofit call");
    }

    @Override
    public void saveAssitance(boolean isRemoteResponse,CaRegAssistanceResponseMod asistanceLocal, BaseCallback<AsistanceResponse> callback) {
        Log.d("Teclo", "saveAssitance retrofit call");
    }

    @Override
    public void saveAssitanceOffline(CaRegistroAsistanceVO caRegistroAsistanceVO, BaseCallback<CaAsistanceSavedResponseOffline> callback) {
        Log.d("Teclo", "saveAssitanceOffline retrofit call");
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);
        Call<CaAsistanceSavedResponseOffline> userCall = service.saveAssitanceOffline(caRegistroAsistanceVO);
        userCall.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getAssistance(boolean isOnline,BaseCallback<AsistanceResponse> callback) {
        Log.d("Teclo", "getAssistance retrofit call");
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);
        Call<AsistanceResponse> userCall = service.getAssistance();
        userCall.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getAllAsistanceOffline(BaseCallback<CaRegistroAsistanceVO> callback) {
        Log.d("Teclo", "getAllAsistanceOffline retrofit call");
    }

    @Override
    public void setLoadLocalData(boolean loadLocalData) {

    }
}
