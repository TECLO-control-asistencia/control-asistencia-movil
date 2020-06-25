package com.teclo.controlasistenciamovil.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppConectionManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class EventsReceiver extends BroadcastReceiver {

    private static final String TAG = EventsReceiver.class.getSimpleName();

    private boolean isWifiOn=true;
    private boolean isDataMovilOn=true;

    public EventsReceiver(Context context) {

        context.registerReceiver(this, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        context.registerReceiver(this, new IntentFilter(new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)));

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case WifiManager.WIFI_STATE_CHANGED_ACTION:

                int WifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN);

                switch (WifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:

                        if(context instanceof MenuInferiorActivity && !isWifiOn){
                            ((MenuInferiorActivity)context).setWifiOn(true);
                        }
                        isWifiOn=true;
                        break;

                    case WifiManager.WIFI_STATE_DISABLED:

                        if(context instanceof MenuInferiorActivity && isWifiOn){
                            ((MenuInferiorActivity)context).setWifiOn(false);
                        }
                        isWifiOn=false;
                        break;
                }

                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                // off
                if (networkInfo == null) {
                    if(context instanceof MenuInferiorActivity && isDataMovilOn){
                        ((MenuInferiorActivity)context).setDataMovileOn(false);
                    }
                    isDataMovilOn=false;
                }

                // on
                else if (networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                    if(context instanceof MenuInferiorActivity && !isDataMovilOn){
                        ((MenuInferiorActivity)context).setDataMovileOn(true);
                    }
                    isDataMovilOn=true;
                }

                break;
        }

        if(isDataMovilOn || isDataMovilOn){// si alguno esta activado se procede a validar si tiene salida a internet,para proceder con modo offline

            if(AppConectionManager.isOnline(context.getApplicationContext())){// se valida si existe coneccion de datos o wifi, se valida si se tiene salida a internet

                if(context instanceof MenuInferiorActivity){
                    if(!((MenuInferiorActivity)context).isProsesSaveRemoteDateOffline()){// si no existe un proceso de guardado en offline se procede a ejecutar
                        ((MenuInferiorActivity)context).saveDataOffline();
                    }
                }

            }/*else{
                Toast.makeText(context, "Sin acceso a la red, se inicia modo offline", Toast.LENGTH_LONG).show();
            }*/

        }
    }
}
