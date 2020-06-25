package com.teclo.controlasistenciamovil.ui.justificacion;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseActivity;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.components.ApplicationComponent;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.CaLoading;
import com.teclo.controlasistenciamovil.utils.CustomSpinnerAdapter;
import com.teclo.controlasistenciamovil.utils.ValidaFormularios;

import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class CaJustificActivity extends BaseActivity
        implements CaJustificContract.View,CaJustificContract.OnResponseCatJustific {

    public static final String TAG = CaJustificActivity.class.getSimpleName();

    @Inject
    CaJustificContract.Presenter presenter;

    @Inject
    AppSharePreferences sharedPreferences;


    @BindView(R.id.textLayoutObservacion)
    TextInputLayout textLayoutObservacion;

    @Nullable
    @BindView(R.id.observationEditTxt)
    TextInputEditText mEdit;

    @BindView(R.id.textLayoutSpinner)
    TextInputLayout textLayoutSpinner;

    @Nullable
    @BindView(R.id.spinnerEditTxt)
    TextView spinnerMEdit;

    @Nullable
    @BindView(R.id.spinnerJustify)
    Spinner spinnerJustify;

    List<String> listCatJustificString;
    List<CaJustificationResponseMod> listCatJustific;

    CaJustificationResponseMod caJustificationResponseModSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CaLoading.show(CaJustificActivity.this,getString(R.string.load_message),false);
        presenter.getCatJustific(this);
        sharedPreferences.savedJustificationPendiete(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CaLoading.hide();
    }

    @Override
    public void setUpComponent(ApplicationComponent appComponent) {
        if(appComponent.getOauthSessionManager().getOAuthComponent() != null){
            Log.d("Creepy","appComponent.getOauthSessionManager().getOAuthComponent() not null");
        }

        DaggerCaJustificComponent.builder()
                .oAuthComponent(appComponent.getOauthSessionManager().getOAuthComponent())
                .caJustificModule(new CaJustificModule(this))
                .build()
                .inject(this);
    }

    protected View getLayoutRootView(){
        //View view=getCurrentFocus() == null ? this.findViewById(R.id.container_excuse) : getCurrentFocus();
        View view= this.findViewById(R.id.container_excuse);
        return view;
    }

    @OnClick(R.id.fab)
    public void onSaveJutific(){

        if(!isValidFormulario()){
            showSnackBar(Constants.MSJ_FORM_INCOMPLET,getLayoutRootView(),0);
            return;
        }

        CaLoading.show(CaJustificActivity.this,getString(R.string.load_message),false);
        caJustificationResponseModSelected.setDescription(mEdit.getText().toString());
        presenter.saveJutificSelectedLocalRealm(caJustificationResponseModSelected,this);

    }

    private boolean isValidFormulario(){

        ValidaFormularios validaFormularios=new ValidaFormularios();

        if(caJustificationResponseModSelected == null)
            validaFormularios.toggleTextInputLayoutError(textLayoutSpinner, getString(R.string.requirement_data_controls));
        else
            validaFormularios.toggleTextInputLayoutError(textLayoutSpinner, null);


        validaFormularios.validEditTexWithInputLayout(mEdit,textLayoutObservacion,"Descripción");

        return validaFormularios.isFormValid();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ca_justific;
    }

    @Override
    public void onGetCatJustificValidationSuccess(List<CaJustificationResponseMod> listCatJustific) {

        if(listCatJustific != null && !listCatJustific.isEmpty()){
            this.listCatJustific= new ArrayList<>();
            this.listCatJustificString =new ArrayList<>();
            this.listCatJustific.addAll(listCatJustific);
            sharedPreferences.saveCatJustify(listCatJustific);
        }

        for(CaJustificationResponseMod just: listCatJustific){
            this.listCatJustificString.add(just.getDescription());
        }

        if (!listCatJustific.isEmpty() && spinnerJustify !=null) {
            setSpinnerText(spinnerJustify,this.listCatJustificString,this.listCatJustificString.get(0));
        }

        CaLoading.hide();
    }


    private void setSpinnerText(Spinner spinner, List<String> listItems, String optionSeleccione) {
        ArrayList optionsListSelect=new ArrayList<>();

        for(String comboVO: listItems){
            optionsListSelect.add(comboVO);
        }

        if (!optionsListSelect.isEmpty()) {
            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(optionsListSelect, getApplicationContext());
            spinner.setAdapter(customSpinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String itemSelected = parent.getItemAtPosition(position).toString();
                    if(itemSelected != null && !itemSelected.equals("-Seleccione-")){
                        caJustificationResponseModSelected =listCatJustific.get(position);
                    }else if(itemSelected.equals("-Seleccione-")){
                        caJustificationResponseModSelected=null;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.i("Teclo", "Nothing selected \n");
                }
            });
            spinner.setSelection(customSpinnerAdapter.getIndexByString(optionSeleccione));
        }
    }

    @Override
    public void onGetCatJustificValidationError(String message) {
        CaLoading.hide();
        Log.i("Teclo", message);

        listCatJustific=sharedPreferences.getCatJustify();

        if(listCatJustificString == null){
            listCatJustificString=new ArrayList<>();
        }

        if(listCatJustific != null){
            for(CaJustificationResponseMod just: listCatJustific){
                this.listCatJustificString.add(just.getDescription());
            }

            if (!listCatJustific.isEmpty() && spinnerJustify !=null) {
                setSpinnerText(spinnerJustify,this.listCatJustificString,this.listCatJustificString.get(0));
            }
        }
    }

    @Override
    public void onSaveJustificSelectedSuccess(CaJustificationResponseMod caJustificationResponseMod) {
        Log.i("Teclo", "Se guarda en realm la justificación seleccionada: "+caJustificationResponseMod.getDescription());

        sharedPreferences.savedJustificationPendiete(null);
        startActivity(new Intent(this,MenuInferiorActivity.class));
        finishAffinity();
        CaLoading.hide();
    }

    @Override
    public void onSaveJustificSelectedError(String message) {
        Log.i("Teclo", message);
        CaLoading.hide();
    }

    public void showSnackBar(String msg,View rootView, int bottomMargin) {
        Snackbar snack;
        View snackView;
        TextView tv;

        try {
            snack = Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG);
            snackView = snack.getView();
            tv = snackView.findViewById(android.support.design.R.id.snackbar_text);

            if (Build.VERSION.SDK_INT >= 23) {
                tv.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.base_white));
            } else {
                tv.setTextColor(getResources().getColor(R.color.base_white));
            }
            snack.show();
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}