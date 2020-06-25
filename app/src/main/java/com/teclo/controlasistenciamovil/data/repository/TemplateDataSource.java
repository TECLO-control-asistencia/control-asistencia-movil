package com.teclo.controlasistenciamovil.data.repository;

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

public interface TemplateDataSource {

    //modificar parámetros a la necesidad específica del proyecto
    void someMethod(String uniqueId, BaseCallback<Void> callback);

    void startLogin(UserMod userMod, BaseCallback<UserResponseMod> callback);

    void setLoadLocalData(boolean loadLocalData);

    void getCatJustification(BaseCallback<CaCatJustifyResponse> callback);

    void saveJutificSelectedLocalRealm(CaJustificationResponseMod caJustificationResponseMod,BaseCallback<CaJustificationResponseMod> callback);

    void getJustificSelectedLocalRealm(BaseCallback<CaJustificationResponseMod> callback);

    void saveAssitance(boolean isRemoteResponse,CaRegAssistanceResponseMod asistanceLocal,
                       BaseCallback<AsistanceResponse> callback);

    void saveAssitanceOffline(CaRegistroAsistanceVO caRegistroAsistanceVO, BaseCallback<CaAsistanceSavedResponseOffline> callback);

    void getAssistance(boolean isOnline,BaseCallback<AsistanceResponse> callback);

    void getAllAsistanceOffline(BaseCallback<CaRegistroAsistanceVO> callback);

}
