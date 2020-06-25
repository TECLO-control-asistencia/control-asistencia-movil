package com.teclo.controlasistenciamovil.ui.listpresence;


import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.ui.register.CaPresenceRegisterContract;

public class CaListPresenceContract {

    interface View{
        void showProgressBar(boolean show);
    }

    public interface Presenter{
        void getAsistance(boolean isOnline,OnResponseGetAsistance listenerResponse);
        void savePresence(boolean isOnline,CaRegAssistanceResponseMod caRegAssistanceResponseMod);
    }

    interface OnResponseGetAsistance{
        void onSuccessGetAsistance(CaRegAssistanceResponseMod caRegAssistanceResponseMod, boolean isDateRemote);
        void onErrorGetAsistane(String mensaje,boolean isDateRemote);
    }

}