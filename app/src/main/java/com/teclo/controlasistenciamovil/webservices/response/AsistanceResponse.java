package com.teclo.controlasistenciamovil.webservices.response;

import com.google.gson.annotations.SerializedName;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.data.model.Status;

public class AsistanceResponse {

    @SerializedName("status")
    public Status status;
    @SerializedName("data")
    public CaRegAssistanceResponseMod data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CaRegAssistanceResponseMod getData() {
        return data;
    }

    public void setData(CaRegAssistanceResponseMod data) {
        this.data = data;
    }
}
