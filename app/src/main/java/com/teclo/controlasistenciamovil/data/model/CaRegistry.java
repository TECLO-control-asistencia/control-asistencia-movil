package com.teclo.controlasistenciamovil.data.model;

public class CaRegistry {

    private Long idMov;

    private Double latitud;

    private Double longitud;

    private String dateRegister;

    private String cdUser;

    private Boolean isWifiOn;
    private Boolean isDataMovileOn;

    private Long idCat;
    private String descripcion;

    public CaRegistry(){}

    public Long getIdMov() {
        return idMov;
    }

    public void setIdMov(Long idMov) {
        this.idMov = idMov;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }

    public String getCdUser() {
        return cdUser;
    }

    public void setCdUser(String cdUser) {
        this.cdUser = cdUser;
    }

    public Boolean getWifiOn() {
        return isWifiOn;
    }

    public void setWifiOn(Boolean wifiOn) {
        isWifiOn = wifiOn;
    }

    public Boolean getDataMovileOn() {
        return isDataMovileOn;
    }

    public void setDataMovileOn(Boolean dataMovileOn) {
        isDataMovileOn = dataMovileOn;
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
}
