package com.teclo.controlasistenciamovil.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilsDate {

    public UtilsDate(){}

    public static String dateFormat(String format){
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        String str = formatter.format(currentDate);
        return str;
    }

    public static String dateFormat(String format,Long timeMiliseconds){
        Date date = new Date(timeMiliseconds*1000);

        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        String str = formatter.format(date);
        return str;
    }

    public static Date toDate(String format, String value) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format,Locale.getDefault());
        try {
            String myDate = TextUtils.isEmpty(value) ? formatter.format(new Date()) : value;
            date = formatter.parse(myDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }

    public static String getHourMinutes(Date date){
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getDate(String format, Date value){
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        String str = formatter.format(value);
        return str;
    }

    public static Date getDate(Long dateLong){
        Date date=new Date(dateLong);
        return date;
    }


    /**
     * Evalua 2 Fechas y determina si la date1 es mayor a la date2
     * @param date1
     * @param date2
     * @return boolean
     */
    public static boolean comparaFecha(Date date1,Date date2){

        boolean respuesta= false;

        try {
            respuesta=date1.after(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respuesta;
    }

    /**
     * Regresa el numero de dias de diferencia entre dos fechas
     * @param date1
     * @param date2
     * @return boolean
     */
    public static Integer getNumDiasDiferencia(Date date1,Date date2){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dfhUno=dateFormat.format(date1);
        String dfhDos=dateFormat.format(date2);
        int dias=0;
        try {
            date1=dateFormat.parse(dfhUno);
            date2=dateFormat.parse(dfhDos);
            dias=(int) ((date1.getTime()-date2.getTime())/86400000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dias<0)
            dias=(-1*dias);

        return dias;
    }

    /**
     *
     * @param stringDate
     * @return Boolean indicando si la cadena enviada es una fecha
     */
    public static boolean isDate(String stringDate){


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        boolean isFecha=false;

        if(stringDate == null)
            return isFecha;

        try {
            fecha = format.parse(stringDate);
            isFecha=true;
        } catch (Exception e) {
            isFecha=false;
        }
        return isFecha;
    }

}
