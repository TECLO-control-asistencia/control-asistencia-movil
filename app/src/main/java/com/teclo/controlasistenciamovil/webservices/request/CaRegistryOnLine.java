package com.teclo.controlasistenciamovil.webservices.request;

public class CaRegistryOnLine {

    private Long idCat;
    private String descripcion;
    private Long idMovimiento;
    private Double latitud;
    private Double longitud;
    private Boolean isWifiOn;
    private Boolean isDataMovileOn;

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

    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
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
}
