package com.teclo.controlasistenciamovil.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.teclo.controlasistenciamovil.R;


public class CaLoading {

    private static ProgressDialog progressDialog=null;

    private static void createProgressDialog(final Context context,boolean isCancelable){
        if(context != null){
            progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER); //R.style.AppTheme_Dark_Dialog
            progressDialog.setCancelable(isCancelable);
        }
    }

    public static void show(final Context context,String message,boolean isCancelable){
        if(progressDialog == null){
            createProgressDialog(context,isCancelable);
        }

        if(progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(isCancelable);
            progressDialog.show();
        }
    }

    public static void hide(){
       if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
            }
        }
        progressDialog = null;
    }
}