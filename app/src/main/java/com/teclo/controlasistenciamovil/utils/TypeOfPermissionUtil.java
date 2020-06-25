package com.teclo.controlasistenciamovil.utils;

import java.io.Serializable;

public class TypeOfPermissionUtil implements Serializable {

    private String namePermission;
    private String tpPermission;
    private Boolean isGranted;

    public TypeOfPermissionUtil(){}

    public TypeOfPermissionUtil(String namePermission, String tpPermission, Boolean isGranted) {
        this.namePermission = namePermission;
        this.tpPermission = tpPermission;
        this.isGranted = isGranted;
    }

    public String getNamePermission() {
        return namePermission;
    }

    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission;
    }

    public String getTpPermission() {
        return tpPermission;
    }

    public void setTpPermission(String tpPermission) {
        this.tpPermission = tpPermission;
    }

    public Boolean getGranted() {
        return isGranted;
    }

    public void setGranted(Boolean granted) {
        isGranted = granted;
    }
}
