package com.teclo.controlasistenciamovil.commons;

/**
 * Created by omhack on 4/6/18.
 */

public class Constants {

    public static final String PUBLIC_SERVICE = "public";
    public static final String BASIC_SERVICE = "basic_auth";
    public static final String OAUTH_MS_SERVICE = "oauth_ms_service";
    public static final String PUBLIC_MS_SERVICE = "public_ms_service";

    public final static String DATA_SOURCE_LOCAL = "local_data_source";
    public final static String DATA_SOURCE_REMOTE = "remote_data_source";
    public final static String DATA_SOURCE_REPOSITORY = "repository_data_source";

    public static final String IS_SHOW_OUT_SESSION="is_shos_out_session";
    public static final String MENSAJE_ESTRA="MENSAJE_ESTRA";


    //Borrar para obtener al config
    public static final String BASE_URL =
            //"http://172.18.44.181:9080/sca_asistencia_wsw/";
    "http://ext.teclo.mx/sca_asistencia_wsw_qa/";//PRODUCCION

    public final static long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    public final static long FASTEST_INTERVAL = 5000; /* 2 sec */

    //Mensajes Permisos
    public static final String MSJ_PERMISSION_GPS="El sistema GPS debe estar activado presiona 'Aceptar' para continuar.";

    public final static int DAY_SHIFT=1;
    public final static int NIGHT_SHIFT=2;
    public final static Integer MOV_DAY=1;
    public final static Integer MOV_NIGHT=2;
    public final static String PENDIENTE="Pendiente";
    public final static String SIN_REGISTRO="Sin registro";
    public final static String MSG_FINAL_MOV_DAY="Ya se registraron todos los movimientos del día";
    public final static String MSG_FINAL_MOV_SHIFT="Ya se registraron todos los movimientos del turno";
    public final static String MSJ_FORM_INCOMPLET="Formulario incompleto";

    public static final String TXT_IMEI="IMEI";
    public static final String TXT_GPS="GPS";

    //CODIGOS DE RESPUESTA DE WS
    public static final String STATUS_SUCCESS_CAT_JUSTIFY="TCL20001";
    public static final String STATUS_ERROR_CAT_JUTIFY="TCL40401";

    public static final String STATUS_SUCCESS_SAVED_ASISTANCE="TCL20102";
    public static final String STATUS_ERROR_SAVED_ASISTANCE="TCL40902";
    public static final String STATUS_ERROR_SAVED_ASISTANCE_204="TCL20402";

    public static final String STATUS_SUCCESS_SAVED_ASISTANCE_OFFLINE="TCL20103";
    public static final String STATUS_ERROR_SAVED_ASISTANCE_OFFLINE="TCL40903";

    public static final String STATUS_SUCCESS_LIST_ASISTANCE="TCL20004";
    public static final String STATUS_ERROR_LIST_ASISTANCE="TCL40404";

    //MENAJES WS
    public static final String MSJ_SUCCESS="Resultado encontrado.";
    public static final String NOT_DATA_FOUND="No se encontraron resultados.";
    public static final String MSJ_CREATE="Registro almacenado correctamente.";
    public static final String MSJ_NOT_CREATE="El registro no se pudo almacenar.";


    //MEENSAJES CONEXION
    public static final String MSJ_SIN_CONEXION_INTERNET="No tiene acceso a internet";

    //Variables de AsyncTask
    public static final String ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_LIS ="ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_LIS";
    public static final String ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_REG ="ACTION_ASYNC_TASK_LIST_IS_RUNNIG_FR_REG";
    public static final String ACTION_ASYNC_TASK_SAVED_IS_RUNNI_FR_REG ="ACTION_ASYNC_TASK_SAVED_IS_RUNNI_FR_REG";
    public static final String ACTION_ASYNC_TASK_SAVED_OFFLINE_IS_RUNNIG ="ACTION_ASYNC_TASK_SAVED_OFFLINE_IS_RUNNIG";


    //Variables de Banderas dialog
    public static final String IS_SHOW_DIALOG_LOG_OUT ="IS_SHOW_DIALOG_LOG_OUT";
    public static final String IS_SHOW_DIALOG_PERMISSION="IS_SHOW_DIALOG_PERMISSION";

    public static final String MSJ_SESSION_FINISH="Tiempo de sesión agotado";

    public static final String SERVICE_NO_DIPONIBLE ="El servicio no esta disponible, inteté de nuevo";

}