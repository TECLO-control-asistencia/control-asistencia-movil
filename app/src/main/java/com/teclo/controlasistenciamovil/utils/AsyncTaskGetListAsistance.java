package com.teclo.controlasistenciamovil.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.ui.listpresence.CaListPresenceContract;
import com.teclo.controlasistenciamovil.ui.register.CaPresenceRegisterContract;
import com.teclo.controlasistenciamovil.webservices.ITemplateAPI;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AsyncTaskGetListAsistance extends AsyncTask<Void, Void, AsistanceResponse> {

    private String TAG = AsyncTaskGetListAsistance.class.getSimpleName();
    public static boolean isRunning;
    AsyncTaskGetListAsistance.AsynckTasckResponseListAsistance listenerResponse;
    public Object presenter;
    private WeakReference<Context> mContextRef;
    private String mensaje=null;
    boolean isOnline=false;
    AppSharePreferences appSharePreferences;


    Retrofit retrofit;

    public AsyncTaskGetListAsistance(AsyncTaskGetListAsistance.AsynckTasckResponseListAsistance listenerResponse,
                                     Object presenter,AppSharePreferences appSharePreferences,
                                     Context context,Retrofit retrofit, boolean isOnline){

        mContextRef = new WeakReference<>(context);
        this.retrofit=retrofit;
        this.listenerResponse=listenerResponse;
        this.presenter=presenter;
        isRunning = false;
        this.isOnline=isOnline;
        this.appSharePreferences=appSharePreferences;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listenerResponse.onTasckAsyncListIsRuning(true);
        appSharePreferences.saveIsAsyncTaskListAsistanceRunningFrReg(true);
    }

    @Override
    protected AsistanceResponse doInBackground(Void... voids) {
        isRunning = true;
        AsistanceResponse reponse;
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);

        if(!isOnline){
            reponse=new AsistanceResponse();
            com.teclo.controlasistenciamovil.data.model.Status status = new com.teclo.controlasistenciamovil.data.model.Status();
            status.setCodigo(Constants.STATUS_ERROR_LIST_ASISTANCE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            reponse.setStatus(status);
            Log.e(TAG, "offline asyncTasck");
            return reponse;
        }

        try{

            Call<AsistanceResponse> userCall = service.getAssistance();
            Response<AsistanceResponse> asisteAsistanceResponse=userCall.execute();
            reponse= asisteAsistanceResponse.body();

        }catch (IOException e){

            reponse=new AsistanceResponse();
            com.teclo.controlasistenciamovil.data.model.Status status = new com.teclo.controlasistenciamovil.data.model.Status();
            status.setCodigo(Constants.STATUS_ERROR_LIST_ASISTANCE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            reponse.setStatus(status);
            Log.e(TAG, "error in getting response from service using retrofit");

        }

        return reponse;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(AsistanceResponse result) {
        super.onPostExecute(result);
        isRunning = false;

        listenerResponse.onTasckAsyncListIsRuning(false);
        appSharePreferences.saveIsAsyncTaskListAsistanceRunningFrReg(false);
        if(result == null){

            listenerResponse.onErrorAsyncTasckListAsistance(Constants.NOT_DATA_FOUND,isOnline);
            Log.e(TAG, "offline asyncTasck");

        }else{

            CaRegAssistanceResponseMod caRegAssistanceResponseMod =  result.getData();
            com.teclo.controlasistenciamovil.data.model.Status status = result.getStatus();

            if(status == null){
                listenerResponse.onErrorAsyncTasckListAsistance(Constants.NOT_DATA_FOUND, isOnline);
            }else{
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode) {
                    case Constants.STATUS_SUCCESS_LIST_ASISTANCE:
                        listenerResponse.onResponseAsyncTasckListAsistance(caRegAssistanceResponseMod, isOnline);
                        if(presenter instanceof CaListPresenceContract.Presenter)
                            ((CaListPresenceContract.Presenter)presenter).savePresence(true,caRegAssistanceResponseMod);
                        else if(presenter instanceof CaPresenceRegisterContract.Presenter)
                            ((CaPresenceRegisterContract.Presenter)presenter).savePresence(true,caRegAssistanceResponseMod);

                        break;
                    case Constants.STATUS_ERROR_LIST_ASISTANCE:
                        listenerResponse.onErrorAsyncTasckListAsistance(decr, isOnline);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    public interface AsynckTasckResponseListAsistance{
        void onTasckAsyncListIsRuning(boolean isRunningAsynTack);
        void onResponseAsyncTasckListAsistance(CaRegAssistanceResponseMod resul,boolean isDateRemote);
        void onErrorAsyncTasckListAsistance(String message,boolean isDateRemote);
    }

}