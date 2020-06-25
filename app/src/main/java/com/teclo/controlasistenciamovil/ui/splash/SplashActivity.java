package com.teclo.controlasistenciamovil.ui.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;

import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseActivity;
import com.teclo.controlasistenciamovil.application.OAuthSessionManager;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.components.ApplicationComponent;
import com.teclo.controlasistenciamovil.ui.justificacion.CaJustificActivity;
import com.teclo.controlasistenciamovil.ui.login.LoginActivity;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.ValidTokenAuth;

import java.time.temporal.JulianFields;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

/**
 * Created by omhack on 4/7/18.
 */

public class SplashActivity extends BaseActivity implements
        SplashContract.View, SplashContract.LoadConfigCallBack{

    public static final String TAG = SplashActivity.class.getSimpleName();
    private static final int TIME_IN_SPLASH = 3000;

    @Inject
    SplashContract.Presenter presenter;

    @Inject
    AppSharePreferences sharedPreferences;

    OAuthSessionManager oAuthSessionManager;

    private AtomicInteger taskCounter;
    private AlertDialog errorDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(false);
        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrList(false);
        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrReg(false);
        sharedPreferences.saveIsAsyncTaskSaveAsistanceRunning(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskCounter = new AtomicInteger(2);
        presenter.loadNetworkConfig(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            //if(taskCounter.decrementAndGet() == 0){
                goToNext();
            //}
        }, TIME_IN_SPLASH);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    public void setUpComponent(ApplicationComponent appComponent) {
        appComponent.getSplashComponet(new SplashModule(this)).inject(this);
        oAuthSessionManager = appComponent.getOauthSessionManager();
        //oAuthSessionManager.createOAuthSession(sharedPreferences.getNetworkConfig().getBaseNonSecureURL(),"token");

    }

    @Override
    public void onError(String message) {
        removeDialog();
        errorDialog = new AlertDialog
                .Builder(new ContextThemeWrapper(this,R.style.SplashAlertDialog))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss()).show();
    }

    @Override
    public void onSuccess() {
        if(taskCounter.decrementAndGet() == 0){
            goToNext();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeDialog();
    }

    private void removeDialog(){
        if(errorDialog!=null && errorDialog.isShowing()){
            errorDialog.dismiss();
            errorDialog = null;
        }
    }

    private void goToNext(){
        oAuthSessionManager.createOAuthSession(Constants.BASE_URL,"token");

        boolean tokenValid=ValidTokenAuth.validTokenAtuh(sharedPreferences.getEncryptedToken());

        if (tokenValid) {
            Boolean isJustificPnedientSaved=sharedPreferences.isJustificacionPendiente();
            if(isJustificPnedientSaved){// si no se ha elegido justificacion al no ser celular asignado por el usuario
                Intent intent = new Intent(SplashActivity.this, CaJustificActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SplashActivity.this, MenuInferiorActivity.class);
                startActivity(intent);
                finish();
            }

        } else {
            sharedPreferences.savedJustificationPendiete(false);
            sharedPreferences.setEncryptedToken(null);
            sharedPreferences.saveDateUserLogin(null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }

}
