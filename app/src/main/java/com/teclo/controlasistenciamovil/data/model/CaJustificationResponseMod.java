package com.teclo.controlasistenciamovil.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CaJustificationResponseMod extends RealmObject {

    public CaJustificationResponseMod(){}

    @PrimaryKey
    private Long id;

    @SerializedName("idJustificacion")
    private Long idJustify;

    @SerializedName("codigoJustificacion")
    private String codeJustify;

    @SerializedName("descripcion")
    private String description;

    public CaJustificationResponseMod(Long idJustify, String description) {
        this.idJustify = idJustify;
        this.description = description;
    }

    public Long getIdJustify() {
        return idJustify;
    }

    public void setIdJustify(Long idJustify) {
        this.idJustify = idJustify;
    }

    public String getCodeJustify() {
        return codeJustify;
    }

    public void setCodeJustify(String codeJustify) {
        this.codeJustify = codeJustify;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
