package com.teclo.controlasistenciamovil.ui.register;

import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;

public class CaPresenceRegisterContract {

    interface View{
        void showProgressBar(boolean show);

    }

    public interface Presenter{
        void getAsistance(boolean isOnline,CaPresenceRegisterContract.OnResponseGetAsistance listenerResponse);
        void savePresence(boolean isRemoteResponse,CaRegAssistanceResponseMod caRegAssistanceResponseMod);
        void getJustify(OnResponseGetJustify onResponseGetJustify);
    }

    public interface OnResponseGetAsistance{
        void onSuccessGetAsistance(CaRegAssistanceResponseMod caRegAssistanceResponseMod,boolean isDateRemote);
        void onErrorGetAsistane(String mensaje,boolean isDateRemote);
    }

    interface OnResponseGetJustify{
        void onSuccessGetJustify(CaJustificationResponseMod caJustificationResponseMod);
        void onErrorGetJustify(String mensaje);
    }

}
