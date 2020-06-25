package com.teclo.controlasistenciamovil.webservices;

import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistryOnLine;
import com.teclo.controlasistenciamovil.webservices.request.TemplateRequest;
import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;
import com.teclo.controlasistenciamovil.webservices.response.CaAsistanceSavedResponseOffline;
import com.teclo.controlasistenciamovil.webservices.response.CaCatJustifyResponse;
import com.teclo.controlasistenciamovil.webservices.response.TemplateResponse;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by omhack on 4/9/18.
 */

public interface ITemplateAPI {

    String UNIQUEID = "uniqueId";
    String PROFILE = "users/{" + UNIQUEID + "}";

    String LOGIN = "login";
    String GET_JUSTIFICACION="movil/obtenerCatJustificaciones";
    String GET_ASISTANCE="movil/getAsistencia";
    String SAVE_ASISTANCE="movil/registroAsistencia";
    String SAVE_ASISTANCE_OFFLINE="movil/registroAsistenciaOffline";


    @GET(PROFILE)
    Call<TemplateResponse> getUserProfile(@Path(UNIQUEID) String uniqueId);

    @POST(PROFILE)
    Call<TemplateResponse> loginWithPin(@Path(UNIQUEID) String uniqueId, @Body TemplateRequest userRequest);

    @PUT(PROFILE)
    Call<Void> updateProfile(@Path(UNIQUEID) String uniqueId);

    @POST(LOGIN)
    Call<UserResponseMod> startLogin(@Body UserMod userMod);

    @GET(GET_JUSTIFICACION)
    Call<CaCatJustifyResponse> getCatJustification();

    @POST(SAVE_ASISTANCE)
    Call<AsistanceResponse> saveAssitance(@Body CaRegistryOnLine caRegistryOnLine);

    @POST(SAVE_ASISTANCE_OFFLINE)
    Call<CaAsistanceSavedResponseOffline> saveAssitanceOffline(@Body CaRegistroAsistanceVO caRegistroAsistanceVO);

    @GET(GET_ASISTANCE)
    Call<AsistanceResponse> getAssistance();
}
