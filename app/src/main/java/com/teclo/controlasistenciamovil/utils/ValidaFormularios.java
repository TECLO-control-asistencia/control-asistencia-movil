package com.teclo.controlasistenciamovil.utils;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

public class ValidaFormularios {



    private String messagsDefault="El campo ? es requerido";

    private boolean isFormValid=true;
    private boolean isMarketLeyendRed=true;

    public ValidaFormularios(){}
    public ValidaFormularios(boolean isMarketLeyendRed){
        this.isMarketLeyendRed=isMarketLeyendRed;
    }

    public boolean isFormValid() {
        return isFormValid;
    }

    public void validEditTexWithInputLayout(EditText editText, TextInputLayout textInputLayout, String nameCampo){

        String mnesaje=messagsDefault.replace("?",nameCampo);
        if(editText.getText().toString().isEmpty() && isMarketLeyendRed){
            this.isFormValid=false;

            toggleTextInputLayoutError(textInputLayout, mnesaje);

        }else{
            toggleTextInputLayoutError(textInputLayout, null);

        }
    }

    public void toggleTextInputLayoutError(TextInputLayout textInputLayout, String messajesError){
        if (messajesError == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            this.isFormValid=false;
            textInputLayout.setErrorEnabled(true);
        }

        textInputLayout.setError(messajesError);
    }

}
