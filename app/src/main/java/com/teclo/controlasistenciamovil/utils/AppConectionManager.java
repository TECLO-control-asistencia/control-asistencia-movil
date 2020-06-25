package com.teclo.controlasistenciamovil.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.teclo.controlasistenciamovil.commons.Constants;

import java.io.IOException;

public class AppConectionManager {

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean isWifiOn(Context context){
        ConnectivityManager connManager1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(mWifi != null && mWifi.isAvailable() && mWifi.isConnected())
            return true;
        else
            return false;
    }

    public static boolean isDataMovileOn(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(mMobile != null &&  mMobile.isAvailable() && mMobile.isConnected())
            return true;
        else
            return false;
    }

    public static String getIpAdressConection(Context context){

        String ip="0.0.0.0.0";

        if(!isWifiOn(context))
            return ip;

        WifiManager wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifi != null){
            ip= Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress());
        }

        return ip;
    }

    public static boolean isAccsessServidor(){

            System.out.println("executeCommand");
            Runtime runtime = Runtime.getRuntime();
            try
            {
                Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + Constants.BASE_URL);
                int mExitValue = mIpAddrProcess.waitFor();
                System.out.println(" mExitValue "+mExitValue);
                if(mExitValue==0){
                    return true;
                }else{
                    return false;
                }
            }
            catch (InterruptedException ignore)
            {
                ignore.printStackTrace();
                System.out.println(" Exception:"+ignore);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println(" Exception:"+e);
            }
            return false;
    }


}
