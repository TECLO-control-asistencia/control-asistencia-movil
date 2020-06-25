package com.teclo.controlasistenciamovil.utils;

import android.Manifest;

import com.teclo.controlasistenciamovil.commons.Constants;

import java.util.ArrayList;
import java.util.List;

public class ListPermissionManager {

    private static List<TypeOfPermissionUtil> listTipePermissionUtil;

    public static List<TypeOfPermissionUtil> getListPermission(){

        if(listTipePermissionUtil == null)
            listTipePermissionUtil= new ArrayList<>();

        listTipePermissionUtil.add(new TypeOfPermissionUtil(Constants.TXT_IMEI,Manifest.permission.READ_PHONE_STATE,null));
        listTipePermissionUtil.add(new TypeOfPermissionUtil(Constants.TXT_GPS,Manifest.permission.ACCESS_FINE_LOCATION,null));

        return listTipePermissionUtil;
    }
}
