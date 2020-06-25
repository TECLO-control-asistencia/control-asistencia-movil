package com.teclo.controlasistenciamovil.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;

import java.lang.reflect.Type;
import java.util.List;

public class CaPermissionsDialogFrag extends DialogFragment {

    public static final String TAG = CaPermissionsDialogFrag.class.getSimpleName();
    private final int PERMISSION_REQUEST_CODE = 101;

    public static final String LIST_PERMISSION="LIST_PERMISSION";
    public static final String SHOULD_RESOLVE="SHOULD_RESOLVE";
    public static final String SHOULD_RETRY="SHOULD_RETRY";
    public static final String EXTERNAL_GRANT_NEEDED="EXTERNAL_GRANT_NEEDED";

    private Context context;
    private PermissionsGrantedCallback listener;

    private boolean shouldResolve;
    private boolean shouldRetry;
    private boolean externalGrantNeeded;
    private static boolean isShowDialogPermission;

    private android.app.AlertDialog alert = null;

    List<TypeOfPermissionUtil> listTipePermissionUtil;
    private String nameCurrentAcceso;

    @NonNull
    public static CaPermissionsDialogFrag newInstance(List<TypeOfPermissionUtil> listTipePermissionUtil) {
        return new CaPermissionsDialogFrag(listTipePermissionUtil);
    }

    public CaPermissionsDialogFrag() {
    }

    @SuppressLint("ValidFragment")
    public CaPermissionsDialogFrag(List<TypeOfPermissionUtil> listTipePermissionUtil) {
        this.listTipePermissionUtil = listTipePermissionUtil;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof PermissionsGrantedCallback) {
            listener = (PermissionsGrantedCallback) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.PermissionsDialogFragmentStyle);

        if(savedInstanceState != null){
            String listJsonPermission=savedInstanceState.getString(LIST_PERMISSION);
            shouldResolve=savedInstanceState.getBoolean(SHOULD_RESOLVE);
            shouldRetry=savedInstanceState.getBoolean(SHOULD_RETRY);
            externalGrantNeeded=savedInstanceState.getBoolean(EXTERNAL_GRANT_NEEDED);
            isShowDialogPermission=savedInstanceState.getBoolean(Constants.IS_SHOW_DIALOG_PERMISSION);
            Type type = new TypeToken<List<TypeOfPermissionUtil>>(){}.getType();
            Gson gson = new Gson();
            this.listTipePermissionUtil = gson.fromJson(listJsonPermission, type);
        }

        setCancelable(false);
        getNameAcces();

