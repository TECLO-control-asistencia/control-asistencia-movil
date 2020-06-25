package com.teclo.controlasistenciamovil.utils;

import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaMoveResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;

import java.util.Date;

import io.realm.RealmList;

public class CaValidationAsistance {

    public static void validAsistanceNightShift(CaRegAssistanceResponseMod asisTanceResult) {

        CaMoveResponseMod movLastRegister = getDataLastMovRegister(asisTanceResult.getListAsistance());

        if (movLastRegister == null){
            // si la hora actual es mayor a las 12 pm y no existe ningun mov nocturno registrado se reinica la lista de asistencia
            // O si la hora actual es menor a las 12pm y no esiste ningun mov diurno resgistrado se reinicia la lista de asistencia

            resetObjectAndListAsistance(asisTanceResult);
            updateAssistaceModelOfflie(asisTanceResult);

        } else if(movLastRegister != null){

            Date fhCurrent=new Date();
            Date fhLastMovRegistred=UtilsDate.toDate("dd/MM/yyyy",movLastRegister.getMovDate());

            Integer numDiasDif=UtilsDate.getNumDiasDiferencia(fhCurrent,fhLastMovRegistred);

            if(numDiasDif >= 2 || ((numDiasDif == 1 || numDiasDif == 0) && isCurrentHourAfterTwelvePM())
                    && movLastRegister.getTpMov().equals(Constants.MOV_DAY) ){// SI ES MAYOR A 2 DIAS O SI ES UN DIA O EL MISMO DIA Y SON MAS DE LAS 12 PM Y EL ULTIMO MOV ES DIURNO SE RESETEA LA LSITA DE ASISTENCIA
                resetObjectAndListAsistance(asisTanceResult);
            }

            if(numDiasDif >0
                    && movLastRegister.getTpMov().equals(Constants.NIGHT_SHIFT)){// SI ES MAYOR A 2 DIAS O SI ES UN DIA O EL MISMO DIA Y SON MAS DE LAS 12 PM Y EL ULTIMO MOV ES DIURNO SE RESETEA LA LSITA DE ASISTENCIA
                resetObjectAndListAsistance(asisTanceResult);
            }

            updateAssistaceModelOfflie(asisTanceResult);
        }

        asignaNextMovReister(asisTanceResult);
    }

    public static void validAsistanceDayShift(CaRegAssistanceResponseMod asisTanceResult){

        CaMoveResponseMod movLastRegister = getDataLastMovRegister(asisTanceResult.getListAsistance());

        if(movLastRegister == null){

            resetObjectAndListAsistance(asisTanceResult);

        }else {


            Date fhCurrent=new Date();
            Date fhLastMovRegistred=UtilsDate.toDate("dd/MM/yyyy",movLastRegister.getMovDate());

            Integer numDiasDif=UtilsDate.getNumDiasDiferencia(fhCurrent,fhLastMovRegistred);

            if(numDiasDif >=1){

                resetObjectAndListAsistance(asisTanceResult);

            }else{


                CaMoveResponseMod nextMovToRegister = netxMovRegistrar(asisTanceResult.getListAsistance());

                if (nextMovToRegister == null) {

                    asisTanceResult.setMessageMov(Constants.MSG_FINAL_MOV_SHIFT);
                    asisTanceResult.setBtnRegisterDisabled(true);
                    asisTanceResult.setIdMovNextRegister(null);

                } else {
                    asisTanceResult.setMessageMov(nextMovToRegister.getMovType());
                    asisTanceResult.setBtnRegisterDisabled(false);
                    asisTanceResult.setIdMovNextRegister(nextMovToRegister.getId());
                }
            }

        }

    }

    private static void asignaNextMovReister(CaRegAssistanceResponseMod asisTanceResult){

        CaMoveResponseMod nextMovToRegister = netxMovRegistrar(asisTanceResult.getListAsistance());

        if (nextMovToRegister == null) {

            if (isCurrentHourAfterTwelvePM()) {
                asisTanceResult.setMessageMov(Constants.MSG_FINAL_MOV_DAY);
            } else if (isCurrentHourBeforeTwelvePM()) {
                asisTanceResult.setMessageMov(Constants.MSG_FINAL_MOV_SHIFT);
            }

            asisTanceResult.setBtnRegisterDisabled(true);
            asisTanceResult.setIdMovNextRegister(null);

        }else{

            if (isCurrentHourAfterTwelvePM() && nextMovToRegister.getTpMov().equals(Constants.MOV_DAY)) {
                asisTanceResult.setBtnRegisterDisabled(true);
                asisTanceResult.setMessageMov(Constants.MSG_FINAL_MOV_DAY);
            } else if (isCurrentHourBeforeTwelvePM() && nextMovToRegister.getTpMov().equals(Constants.MOV_NIGHT)) {
                asisTanceResult.setBtnRegisterDisabled(true);
                asisTanceResult.setMessageMov(Constants.MSG_FINAL_MOV_SHIFT);
            }else{
                asisTanceResult.setBtnRegisterDisabled(false);
                asisTanceResult.setMessageMov(nextMovToRegister.getMovType());
            }

            asisTanceResult.setIdMovNextRegister(nextMovToRegister.getId());
        }
    }

