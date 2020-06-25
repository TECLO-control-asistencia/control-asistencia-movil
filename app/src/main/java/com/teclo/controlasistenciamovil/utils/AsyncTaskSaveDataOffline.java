package com.teclo.controlasistenciamovil.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.webservices.ITemplateAPI;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.webservices.response.CaAsistanceSavedResponseOffline;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AsyncTaskSaveDataOffline extends AsyncTask<CaRegistroAsistanceVO, Void, CaAsistanceSavedResponseOffline> {

    private String TAG = AsyncTaskSaveDataOffline.class.getSimpleName();
    AsyncTaskSaveDataOffline.AsynckTasckResponseSaveAsistanceOffline listenerResponse;
    AppSharePreferences sharePreferences;
    private WeakReference<Context> mContextRef;
    Retrofit retrofit;
    boolean isOnline;


    public AsyncTaskSaveDataOffline(AsyncTaskSaveDataOffline.AsynckTasckResponseSaveAsistanceOffline listenerResponse,
                                    AppSharePreferences sharePreferences,
                                    Context context, Retrofit retrofit,boolean isOnline){

        this.listenerResponse=listenerResponse;
        this.sharePreferences=sharePreferences;
        this.mContextRef = new WeakReference<>(context);
        this.retrofit=retrofit;
        this.isOnline=isOnline;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listenerResponse.onTasckAsyncSaveAsistanceOfflineIsRuning(true);
        sharePreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(true);
    }


    @Override
    protected CaAsistanceSavedResponseOffline doInBackground(CaRegistroAsistanceVO... caRegistroAsistanceVOS) {

        CaAsistanceSavedResponseOffline reponse;
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);

        if(!isOnline){
            reponse=new CaAsistanceSavedResponseOffline();
            com.teclo.controlasistenciamovil.data.model.Status status = new com.teclo.controlasistenciamovil.data.model.Status();
            status.setCodigo(Constants.STATUS_ERROR_SAVED_ASISTANCE_OFFLINE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            reponse.setStatus(status);
            reponse.setData(false);
            Log.e(TAG, "offline asyncTasck");
            return reponse;
        }

        try{

            Call<CaAsistanceSavedResponseOffline> userCall = service.saveAssitanceOffline(caRegistroAsistanceVOS[0]);
            Response<CaAsistanceSavedResponseOffline> asisteAsistanceResponse=userCall.execute();
            reponse= asisteAsistanceResponse.body();

        }catch (IOException e){

            reponse=new CaAsistanceSavedResponseOffline();
            com.teclo.controlasistenciamovil.data.model.Status status = new com.teclo.controlasistenciamovil.data.model.Status();
            status.setCodigo(Constants.STATUS_ERROR_SAVED_ASISTANCE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            reponse.setStatus(status);
            reponse.setData(false);
            Log.e(TAG, "error in getting response from service using retrofit");

        }

        return reponse;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(CaAsistanceSavedResponseOffline result) {
        super.onPostExecute(result);
        com.teclo.controlasistenciamovil.data.model.Status status=null;

        if(result != null)
            status= result.getStatus();

        if(result != null || status != null) {

            String statusCode = status.getCodigo();
            String decr = status.getDescripcion();

            switch (statusCode) {
                case Constants.STATUS_SUCCESS_SAVED_ASISTANCE_OFFLINE:
                    sharePreferences.deleteAsistance();
                    Log.e(TAG, "saved in  data offline retrofit, not error: ");
                    break;
                case Constants.STATUS_ERROR_SAVED_ASISTANCE_OFFLINE:
                    Log.e(TAG, "error in Ssave data offline retrofit, Error: "+decr);
                    break;
                default:
                    break;
            }
        }else{
            Log.e(TAG, "error asyntTaskOffline");
        }

        listenerResponse.onTasckAsyncSaveAsistanceOfflineIsRuning(false);
        sharePreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(false);
    }

    public interface AsynckTasckResponseSaveAsistanceOffline{
        void onTasckAsyncSaveAsistanceOfflineIsRuning(boolean isRunningAsynTack);
        void onSuccessResponAsyncTacsOffline(boolean isSaved,String mesj);
        void onErrorResponAsyncTacsOffline(boolean isSaved,String mesj);
    }
}