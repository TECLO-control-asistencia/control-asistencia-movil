package com.teclo.controlasistenciamovil.webservices.response;

import com.google.gson.annotations.SerializedName;
import com.teclo.controlasistenciamovil.data.model.Status;

public class CaAsistanceSavedResponseOffline {

    @SerializedName("status")
    public Status status;

    @SerializedName("data")
    public Boolean data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }
}