    private static void resetObjectAndListAsistance(CaRegAssistanceResponseMod asisTanceResult){

        asisTanceResult.setId(null);

        for(CaMoveResponseMod mov: asisTanceResult.getListAsistance()){
            mov.setPendienteRegistro(true);
            mov.setMovDate(Constants.PENDIENTE);
            mov.setMovHour(Constants.PENDIENTE);
            mov.setLatitud(null);
            mov.setLongitud(null);
            mov.setDateOffline(false);
        }

    }

    private static boolean isCurrentHourAfterTwelvePM(){

        String currentStringDate=UtilsDate.dateFormat("dd/MM/yyyy");
        currentStringDate=currentStringDate+" 12:00:00";
        Date dateCurrentTwelveHr=UtilsDate.toDate("dd/MM/yyyy HH:mm:ss",currentStringDate);
        Date currentDate=new Date();

        return UtilsDate.comparaFecha(currentDate,dateCurrentTwelveHr);
    }

    private  static boolean isCurrentHourBeforeTwelvePM(){

        String currentStringDate=UtilsDate.dateFormat("dd/MM/yyyy");
        currentStringDate=currentStringDate+" 12:00:00";
        Date dateCurrentTwelveHr=UtilsDate.toDate("dd/MM/yyyy HH:mm:ss",currentStringDate);
        Date currentDate=new Date();

        return UtilsDate.comparaFecha(dateCurrentTwelveHr,currentDate);

    }

    private static CaMoveResponseMod getDataLastMovRegister(RealmList<CaMoveResponseMod> listAsistance){

        int i;
        int lengthListAssistance=listAsistance.size();
        CaMoveResponseMod movLastRegister=null;
        for(i=(lengthListAssistance-1); i >= 0; i--){
            if(!listAsistance.get(i).getMovDate().equals(Constants.PENDIENTE) && !listAsistance.get(i).getMovDate().equals(Constants.SIN_REGISTRO)){
                movLastRegister=listAsistance.get(i);
                break;
            }
        }

        return movLastRegister;
    }

    private static CaMoveResponseMod netxMovRegistrar(RealmList<CaMoveResponseMod> listAsistance){

        int i;
        int lengthListAssistance=listAsistance.size();
        CaMoveResponseMod nexMovToRegister=null;

        for(i=0; i< lengthListAssistance; i++){
            if(listAsistance.get(i).getMovDate() == null || listAsistance.get(i).getMovDate().equals(Constants.PENDIENTE)){
                nexMovToRegister=listAsistance.get(i);
                listAsistance.get(i).setDateOffline(true);
                break;
            }
        }

        return nexMovToRegister;
    }

    //Actualiza la lista de asistencia de acuerdo al tipo de movimoento diurno o nocturno, y coloca las correspondientes etiquetas Sin Registro o Pendiente
    private static void updateAssistaceModelOfflie(CaRegAssistanceResponseMod result){

        for(CaMoveResponseMod movTurno : result.getListAsistance()){

            if(result.getTpTurno() == Constants.NIGHT_SHIFT && isCurrentHourAfterTwelvePM() && movTurno.getTpMov().equals(Constants.MOV_NIGHT)){

                if(UtilsDate.isDate(movTurno.getMovDate())){
                    movTurno.setMovHour(movTurno.getMovHour());
                    movTurno.setMovDate(movTurno.getMovDate());
                }else{
                    movTurno.setMovHour(Constants.PENDIENTE);
                    movTurno.setMovDate(Constants.PENDIENTE);
                }

            }else if(result.getTpTurno() == Constants.NIGHT_SHIFT && isCurrentHourBeforeTwelvePM() && movTurno.getTpMov().equals(Constants.MOV_NIGHT)){

                if(UtilsDate.isDate(movTurno.getMovDate())){
                    movTurno.setMovHour(movTurno.getMovHour());
                    movTurno.setMovDate(movTurno.getMovDate());
                }else{
                    movTurno.setMovHour(Constants.SIN_REGISTRO);
                    movTurno.setMovDate(Constants.SIN_REGISTRO);
                }

            }else if(result.getTpTurno() == Constants.NIGHT_SHIFT && isCurrentHourAfterTwelvePM() && movTurno.getTpMov().equals(Constants.DAY_SHIFT)){

                movTurno.setMovHour(Constants.PENDIENTE);
                movTurno.setMovDate(Constants.PENDIENTE);

            }else if(result.getTpTurno() == Constants.NIGHT_SHIFT && isCurrentHourBeforeTwelvePM() && movTurno.getTpMov().equals(Constants.DAY_SHIFT)){

                if(UtilsDate.isDate(movTurno.getMovDate())){
                    movTurno.setMovHour(movTurno.getMovHour());
                    movTurno.setMovDate(movTurno.getMovDate());
                }else{
                    movTurno.setMovHour(Constants.PENDIENTE);
                    movTurno.setMovDate(Constants.PENDIENTE);
                }
            }
        }

    }

