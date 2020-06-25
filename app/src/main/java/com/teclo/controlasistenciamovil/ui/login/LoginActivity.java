package com.teclo.controlasistenciamovil.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseActivity;
import com.teclo.controlasistenciamovil.application.OAuthSessionManager;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.components.ApplicationComponent;
import com.teclo.controlasistenciamovil.ui.justificacion.CaJustificActivity;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppConectionManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.CaLoading;
import com.teclo.controlasistenciamovil.utils.CaPermissionsDialogFrag;
import com.teclo.controlasistenciamovil.utils.TypeOfPermissionUtil;
import com.teclo.controlasistenciamovil.webservices.request.UserMod;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
        implements LoginContract.View,LoginContract.OnResponseLoginUser,
        CaPermissionsDialogFrag.PermissionsGrantedCallback{

    public static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    LoginContract.Presenter presenter;

    @Inject
    AppSharePreferences sharedPreferences;

    OAuthSessionManager oAuthSessionManager;


    @BindView(R.id.input_password)
    EditText mPassword;

    @BindView(R.id.input_user)
    EditText mUser;

    @BindView(R.id.txt_user)
    TextInputLayout mTextUser;

    @BindView(R.id.txt_password)
    TextInputLayout mTextPassword;

    private KProgressHUD hud;

    private boolean onPause;
    private boolean onResumen;

    List<TypeOfPermissionUtil> listTipePermissionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emptyStackBack();//SE LIMPIA LA PILA DE BACK, ESTO PARA NO PERMITIR IR HACIA ATRAS CUANDO SE ESTE EN ESTA ACTIVIDAD
        if (getIntent().getExtras() != null) {
            Boolean isShowOutDialog= getIntent().getExtras().getBoolean(Constants.IS_SHOW_OUT_SESSION);
            String msj=getIntent().getExtras().getString(Constants.MENSAJE_ESTRA);
            if(isShowOutDialog != null && isShowOutDialog){
                showAlertDialog(0,"",msj);
            }

            sharedPreferences.getAsistaneAllSavedOffline();
            getIntent().removeExtra(Constants.IS_SHOW_OUT_SESSION);
            getIntent().removeExtra(Constants.MENSAJE_ESTRA);

        }

        listTipePermissionUtil=new ArrayList<>();
        listTipePermissionUtil.add(new TypeOfPermissionUtil(Constants.TXT_IMEI,Manifest.permission.READ_PHONE_STATE,null));

        if(!getPermissionImei(listTipePermissionUtil) && !onResumen){
            callPermission();
        }else{
            sharedPreferences.saveImeiDevice(getDeviceImei(getApplicationContext()));
        }
    }

    public void emptyStackBack(){// SE VALIDA
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(onPause){
            if(getPermissionImei(listTipePermissionUtil)){
                sharedPreferences.saveImeiDevice(getDeviceImei(getApplicationContext()));
            }
        }

        onResumen=true;
        onPause=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        onPause=true;
        onResumen=false;
    }

    @Override
    public void setUpComponent(ApplicationComponent appComponent) {

        if(appComponent.getOauthSessionManager().getOAuthComponent() != null){
            Log.d("Creepy","appComponent.getOauthSessionManager().getOAuthComponent() not null");
        }

        DaggerLoginComponent.builder()
                .oAuthComponent(appComponent.getOauthSessionManager().getOAuthComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
        oAuthSessionManager = appComponent.getOauthSessionManager();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    protected View getLayoutRootView(){
        View view=getCurrentFocus() == null ? this.findViewById(R.id.containerLoginForm) : getCurrentFocus();
        return view;
    }

    @OnClick(R.id.btn_login)
    public void login(){

        hideKeyboard(this);

        CaLoading.show(LoginActivity.this,getString(R.string.login_dialog_message),false);

        if(!AppConectionManager.isOnline(getApplicationContext())){
            CaLoading.hide();

            showSnackBar(Constants.MSJ_SIN_CONEXION_INTERNET, getLayoutRootView(), 0);
            return;
        }

        if(!getPermissionImei(listTipePermissionUtil)){
            CaLoading.hide();
            callPermission();
            return;
        }

        if(!validate()){
            CaLoading.hide();
            showSnackBar(Constants.MSJ_FORM_INCOMPLET, getLayoutRootView(), 0);
            return;
        }

        String user = mUser.getText().toString();
        String password = mPassword.getText().toString();

        UserMod userObj = new UserMod(user, password,"");
        presenter.starLogin(userObj,this);

    }

    private boolean validate(){
        boolean valid = true;

        String user = mUser.getText().toString();
        String password = mPassword.getText().toString();

        if(user.isEmpty()){
            toggleTextInputLayoutError(mTextUser, getString(R.string.requirement_data_controls));
            valid = false;
        }else{
            toggleTextInputLayoutError(mTextUser, null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            toggleTextInputLayoutError(mTextPassword, getString(R.string.password_invalid));
            valid = false;
        } else {
            toggleTextInputLayoutError(mTextPassword, null);
        }

        return valid;
    }

    private void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout, String msg) {
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
        textInputLayout.setError(msg);
    }

    public void showAlertDialog(int title, String content,String mensaje){// cambiar por mensaje de show output
        new AwesomeErrorDialog(this)
                .setDialogIconOnly(R.drawable.ic_alert_white)
                .setTitle(content)
                .setMessage(mensaje)
                .show();
    }

    public void showCustomProgressBar(){

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.wine))
                .setAnimationSpeed(2);
        hud.show();

    }

    public void hideCustomProgressBar(){
        if(hud != null && hud.isShowing())
            hud.dismiss();
    }

    private Boolean getPermissionImei(List<TypeOfPermissionUtil> listTipePermissionUtil){// se queda

        boolean isGranted=true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {
                if (checkSelfPermission(typeOfPermissionUtil.getTpPermission()) != PackageManager.PERMISSION_GRANTED) {
                    typeOfPermissionUtil.setGranted(false);
                    isGranted= false;
                }else {
                    typeOfPermissionUtil.setGranted(true);
                }
            }
        }else{
            for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {

                typeOfPermissionUtil.setGranted(true);

            }

            isGranted= true;
        }

        return isGranted;
    }

    public List<String> getDeviceImei(Context ctx) {

        List<String> listImei=new ArrayList<>();
        int index=0;
        int i=0;
        TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        Boolean permissionCheck = (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                ? true
                : (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);

        if (!permissionCheck){
            callPermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (manager.getPhoneCount() > 1) {
                for (i = 0; i < manager.getPhoneCount(); i++) {
                    if (manager.hasIccCard())
                        listImei.add(manager.getImei(i));
                }

            } else if (manager.getPhoneCount() == 1) {
                listImei.add(manager.getImei());
            }

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (manager.getDeviceId() != null) {
                listImei.add(manager.getDeviceId());
            }
        }

        return listImei;
    }

    @Override
    public void onSaveLoginValidationSuccess(UserResponseMod userResponseMod) {
        CaLoading.hide();

        sharedPreferences.getAsistaneAllSavedOffline();// se valida si existen datos antes de iniciar la session de otro o el mismo usuario

        oAuthSessionManager.createOAuthSession(Constants.BASE_URL,userResponseMod.getToken());

        JWT jwt = new JWT(userResponseMod.getToken());
        userResponseMod.setUserName(jwt.getClaim("name").asString());
        userResponseMod.setPoliceBadge(jwt.getClaim("sub").asString());

        sharedPreferences.saveDateUserLogin(userResponseMod);
        sharedPreferences.setEncryptedToken(userResponseMod.getToken());

        List<String> listImei=sharedPreferences.getImeiDevice();
        Boolean imeiIgual=false;

        for(String imei : listImei){
            imeiIgual=imei.equals(userResponseMod.getImei());
            if(imeiIgual)
                break;
        }

        if(imeiIgual){
            startActivity(new Intent(this,MenuInferiorActivity.class));
            finishAffinity();
        }else{
            startActivity(new Intent(this,CaJustificActivity.class));
            finishAffinity();
        }
    }

    @Override
    public void onSaveLoginValidationError(String message) {

        CaLoading.hide();

        showSnackBar(message, getLayoutRootView(), 0);
    }

    @Override
    public void callPermission() {

        if(!getPermissionImei(listTipePermissionUtil)){
            CaPermissionsDialogFrag.newInstance(listTipePermissionUtil).show(this.getSupportFragmentManager(), CaPermissionsDialogFrag.TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
