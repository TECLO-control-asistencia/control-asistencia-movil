package com.teclo.controlasistenciamovil.ui.menuinferior;

import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;

/**
 * @author DanielUitis
 */
public interface MenuInferiorContract {

    interface View{
        void showProgressBar(boolean show);
    }

    interface Presenter{
        void getMenuDinamico(Long idUser,
                             OnResponseGetMenuDinmico listener);



        void getListAllAasistanceOffline(OnResponeListAllAsistance listener);

        void saveDataOfflineInServerRemote(CaRegistroAsistanceVO caRegistroAsistanceVO,OnResponseSaveDataOfflineInServerRemote listener);

        void getAsistance(boolean isOnline,OnResponseGetAsistance listenerResponse);

    }

    interface OnResponseGetAsistance{
        void onSuccessGetAsistance(CaRegAssistanceResponseMod caRegAssistanceResponseMod, boolean isOnline);
        void onErrorGetAsistane(String message,boolean isOnline);
    }

    interface OnResponseSavePresence{
        void onSuccessSavePresence(CaRegAssistanceResponseMod caRegAssistanceResponseMod,boolean isDateRemote);
        void onErrorSavePresence(String mensaje);
    }

    interface  OnResponeListAllAsistance{
        void onResponseSuccessGetListAllAsistance(CaRegistroAsistanceVO caRegistroAsistanceVO);
        void onResponseErrorGetListAllAssistance(String message);
    }

    interface OnResponseSaveDataOfflineInServerRemote {
        void onSavedSuccess(Boolean isSaved);
        void onSavedErrorr(String message);
    }

    interface OnResponseGetMenuDinmico {
        void onGetMenuDinamicoValidationSuccess();
        void onGetMenuDinamicoValidationError(String message);
    }
}
