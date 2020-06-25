package com.teclo.controlasistenciamovil.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.data.model.NetworkConfig;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by omhack on 4/6/18.
 */

public class AppSharePreferences {
    private static final String FILE_NAME = "sicn_app_preferences";
    private static final String PREF_TUTORIAL_SEEN = "tutorial_seen";
    private static final String UNIQUE_ID = "unique_id";
    private static final String NETWORK_CONFIG = "network_config";
    private static final String ENCRYPTED_TOKEN = "encrypted_token";
    private static final String SAVE_DATA_USER_LOGIN="save_data_user_login";
    private static final String SAVE_IMEI_DEVICE="save_imei_device";

    /**
     * Metodos para manego de asistencia en modo offline usando SharePreferences
     */
    private static final String ASISTANCE="list_asistance";// guardar y obtener
    private static final String JUSTIFY="justify";// guardar y obtener
    private static final String IS_PENDIENT_JUSTIFY="is_pendient_justify";
    private static final String CAT_LIST_JUSTIFY="CAT_LIST_JUSTIFY";



    private SharedPreferences preferences;
    private Context context;

    public AppSharePreferences(Context context) {
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.context=context;
    }

    public Context getContext(){
        return this.context;
    }

    public boolean isTutorialSeen() {
        return preferences.getBoolean(PREF_TUTORIAL_SEEN, false);
    }