        if(!shouldResolve && !shouldRetry && !externalGrantNeeded && !isShowDialogPermission)
            requestNecessaryPermissions();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        String jsonPermission=new Gson().toJson(this.listTipePermissionUtil);
        outState.putSerializable(LIST_PERMISSION, jsonPermission);
        outState.putBoolean(SHOULD_RESOLVE, shouldResolve);
        outState.putBoolean(SHOULD_RETRY, shouldRetry);
        outState.putBoolean(EXTERNAL_GRANT_NEEDED, externalGrantNeeded);
        outState.putSerializable(Constants.IS_SHOW_DIALOG_PERMISSION,isShowDialogPermission);
    }

    @Override
    public void onResume() {
        super.onResume();
        getNameAcces();
        if (shouldResolve) {
            if(!isShowDialogPermission){
                if (externalGrantNeeded && alert == null) {
                    try{
                        showAppSettingsDialog();
                    }catch (Exception e){}
                } else if (shouldRetry && alert == null) {
                    try{
                        showRetryDialog();
                    }catch (Exception e){}
                } if(alert!= null && !alert.isShowing()){
                    alert.show();
                }
            }
        }else{
            if (isPermiseGranted()) {
                /*if(listener != null)
                    listener.callPermission();*/

                if (alert != null) {

                    alert.dismiss();
                    alert=null;
                }

                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                    dismissAllowingStateLoss();
                else
                    dismiss();
            }
        }
    }

    public boolean isPermiseGranted(){
        for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {
            if (!checkPermissions(typeOfPermissionUtil.getTpPermission())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        listener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        shouldResolve = false;
        shouldRetry = false;
        externalGrantNeeded=false;

        if (permissions.length == 0 && isShowDialogPermission) {

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                dismissAllowingStateLoss();
            else
                dismiss();

            if( alert != null){
                alert.dismiss();
                alert=null;
            }

            return;
        }

        int i;
        for ( i= 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];
            if (!shouldShowRequestPermissionRationale(permission) && grantResult != PackageManager.PERMISSION_GRANTED) {
                shouldResolve = true;
                externalGrantNeeded = true;
                return;
            } else if (grantResult != PackageManager.PERMISSION_GRANTED) {
                shouldResolve = true;
                shouldRetry = true;
                return;
            }else{
                externalGrantNeeded = false;
                shouldRetry = false;
            }
        }

        if(!externalGrantNeeded &&  !shouldRetry &&  isShowDialogPermission){
            //this.alert.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                dismissAllowingStateLoss();
            else{
                dismiss();
            }

            if(alert != null){
                alert.dismiss();
                alert=null;
            }

            if(listener != null)
                listener.callPermission();

            return;
        }
    }

    private void getNameAcces() {
        nameCurrentAcceso = null;
        for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {

            if (typeOfPermissionUtil.getGranted() != null && !typeOfPermissionUtil.getGranted()) {
                if (nameCurrentAcceso == null)
                    nameCurrentAcceso = typeOfPermissionUtil.getNamePermission();
                else
                    nameCurrentAcceso += "," + typeOfPermissionUtil.getNamePermission();
            }

        }

    }

    public boolean checkPermissions(String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestNecessaryPermissions() {

        int sizePendient = 0;

        for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {
            if (!checkPermissions(typeOfPermissionUtil.getTpPermission())) {
                typeOfPermissionUtil.setGranted(false);
                sizePendient++;
            } else {
                typeOfPermissionUtil.setGranted(true);
            }

        }

        String[] remainingPermissions = new String[sizePendient];
        int aux = 0;
        for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {

            if (!checkPermissions(typeOfPermissionUtil.getTpPermission())) {
                remainingPermissions[aux] = typeOfPermissionUtil.getTpPermission();
                aux++;
            }
        }

        if (remainingPermissions.length > 0)
            requestPermissions(remainingPermissions, PERMISSION_REQUEST_CODE);
    }

    private void showAppSettingsDialog() {
        CaLoading.hide();
        if(isShowDialogPermission && alert != null){

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                dismissAllowingStateLoss();
            else
                dismiss();

            alert.dismiss();
            alert=null;
        }

        String message = "Para utilizar la aplicacion se necesita acceso al " + nameCurrentAcceso + ".";
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        builder.setCancelable(false)
                .setTitle("Permisos requeridos")
                .setMessage(message + "Habilite los permisos desde la configuración de la aplicación.")
                .setPositiveButton("CONFIGURAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);

                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                            dismissAllowingStateLoss();
                        else
                            dismiss();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showRetryDialog();
                    }
                });

        alert = builder.create();
        alert.show();


    }

    private void showRetryDialog() {
        CaLoading.hide();
        if(isShowDialogPermission && alert != null){

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                dismissAllowingStateLoss();
            else
                dismiss();

            alert.dismiss();
            alert=null;
        }


        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setCancelable(false)
                .setTitle("Permisos rechazados")
                .setMessage("Para el funcionamiento de la aplicación se necesita acceso al " + nameCurrentAcceso + ".")
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        shouldResolve = false;
                        shouldRetry = false;

                        if(!externalGrantNeeded)
                            requestNecessaryPermissions();
                        else
                            showAppSettingsDialog();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showFinishDialog();
                    }
                });

        alert = builder.create();
        alert.show();

    }

    private void showFinishDialog() {
        CaLoading.hide();

        if(isShowDialogPermission && alert != null){

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                dismissAllowingStateLoss();
            else
                dismiss();

            alert.dismiss();
            alert=null;
        }
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        builder.setCancelable(false)
                .setTitle("Atención")
                .setMessage("Debe permitir el acceso al " + nameCurrentAcceso + " para el funcionamiento de la aplicacion.")
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        shouldResolve = false;
                        shouldRetry = false;

                        if(!externalGrantNeeded)
                            requestNecessaryPermissions();
                        else
                            showAppSettingsDialog();
                    }
                })
                .setNegativeButton("Cerrar aplicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                            dismissAllowingStateLoss();
                        else
                            dismiss();

                        getActivity().finish();
                        System.exit(0);
                    }
                });

        alert = builder.create();
        alert.show();
    }

    public static boolean isShowDialog(){
        return isShowDialogPermission;
    }

    public static void dimmiss(){
        dimmiss();
    }

    public interface PermissionsGrantedCallback {
        void callPermission();
    }

}
