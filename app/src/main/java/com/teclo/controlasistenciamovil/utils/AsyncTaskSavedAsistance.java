package com.teclo.controlasistenciamovil.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.ui.register.CaPresenceRegisterContract;
import com.teclo.controlasistenciamovil.webservices.ITemplateAPI;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistryOnLine;
import com.teclo.controlasistenciamovil.webservices.response.AsistanceResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AsyncTaskSavedAsistance extends AsyncTask<CaRegAssistanceResponseMod, Void, AsistanceResponse> {


    private String TAG = AsyncTaskSavedAsistance.class.getSimpleName();
    public static boolean isRunning;
    AsyncTaskSavedAsistance.AsynckTasckResponseSaveAsistance listenerResponse;
    CaPresenceRegisterContract.Presenter listernerResponseSave;
    AppSharePreferences sharePreferences;

    private WeakReference<Context> mContextRef;
    private String mensaje=null;
    boolean isOnline=false;


    Retrofit retrofit;

    public AsyncTaskSavedAsistance(AsyncTaskSavedAsistance.AsynckTasckResponseSaveAsistance listenerResponse,
                                   AppSharePreferences sharePreferences,
                                   CaPresenceRegisterContract.Presenter listernerResponseSave,
                                     Context context, Retrofit retrofit, boolean isOnline){

        mContextRef = new WeakReference<>(context);
        this.retrofit=retrofit;
        this.listenerResponse=listenerResponse;
        this.listernerResponseSave=listernerResponseSave;
        isRunning = false;
        this.isOnline=isOnline;
        this.sharePreferences=sharePreferences;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listenerResponse.onTasckAsyncSaveAsistanceIsRuning(true);
        sharePreferences.saveIsAsyncTaskSaveAsistanceRunning(true);
    }

    @Override
    protected AsistanceResponse doInBackground(CaRegAssistanceResponseMod... caRegAssistanceResponseModArray) {

        isRunning = true;
        AsistanceResponse reponse;
        ITemplateAPI service = retrofit.create(ITemplateAPI.class);

        if(!isOnline){
            reponse=new AsistanceResponse();
            com.teclo.controlasistenciamovil.data.model.Status status = new com.teclo.controlasistenciamovil.data.model.Status();
            status.setCodigo(Constants.STATUS_ERROR_SAVED_ASISTANCE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            reponse.setStatus(status);
            reponse.setData(caRegAssistanceResponseModArray[0]);
            Log.e(TAG, "offline asyncTasck");
            return reponse;
        }

        try{

            CaRegistryOnLine caRegistryOnLine=new CaRegistryOnLine();

            caRegistryOnLine.setIdCat(caRegAssistanceResponseModArray[0].getIdCat());
            caRegistryOnLine.setDescripcion(caRegAssistanceResponseModArray[0].getDescripcion());
            caRegistryOnLine.setIdMovimiento(caRegAssistanceResponseModArray[0].getIdMovNextRegister());
            caRegistryOnLine.setLatitud(caRegAssistanceResponseModArray[0].getLatitude());
            caRegistryOnLine.setLongitud(caRegAssistanceResponseModArray[0].getLongitude());
            caRegistryOnLine.setDataMovileOn(caRegAssistanceResponseModArray[0].getDataMovilOn());
            caRegistryOnLine.setWifiOn(caRegAssistanceResponseModArray[0].getWifiOn());

            Call<AsistanceResponse> userCall = service.saveAssitance(caRegistryOnLine);
            Response<AsistanceResponse> asisteAsistanceResponse=userCall.execute();
            reponse= asisteAsistanceResponse.body();

        }catch (IOException e){

            reponse=new AsistanceResponse();
            com.teclo.controlasistenciamovil.data.model.Status status = new com.teclo.controlasistenciamovil.data.model.Status();
            status.setCodigo(Constants.STATUS_ERROR_SAVED_ASISTANCE);
            status.setDescripcion(Constants.NOT_DATA_FOUND);
            reponse.setStatus(status);
            reponse.setData(caRegAssistanceResponseModArray[0]);
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

        listenerResponse.onTasckAsyncSaveAsistanceIsRuning(false);
        sharePreferences.saveIsAsyncTaskSaveAsistanceRunning(false);

        if(result == null){
            listenerResponse.onErrorAsyncTasckSavedAsistance(null,Constants.NOT_DATA_FOUND,isOnline);
        }else{

            CaRegAssistanceResponseMod caRegAssistanceResponseMod =  result.getData();
            com.teclo.controlasistenciamovil.data.model.Status status = result.getStatus();

            if(status == null){
                listenerResponse.onErrorAsyncTasckSavedAsistance(null,Constants.NOT_DATA_FOUND,isOnline);
            }else{
                String statusCode = status.getCodigo();
                String decr = status.getDescripcion();

                switch (statusCode){
                    case Constants.STATUS_SUCCESS_SAVED_ASISTANCE:

                        listernerResponseSave.savePresence(true,caRegAssistanceResponseMod);
                        listenerResponse.onResponseAsyncTasckSavedAsistance(caRegAssistanceResponseMod,isOnline);

                        break;
                    case Constants.STATUS_ERROR_SAVED_ASISTANCE:
                        listernerResponseSave.savePresence(false,caRegAssistanceResponseMod);
                        listenerResponse.onErrorAsyncTasckSavedAsistance(null,decr,isOnline);
                        break;
                    case Constants.STATUS_ERROR_SAVED_ASISTANCE_204:
                        listenerResponse.onErrorAsyncTasckSavedAsistance(Constants.STATUS_ERROR_SAVED_ASISTANCE_204,decr,isOnline);
                    default:
                        break;
                }

            }
        }
    }

    public interface AsynckTasckResponseSaveAsistance{
        void onTasckAsyncSaveAsistanceIsRuning(boolean isRunningAsynTack);
        void onResponseAsyncTasckSavedAsistance(CaRegAssistanceResponseMod resul, boolean isDateRemote);
        void onErrorAsyncTasckSavedAsistance(String cdResponse,String message,boolean isDateRemote);
    }
}
