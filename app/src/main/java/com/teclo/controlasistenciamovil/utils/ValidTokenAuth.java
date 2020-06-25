package com.teclo.controlasistenciamovil.utils;

import com.auth0.android.jwt.JWT;

import java.util.Date;

public class ValidTokenAuth {

    public static boolean validTokenAtuh(String token){
        boolean isValidToken=true;

        if(token == null){
            isValidToken=false;
        }else{

            JWT jwt = new JWT(token);
            Long dateLong=Long.parseLong(jwt.getClaim("exp").asString());

            Date dateExpireToken=UtilsDate.getDate(dateLong*1000);
            Date currentDate=new Date();

            String fhExpiracion=UtilsDate.getDate("dd/MM/yyyy hh:mm:ss",dateExpireToken);
            String fhcurrent=UtilsDate.getDate("dd/MM/yyyy hh:mm:ss",currentDate);

            if(dateLong == null || UtilsDate.comparaFecha(currentDate,dateExpireToken)) {
                isValidToken=false;
            }
        }

        return isValidToken;
    }

    public static String getCdUserByToken(String token){
        String cdUser="";
        if(token != null){
            JWT jwt = new JWT(token);
            cdUser=jwt.getClaim("sub").asString();
        }

        return cdUser;
    }

}
