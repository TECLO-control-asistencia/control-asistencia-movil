package com.teclo.controlasistenciamovil.webservices.response;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserResponseMod extends RealmObject {
    @PrimaryKey
    private Long idUser;

    @SerializedName("idPerfil")
    private Long idProfile;

    @SerializedName("username")
    private String userName;

    @SerializedName("token")
    private String token;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("isActive")
    private String isActive;

    @SerializedName("imei")
    private String imei;

    private String policeBadge;

    private String cdRespuesta;
    private String msjRespuesta;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPoliceBadge() {
        return policeBadge;
    }

    public void setPoliceBadge(String policeBadge) {
        this.policeBadge = policeBadge;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getCdRespuesta() {
        return cdRespuesta;
    }

    public void setCdRespuesta(String cdRespuesta) {
        this.cdRespuesta = cdRespuesta;
    }

    public String getMsjRespuesta() {
        return msjRespuesta;
    }

    public void setMsjRespuesta(String msjRespuesta) {
        this.msjRespuesta = msjRespuesta;
    }
}
