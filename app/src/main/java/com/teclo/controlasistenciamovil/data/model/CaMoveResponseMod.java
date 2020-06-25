package com.teclo.controlasistenciamovil.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CaMoveResponseMod extends RealmObject{

    @PrimaryKey
    @SerializedName("movimientoTipoId")
    private Long id;

    @SerializedName("movimientoTipo")
    private String movType;

    @SerializedName("tpMovimiento")
    private Integer tpMov;

    @SerializedName("movimientoFecha")
    private String movDate;

    @SerializedName("movimientoHora")
    private String movHour;

    @SerializedName("turno")
    private String turn;

    @SerializedName("longitud")
    private Double longitud;

    @SerializedName("latitud")
    private Double latitud;

    @SerializedName("isPendienteRegistro")
    private boolean isPendienteRegistro;
    private boolean isDateOffline;
    private boolean isWifiOn;
    private boolean isDataMovileOn;
    private String cdUser;

    public CaMoveResponseMod() {
    }

    public String getMovType() {
        return movType;
    }

    public void setMovType(String movType) {
        this.movType = movType;
    }

    public String getMovDate() {
        return movDate;
    }

    public void setMovDate(String movDate) {
        this.movDate = movDate;
    }

    public String getMovHour() {
        return movHour;
    }

    public void setMovHour(String movHour) {
        this.movHour = movHour;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTpMov() {
        return tpMov;
    }

    public void setTpMov(Integer tpMov) {
        this.tpMov = tpMov;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public boolean isPendienteRegistro() {
        return isPendienteRegistro;
    }

    public void setPendienteRegistro(boolean pendienteRegistro) {
        isPendienteRegistro = pendienteRegistro;
    }

    public boolean isDateOffline() {
        return isDateOffline;
    }

    public void setDateOffline(boolean dateOffline) {
        isDateOffline = dateOffline;
    }

    public boolean isWifiOn() {
        return isWifiOn;
    }

    public void setWifiOn(boolean wifiOn) {
        isWifiOn = wifiOn;
    }

    public boolean isDataMovileOn() {
        return isDataMovileOn;
    }

    public void setDataMovileOn(boolean dataMovileOn) {
        isDataMovileOn = dataMovileOn;
    }

    public String getCdUser() {
        return cdUser;
    }

    public void setCdUser(String cdUser) {
        this.cdUser = cdUser;
    }
}
