package com.teclo.controlasistenciamovil.webservices.request;

import com.teclo.controlasistenciamovil.data.model.CaRegistry;

import java.util.List;

public class CaRegistroAsistanceVO {


    private List<CaRegistry> listAisistance;

    public List<CaRegistry> getListAisistance() {
        return listAisistance;
    }

    public void setListAisistance(List<CaRegistry> listAisistance) {
        this.listAisistance = listAisistance;
    }
}
