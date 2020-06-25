package com.teclo.controlasistenciamovil.ui.menuinferior;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseActivity;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.dagger.components.ApplicationComponent;
import com.teclo.controlasistenciamovil.utils.AppConectionManager;
import com.teclo.controlasistenciamovil.utils.AsyncTaskSaveDataOffline;
import com.teclo.controlasistenciamovil.utils.CaLoading;
import com.teclo.controlasistenciamovil.webservices.request.CaRegistroAsistanceVO;
import com.teclo.controlasistenciamovil.receiver.EventsReceiver;
import com.teclo.controlasistenciamovil.ui.listpresence.CaListPresenceFragment;
import com.teclo.controlasistenciamovil.ui.login.LoginActivity;
import com.teclo.controlasistenciamovil.ui.settings.SettingsFragment;
import com.teclo.controlasistenciamovil.ui.register.CaPresenceRegisterFragment;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.ValidTokenAuth;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import retrofit2.Retrofit;

/**
 * @author DanielUitis
 */
public class MenuInferiorActivity extends BaseActivity implements
        MenuInferiorContract.View,
        MenuInferiorContract.OnResponeListAllAsistance,
        AsyncTaskSaveDataOffline.AsynckTasckResponseSaveAsistanceOffline{


    public static final String TAG = MenuInferiorActivity.class.getSimpleName();

    private int containerId = R.id.container;

    private Dialog mDialogLogOut;
    Boolean isMdialogLogOutShow=false;

    @Inject
    MenuInferiorContract.Presenter presenter;

    @Inject
    @Named(Constants.OAUTH_MS_SERVICE)
    Retrofit retrofit;

    @Inject
    AppSharePreferences sharedPreferences;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private boolean onResum=false;
    private boolean onPause=false;

    private boolean isProsesSaveRemoteDateOffline=false;
    Boolean isRunningAsynckTasckOffline=false;
    private boolean isWifiOn;
    private boolean isDataMovileOn;
    Toolbar toolbar;

    private EventsReceiver eventsReceiver;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if(item.isChecked()){
                        return false;
                    }else{
                        selectedFragmentMenu(item.getItemId());
                        return true;
                    }
                }
            };

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_menu_inferior;
    }

    @Override
    public void setUpComponent(ApplicationComponent appComponent) {
        DaggerMenuInferiorComponent.builder()
                .oAuthComponent(appComponent.getOauthSessionManager().getOAuthComponent())
                .menuInferiorModule(new MenuInferiorModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // presenter.getAsistance(AppConectionManager.isOnline(this),this);

        eventsReceiver = new EventsReceiver(this);
        IntentFilter filter = new IntentFilter();
        registerReceiver(eventsReceiver , filter);

        configureToolbarHomeEnabled();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null)
            selectedFragmentMenu(navigation.getMenu().getItem(0).getItemId());

        if(savedInstanceState != null){
            isMdialogLogOutShow=savedInstanceState.getBoolean(Constants.IS_SHOW_DIALOG_LOG_OUT);
        }

        isRunningAsynckTasckOffline=sharedPreferences.getIsAsyncTaskSaveAsistanceOfflineRunning();

        if(isRunningAsynckTasckOffline == null || !isRunningAsynckTasckOffline){
            presenter.getListAllAasistanceOffline(this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        sharedPreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(this.isRunningAsynckTasckOffline);
        outState.putSerializable(Constants.IS_SHOW_DIALOG_LOG_OUT,this.isMdialogLogOutShow);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            isMdialogLogOutShow=savedInstanceState.getBoolean(Constants.IS_SHOW_DIALOG_LOG_OUT);
            if(isMdialogLogOutShow)
                showLogOutDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResum=true;
        if(onPause){

            if(eventsReceiver == null){
                eventsReceiver = new EventsReceiver(this);
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(eventsReceiver , filter);
            }

            onPause=false;
        }

        isRunningAsynckTasckOffline=sharedPreferences.getIsAsyncTaskSaveAsistanceOfflineRunning();

        if(isRunningAsynckTasckOffline == null || !isRunningAsynckTasckOffline){
            presenter.getListAllAasistanceOffline(this);
        }

        Fragment fragmentRegister = getSupportFragmentManager().findFragmentByTag(CaPresenceRegisterFragment.TAG);

        if (fragmentRegister != null && fragmentRegister.isVisible() &&
                fragmentRegister instanceof CaPresenceRegisterFragment && isRunningAsynckTasckOffline) {
          //  CaLoading.show(this,"Espere...",false);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        onResum=false;
        onPause=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharedPreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(false);
        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrList(false);
        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrReg(false);
        sharedPreferences.saveIsAsyncTaskSaveAsistanceRunning(false);

        try {
            if(eventsReceiver != null)
                unregisterReceiver(eventsReceiver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(isMdialogLogOutShow)
            mDialogLogOut.dismiss();
    }

    public void goToAsistanceRegister() {

        changeFragment(containerId, CaPresenceRegisterFragment.newInstance(),CaPresenceRegisterFragment.TAG);

        if(isRunningAsynckTasckOffline == null || !isRunningAsynckTasckOffline){
            presenter.getListAllAasistanceOffline(this);
        }
    }

    public void goToListAsistance() {

        changeFragment(containerId, CaListPresenceFragment.newInstance(),CaListPresenceFragment.TAG);

        if(isRunningAsynckTasckOffline == null || !isRunningAsynckTasckOffline){
            presenter.getListAllAasistanceOffline(this);
        }
    }

    public void goToSettings() {

        if(isRunningAsynckTasckOffline == null || !isRunningAsynckTasckOffline){
            presenter.getListAllAasistanceOffline(this);
        }

        changeFragment(containerId, SettingsFragment.newInstance(),SettingsFragment.TAG);
    }

    public void goToLoginAppShowDialog(String mensaje){

        if(eventsReceiver != null){
            try {
                unregisterReceiver(eventsReceiver);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            eventsReceiver=null;

        }

        Intent intent = new Intent(MenuInferiorActivity.this, LoginActivity.class);
        intent.putExtra(Constants.IS_SHOW_OUT_SESSION,true);
        intent.putExtra(Constants.MENSAJE_ESTRA,mensaje);
        startActivity(intent);
        finish();
        finish();
    }

    public void goToLogin(){

        if(eventsReceiver != null){
            try {
                unregisterReceiver(eventsReceiver);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            eventsReceiver=null;
        }

        Intent intent = new Intent(MenuInferiorActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        finish();
    }


    /**
     * Metodo para iniciar el menu inferior
     * @param idMenu
     */
    private void selectedFragmentMenu(int idMenu){

        getSupportActionBar().setTitle("");
        toolbar.setTitle("");
        switch (idMenu) {

            case R.id.nav_asistance_register:
                goToAsistanceRegister();
                break;
            case R.id.navigation_consulta:
                goToListAsistance();
                break;
            case R.id.navigation_settings:
                goToSettings();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if(navigation.getMenu().getItem(0).isChecked()) {
            showLogOutDialog();
        }else{
            super.onBackPressed();
        }
    }

    public void chekcMenuBottomByIndex(int idMenuItem){
        getSupportActionBar().setTitle("");
        toolbar.setTitle("");
        navigation.getMenu().findItem(idMenuItem).setChecked(true);
        toolbar.setTitle(navigation.getMenu().findItem(idMenuItem).getTitle());
    }


    /**
     * Prepara el menu superior
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Metodo para el menuSuperior
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.out_login:
                showLogOutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public void showAlertDialog(int title, String content){
        new AwesomeErrorDialog(this)
                .setDialogIconOnly(R.drawable.ic_alert_white)
                .setTitle(content)
                .setMessage("")
                .show();
    }

        @Override
    public void showProgressBar(boolean show) {

    }

    public void saveDataOffline(){
        presenter.getListAllAasistanceOffline(this);
    }

    @Override
    public void onResponseSuccessGetListAllAsistance(CaRegistroAsistanceVO caRegistroAsistanceVO) {

        if(caRegistroAsistanceVO != null){// si existen datos en local se procede a guardarlos en el servidor remoto
            Log.d("Teclo", "Se obtienen datos locales Offline para guardar en remoto");
            setProsesSaveRemoteDateOffline(true);
            new AsyncTaskSaveDataOffline(this,sharedPreferences,
                    this, retrofit,AppConectionManager.isOnline(this)).execute(caRegistroAsistanceVO);
        }
    }

    @Override
    public void onResponseErrorGetListAllAssistance(String message) {
        setProsesSaveRemoteDateOffline(false);
        Log.d("Teclo", "Error getData offline: "+message);
    }

    public boolean isProsesSaveRemoteDateOffline() {
        return isProsesSaveRemoteDateOffline;
    }

    public void setProsesSaveRemoteDateOffline(boolean prosesSaveRemoteDateOffline) {
        isProsesSaveRemoteDateOffline = prosesSaveRemoteDateOffline;
    }

    public boolean isWifiOn() {
        return isWifiOn;
    }

    public void setWifiOn(boolean wifiOn) {
        isWifiOn = wifiOn;
    }

    public boolean isDataMovileOn() {
        return isDataMovileOn;
    }

    public void setDataMovileOn(boolean dataMovileOn) {
        isDataMovileOn = dataMovileOn;
    }



    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "ESTAS EN LANDS CAPE", Toast.LENGTH_LONG).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "ESTAS EN portrait", Toast.LENGTH_LONG).show();
        }
    }*/


    protected void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    protected  boolean isLockScreenOrientation(){
        return this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    }

    protected void unlockScreenOrientation() {
        if(this != null)
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onTasckAsyncSaveAsistanceOfflineIsRuning(boolean isRunningAsynTack) {
        setProsesSaveRemoteDateOffline(isRunningAsynTack);
        this.isRunningAsynckTasckOffline=isRunningAsynTack;
    }



    @Override
    public void onSuccessResponAsyncTacsOffline(boolean isSaved, String mesj) {

    }

    @Override
    public void onErrorResponAsyncTacsOffline(boolean isSaved, String mesj) {

    }



    public void showLogOutDialog(){
        isMdialogLogOutShow=true;
        sharedPreferences=new  AppSharePreferences(getBaseContext());
        mDialogLogOut = new Dialog(this, R.style.dialog_no_border);
        mDialogLogOut.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater m_inflater = LayoutInflater.from(this);
        View m_view = m_inflater.inflate(R.layout.custom_dialog_confirm, null);
        LinearLayout m_llMain = m_view.findViewById(R.id.id_dialog_confirm);

        m_llMain.setBackgroundResource(R.drawable.style_custom_dialog);
        Button m_btnOk = m_view.findViewById(R.id.btnOk);
        Button m_btnCancel = m_view.findViewById(R.id.dbtnCancel);


        View.OnClickListener m_clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View p_v)
            {

                switch (p_v.getId()) {
                    case R.id.btnOk:
                        sharedPreferences.saveDateUserLogin(null);
                        sharedPreferences.setEncryptedToken(null);
                        sharedPreferences.saveImeiDevice(null);
                        sharedPreferences.deleteDataJustify();
                        sharedPreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(false);
                        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrList(false);
                        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrReg(false);
                        sharedPreferences.saveIsAsyncTaskSaveAsistanceRunning(false);
                        sharedPreferences.saveCatJustify(null);
                        //oAuthSessionManager.logout();
                        goToLogin();
                        isMdialogLogOutShow=false;
                        mDialogLogOut.dismiss();
                        break;
                    case R.id.dbtnCancel:
                        isMdialogLogOutShow=false;
                        mDialogLogOut.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        m_btnOk.setOnClickListener(m_clickListener);
        m_btnCancel.setOnClickListener(m_clickListener);

        mDialogLogOut.setContentView(m_view);
        mDialogLogOut.show();
    }



    public void showSnackBar(String msg,View rootView, int bottomMargin) {
        Snackbar snack;
        View snackView;
        TextView tv;

        try {
            snack = Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG);
            snackView = snack.getView();
            tv = snackView.findViewById(android.support.design.R.id.snackbar_text);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.base_white));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                tv.setTextColor(getResources().getColor(R.color.base_white));
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            snack.show();
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}