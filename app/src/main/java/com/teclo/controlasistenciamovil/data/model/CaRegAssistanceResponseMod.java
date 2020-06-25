package com.teclo.controlasistenciamovil.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CaRegAssistanceResponseMod extends RealmObject{

    @PrimaryKey
    private Integer id;

    @SerializedName("listAsistencia")
    private RealmList<CaMoveResponseMod> listAsistance;

    @SerializedName("btnRegDeshabilitado")
    private boolean btnRegisterDisabled;

    @SerializedName("mensajeMovimiento")
    private String messageMov;

    @SerializedName("tpTurno")
    private Integer tpTurno;

    @SerializedName("idMovSiguiente")
    private Long idMovNextRegister;

    //datos obtenidos desde UI y GPS
    private Long idCat;

    private String descripcion;

    private double latitude;

    private double longitude;

    private boolean isDateRemote;

    private String cdUser;

    private String fhRegister;


    // Datos de bitacora de la forma en que se realiza el guardado del movimiento

    private Boolean isWifiOn;
    private Boolean isDataMovilOn;

    public CaRegAssistanceResponseMod(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RealmList<CaMoveResponseMod> getListAsistance() {
        return listAsistance;
    }

    public void setListAsistance(RealmList<CaMoveResponseMod> listAsistance) {
        this.listAsistance = listAsistance;
    }

    public boolean isBtnRegisterDisabled() {
        return btnRegisterDisabled;
    }

    public void setBtnRegisterDisabled(boolean btnRegisterDisabled) {
        this.btnRegisterDisabled = btnRegisterDisabled;
    }

    public String getMessageMov() {
        return messageMov;
    }

    public void setMessageMov(String messageMov) {
        this.messageMov = messageMov;
    }

    public Integer getTpTurno() {
        return tpTurno;
    }

    public void setTpTurno(Integer tpTurno) {
        this.tpTurno = tpTurno;
    }

    public Long getIdMovNextRegister() {
        return idMovNextRegister;
    }

    public void setIdMovNextRegister(Long idMovNextRegister) {
        this.idMovNextRegister = idMovNextRegister;
    }

    public Long getIdCat() {
        return idCat;
    }

    public void setIdCat(Long idCat) {
        this.idCat = idCat;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isDateRemote() {
        return isDateRemote;
    }

    public void setDateRemote(boolean dateRemote) {
        isDateRemote = dateRemote;
    }

    public Boolean getWifiOn() {
        return isWifiOn;
    }

    public void setWifiOn(Boolean wifiOn) {
        isWifiOn = wifiOn;
    }

    public Boolean getDataMovilOn() {
        return isDataMovilOn;
    }

    public void setDataMovilOn(Boolean dataMovilOn) {
        isDataMovilOn = dataMovilOn;
    }

    public String getCdUser() {
        return cdUser;
    }

    public void setCdUser(String cdUser) {
        this.cdUser = cdUser;
    }

    public String getFhRegister() {
        return fhRegister;
    }

    public void setFhRegister(String fhRegister) {
        this.fhRegister = fhRegister;
    }
}