    public void setTutorialSeen(boolean tutorialSeen) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_TUTORIAL_SEEN, tutorialSeen).apply();
    }

    public String getUniqueId(){
        return preferences.getString(UNIQUE_ID, "");
    }

    public void setUniqueId(String uniqueId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UNIQUE_ID, uniqueId);
        editor.apply();
    }


    public void saveNetworkConfig(NetworkConfig networkConfig) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NETWORK_CONFIG,new Gson().toJson(networkConfig));
        editor.apply();
    }

    public NetworkConfig getNetworkConfig() {
        return new Gson().fromJson(preferences.getString(NETWORK_CONFIG,""),NetworkConfig.class);
    }

    public String getEncryptedToken() {
        return preferences.getString(ENCRYPTED_TOKEN, null);
    }

    public void setEncryptedToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ENCRYPTED_TOKEN, token);
        editor.apply();
    }

    public void saveDateUserLogin(UserResponseMod userResponseMod){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVE_DATA_USER_LOGIN,new Gson().toJson(userResponseMod));
        editor.apply();
    }

    public UserResponseMod getDateUserLogin(){
        return new Gson().fromJson(preferences.getString(SAVE_DATA_USER_LOGIN,""),UserResponseMod.class);
    }

    public void saveImeiDevice(List<String> listImei) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVE_IMEI_DEVICE,new Gson().toJson(listImei));
        editor.apply();
        Log.d("Teclo", "saveImeiDevice");
    }

    public List<String> getImeiDevice() {

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        String json = preferences.getString(SAVE_IMEI_DEVICE, null);
        List<String> listMenus = gson.fromJson(json, type);

        return listMenus;
    }


    /**
     * Metodos para manego de asistencia en modo offline usando SharePreferences
     */


    public CaRegAssistanceResponseMod saveAssistanceModel(final CaRegAssistanceResponseMod model) {

        List<CaRegAssistanceResponseMod> listAsistanceAll= getAsistaneAllSavedOffline();

        if(listAsistanceAll == null){

            listAsistanceAll=new ArrayList<>();

        }if(model.getId() == null){
            // se crea otro objeto nuevo PENDIENTE DE VALIDAR CUANDO SE GUARDA EN OFFLINE ESTO ES NULL

            model.setId((listAsistanceAll.size()+1));
            listAsistanceAll.add(model);

        }else if(model.getId() != null){

            int i;

            for(i=0; i<listAsistanceAll.size(); i++){
                if(listAsistanceAll.get(i).getId().equals(model.getId())){
                    listAsistanceAll.set(i,model);
                    break;
                }
            }

        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ASISTANCE,new Gson().toJson(listAsistanceAll));
        editor.apply();
        Log.d("Teclo", "saveAssistanceModel offline local");

        return getAsistaneLastSavedOffline();
    }

    public void deleteAsistance(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ASISTANCE, null);
        editor.apply();
    }

    public CaRegAssistanceResponseMod getAsistaneLastSavedOffline(){

        List<CaRegAssistanceResponseMod> listAsistanceAll= getAsistaneAllSavedOffline();
        UserResponseMod userMod=getDateUserLogin();

        if(userMod != null){
            int i;
            int sizeList=listAsistanceAll == null ? 0 : listAsistanceAll.size();

            int j;
            for(j=(sizeList-1); j>=0; j--){
                if(listAsistanceAll.get(j).getCdUser() != null && listAsistanceAll.get(j).getCdUser().equals(userMod.getPoliceBadge()) &&
                        UtilsDate.getDate("dd/MM/yyyy",new Date()).equals(listAsistanceAll.get(j).getFhRegister())){
                    return listAsistanceAll.get(j);
                }
            }

            for(i=(sizeList-1); i>=0; i--){
                if(listAsistanceAll.get(i).getCdUser().equals(userMod.getPoliceBadge())){
                    return listAsistanceAll.get(i);
                }
            }
        }

        return null;
    }

    public List<CaRegAssistanceResponseMod> getAsistaneAllSavedOffline(){
        Gson gson = new Gson();
        Type type = new TypeToken<List<CaRegAssistanceResponseMod>>(){}.getType();
        String json = preferences.getString(ASISTANCE, null);
        List<CaRegAssistanceResponseMod> listAsistance = gson.fromJson(json, type);

        return listAsistance;
    }

    public boolean saveJustificationModel(final CaJustificationResponseMod justifyModel) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(JUSTIFY,new Gson().toJson(justifyModel));
        editor.apply();
        Log.d("Teclo", "saveJustificationModel offline local");

        return true;
    }

    public void deleteDataJustify(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(JUSTIFY, null);
        editor.apply();
    }


    public CaJustificationResponseMod getJustificationModel() {

        Gson gson = new Gson();
        Type type = new TypeToken<CaJustificationResponseMod>(){}.getType();
        String json = preferences.getString(JUSTIFY, null);
        CaJustificationResponseMod justificacion = gson.fromJson(json, type);

        return justificacion;
    }

    public void savedJustificationPendiete(Boolean isPenditene){

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IS_PENDIENT_JUSTIFY,new Gson().toJson(isPenditene));
        editor.apply();
        Log.d("Teclo", "savedJustificationPendiete offline local");

    }

    public Boolean isJustificacionPendiente(){

        Gson gson = new Gson();
        Type type = new TypeToken<Boolean>(){}.getType();
        String json = preferences.getString(IS_PENDIENT_JUSTIFY, null);
        Boolean isPendiet = gson.fromJson(json, type);

        if(isPendiet==null)
            return false;

        return isPendiet;
    }

    public void saveIsAsyncTaskSaveAsistanceRunning(Boolean isRunning){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.ACTION_ASYNC_TASK_SAVED_IS_RUNNI_FR_REG,isRunning);
        editor.apply();
    }

    public boolean getIsAsyncTaskSaveAsistanceRunning(){
      return preferences.getBoolean(Constants.ACTION_ASYNC_TASK_SAVED_IS_RUNNI_FR_REG, false);
    }

    public void saveIsAsyncTaskSaveAsistanceOfflineRunning(Boolean isRunning){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.ACTION_ASYNC_TASK_SAVED_OFFLINE_IS_RUNNIG,isRunning);
        editor.apply();
    }

    public boolean getIsAsyncTaskSaveAsistanceOfflineRunning(){
        return preferences.getBoolean(Constants.ACTION_ASYNC_TASK_SAVED_OFFLINE_IS_RUNNIG, false);
    }

    public void saveIsAsyncTaskListAsistanceRunningFrList(Boolean isRunning){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_LIS,isRunning);
        editor.apply();
    }

    public boolean getIsAsyncTaskListAsistanceRunningFrList(){
        return preferences.getBoolean(Constants.ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_LIS, false);
    }

    public void saveIsAsyncTaskListAsistanceRunningFrReg(Boolean isRunning){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_REG,isRunning);
        editor.apply();
    }

    public boolean getIsAsyncTaskListAsistanceRunningFrReg(){
        return preferences.getBoolean(Constants.ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_REG, false);
    }


    public void saveCatJustify(List<CaJustificationResponseMod> listCatJustific){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CAT_LIST_JUSTIFY,new Gson().toJson(listCatJustific));
        editor.apply();
    }

    public List<CaJustificationResponseMod>  getCatJustify(){

        Gson gson = new Gson();
        Type type = new TypeToken<List<CaJustificationResponseMod> >(){}.getType();
        String json = preferences.getString(CAT_LIST_JUSTIFY, null);
        List<CaJustificationResponseMod> listCat = gson.fromJson(json, type);
        return listCat;

    }


}