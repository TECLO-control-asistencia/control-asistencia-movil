package com.teclo.controlasistenciamovil.data.model;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("codigo")
    private String codigo;
    @SerializedName("descripcion")
    private String descripcion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
