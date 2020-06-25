package com.teclo.controlasistenciamovil.ui.justificacion;

import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;

import java.util.List;

/**
 * Created by DanielGC on 6/5/19.
 */
public interface CaJustificContract {
    interface View{
    }

    interface Presenter{
        void getCatJustific(OnResponseCatJustific listener);
        void saveJutificSelectedLocalRealm(CaJustificationResponseMod caJustificationResponseMod,OnResponseCatJustific litener);
    }

    interface OnResponseCatJustific {
        void onGetCatJustificValidationSuccess(List<CaJustificationResponseMod> listCatJustific);
        void onGetCatJustificValidationError(String message);

        void onSaveJustificSelectedSuccess(CaJustificationResponseMod caJustificationResponseMod);
        void onSaveJustificSelectedError(String message);
    }
}