    public static boolean isEquals(CaRegAssistanceResponseMod savedLocal,CaRegAssistanceResponseMod newObJCaRegAssistanceResponseMod){


        if(savedLocal == null && newObJCaRegAssistanceResponseMod != null || (savedLocal != null && newObJCaRegAssistanceResponseMod == null)){
            return false;
        }

        if(savedLocal != null && newObJCaRegAssistanceResponseMod != null){

            if ((savedLocal.getIdMovNextRegister() == null) ? (newObJCaRegAssistanceResponseMod.getIdMovNextRegister() != null) : !savedLocal.getIdMovNextRegister().equals(newObJCaRegAssistanceResponseMod.getIdMovNextRegister())) {
                return false;
            }

            if ((savedLocal.getTpTurno() == null) ? (newObJCaRegAssistanceResponseMod.getTpTurno() != null) : !savedLocal.getTpTurno().equals(newObJCaRegAssistanceResponseMod.getTpTurno())) {
                return false;
            }

            if ((savedLocal.getCdUser() == null) ? (newObJCaRegAssistanceResponseMod.getCdUser() != null) : !savedLocal.getCdUser().equals(newObJCaRegAssistanceResponseMod.getCdUser())) {
                return false;
            }

            if (savedLocal.getListAsistance() == null && newObJCaRegAssistanceResponseMod.getListAsistance() != null || (savedLocal.getListAsistance() == null && newObJCaRegAssistanceResponseMod.getListAsistance() != null)){
                return false;
            }

            if(savedLocal.getListAsistance() != null && newObJCaRegAssistanceResponseMod.getListAsistance() != null){

                if (savedLocal.getListAsistance().size() !=  newObJCaRegAssistanceResponseMod.getListAsistance().size()){
                    return false;
                }

                if(savedLocal.getListAsistance().size() ==  newObJCaRegAssistanceResponseMod.getListAsistance().size()){

                    boolean isEquals=false;

                    for(CaMoveResponseMod localOBJ: savedLocal.getListAsistance()){

                        for(CaMoveResponseMod newObj : newObJCaRegAssistanceResponseMod.getListAsistance()){

                            if(equalsMov(localOBJ,newObj)){
                                isEquals=true;
                            }else{
                                isEquals=false;
                                break;
                            }

                            if(!isEquals)
                                return false;
                        }
                    }

                }
            }

        }

        return true;
    }

    private static boolean equalsMov(CaMoveResponseMod movUno,CaMoveResponseMod movDos){

        if((movUno == null && movDos != null) || (movUno != null && movDos == null))
            return false;
        else if(movUno != null && movDos != null){

            if ((movUno.getId() == null) ? (movDos.getId() != null) : !movUno.getId().equals(movDos.getId())) {
                return false;
            }

            if ((movUno.getMovHour() == null) ? (movDos.getMovHour() != null) : !movUno.getMovHour().equals(movDos.getMovHour())) {
                return false;
            }

            if ((movUno.getTpMov() == null) ? (movDos.getTpMov() != null) : !movUno.getTpMov().equals(movDos.getTpMov())) {
                return false;
            }

            if ((movUno.getLatitud() == null) ? (movDos.getLatitud() != null) : !movUno.getLatitud().equals(movDos.getLatitud())) {
                return false;
            }

            if ((movUno.getLongitud() == null) ? (movDos.getLongitud() != null) : !movUno.getLongitud().equals(movDos.getLongitud())) {
                return false;
            }

            if ((movUno.getMovDate() == null) ? (movDos.getMovDate() != null) : !movUno.getMovDate().equals(movDos.getMovDate())) {
                return false;
            }

            if ((movUno.getMovType() == null) ? (movDos.getMovType() != null) : !movUno.getMovType().equals(movDos.getMovType())) {
                return false;
            }

            if ((movUno.getTurn() == null) ? (movDos.getTurn() != null) : !movUno.getTurn().equals(movDos.getTurn())) {
                return false;
            }

            //if (movUno.isDateOffline() != movDos.isDateOffline()) {
              //  return false;
            //}

            if(movUno.isDataMovileOn() != movDos.isDataMovileOn()){
                return false;
            }

            if(movUno.isWifiOn() != movDos.isWifiOn()){
                return false;
            }

        }

        return true;
    }
}