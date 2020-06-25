package com.teclo.controlasistenciamovil.utils;

import android.content.Context;

import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaMoveResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AppRealmManager {

    public final static String TAG = AppRealmManager.class.getSimpleName();
    private Realm realm = null;

    public AppRealmManager(Context context){
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return realm;
    }

    public void saveOrUpdateUserModel(final UserResponseMod userModel) {
        realm.beginTransaction();
        realm.insertOrUpdate(userModel);
        realm.commitTransaction();
    }


    public UserResponseMod getUserModel(){
        realm.beginTransaction();
        UserResponseMod user =realm.where(UserResponseMod.class).findFirst();
        realm.commitTransaction();
        return user;

    }

    /*Justificacion*/
    public boolean saveJustificationModel(final CaJustificationResponseMod justifyModel) {

        try{

            CaJustificationResponseMod newjustify;

            realm.beginTransaction();

            Number currentIdNum = realm.where(CaJustificationResponseMod.class).max("id");
            Long nextId;
            if(currentIdNum == null) {
                nextId = 1L;
            } else {
                nextId = currentIdNum.longValue() + 1L;
            }

            if(justifyModel.getId() == null)
                justifyModel.setId(nextId);

            newjustify=realm.copyToRealmOrUpdate(justifyModel); // Persist unmanaged objects
            realm.commitTransaction();

            return true;
        }catch(Exception e){
            return false;
        }
    }

    public CaJustificationResponseMod getJustificationModel() {
        RealmResults<CaJustificationResponseMod> justification = realm.where(CaJustificationResponseMod.class).findAll();
        realm.commitTransaction();
        if(justification != null && justification.size() > 0)
            return justification.first();
        else
            return null;
    }

    public RealmResults<CaJustificationResponseMod> getListJustificationModel() {
        RealmResults<CaJustificationResponseMod> justification = realm.where(CaJustificationResponseMod.class).findAll();
        realm.commitTransaction();
        if(justification != null && justification.size() > 0)
            return justification;
        else
            return null;
    }



    public void removeCaJustificacionMod(){//se borra catalogos
        final RealmResults<CaJustificationResponseMod> model = realm.where(CaJustificationResponseMod.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteAllFromRealm();
            }
        });
        realm.commitTransaction();
    }

    public void removeCaJustificacionModFirst(){
        final RealmResults<CaJustificationResponseMod> model = realm.where(CaJustificationResponseMod.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFirstFromRealm();
            }
        });
        realm.commitTransaction();
    }

    /**Resgistro de Asistencia**/

    public CaRegAssistanceResponseMod saveAssistanceModel(final CaRegAssistanceResponseMod model) {

        CaRegAssistanceResponseMod newModel;

        realm.beginTransaction();

        Number currentIdNum = realm.where(CaRegAssistanceResponseMod.class).max("id");
        Integer nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }

        if(model.getId() == null)
            model.setId(nextId);

        newModel=getNewObjectAsistanceLocal(realm.copyToRealmOrUpdate(model)); // Persist unmanaged objects
        realm.commitTransaction();

        return newModel;
    }

    public CaRegAssistanceResponseMod getAsistane(){
        RealmResults<CaRegAssistanceResponseMod> newModel;
        newModel=realm.where(CaRegAssistanceResponseMod.class).findAll();

        realm.commitTransaction();

        if(newModel!= null && !newModel.isEmpty())
            return getNewObjectAsistanceLocal(newModel.last());
        else
            return null;
    }

    public CaRegAssistanceResponseMod getNewObjectAsistanceLocal(CaRegAssistanceResponseMod caRegAssistanceResponseMod){

        CaRegAssistanceResponseMod caRegAssistanceResponseNew=new CaRegAssistanceResponseMod();
        caRegAssistanceResponseNew.setDateRemote(caRegAssistanceResponseMod.isDateRemote());
        caRegAssistanceResponseNew.setIdCat(caRegAssistanceResponseMod.getIdCat());
        caRegAssistanceResponseNew.setId(caRegAssistanceResponseMod.getId());
        caRegAssistanceResponseNew.setDescripcion(caRegAssistanceResponseMod.getDescripcion());
        caRegAssistanceResponseNew.setLongitude(caRegAssistanceResponseMod.getLongitude());
        caRegAssistanceResponseNew.setLatitude(caRegAssistanceResponseMod.getLatitude());
        caRegAssistanceResponseNew.setBtnRegisterDisabled(caRegAssistanceResponseMod.isBtnRegisterDisabled());
        caRegAssistanceResponseNew.setIdMovNextRegister(caRegAssistanceResponseMod.getIdMovNextRegister());
        caRegAssistanceResponseNew.setMessageMov(caRegAssistanceResponseMod.getMessageMov());
        caRegAssistanceResponseNew.setTpTurno(caRegAssistanceResponseMod.getTpTurno());

        RealmList<CaMoveResponseMod> listAsistanceNew = new RealmList<>();
        CaMoveResponseMod movNew;

        for(CaMoveResponseMod movMod:caRegAssistanceResponseMod.getListAsistance()){
            movNew= new CaMoveResponseMod();
            movNew.setLongitud(movMod.getLongitud());
            movNew.setLatitud(movMod.getLatitud());
            movNew.setId(movMod.getId());
            movNew.setMovDate(movMod.getMovDate());
            movNew.setMovHour(movMod.getMovHour());
            movNew.setMovType(movMod.getMovType());
            movNew.setPendienteRegistro(movMod.isPendienteRegistro());
            movNew.setTpMov(movMod.getTpMov());
            movNew.setTurn(movMod.getTurn());
            listAsistanceNew.add(movNew);
        }

        caRegAssistanceResponseNew.setListAsistance(listAsistanceNew);

        return caRegAssistanceResponseNew;
    }

    public void deletAsistance(CaRegAssistanceResponseMod model){
        realm.beginTransaction();
        RealmResults<CaRegAssistanceResponseMod> result=
                realm.where(CaRegAssistanceResponseMod.class).equalTo("id",model.getId()).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

}
