package com.teclo.controlasistenciamovil.webservices.response;

import com.google.gson.annotations.SerializedName;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.Status;

import java.util.List;

public class CaCatJustifyResponse {

    @SerializedName("status")
    public Status status;
    @SerializedName("data")
    public List<CaJustificationResponseMod> data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<CaJustificationResponseMod> getData() {
        return data;
    }

    public void setData(List<CaJustificationResponseMod> data) {
        this.data = data;
    }
}
