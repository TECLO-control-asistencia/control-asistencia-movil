package com.teclo.controlasistenciamovil.application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.dagger.components.ApplicationComponent;
import com.teclo.controlasistenciamovil.ui.login.LoginActivity;
import com.teclo.controlasistenciamovil.ui.splash.SplashActivity;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.IBackHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by omhack on 4/4/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        IBackHandler {

    private Unbinder unbinder;
    private AppSharePreferences sharedPrefs;
    OAuthSessionManager oAuthSessionManager;
    private BaseFragment baseFragment;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        injectDependencies();
        configureToolbar();
        configureToolbarHomeEnabled();
    }

    @Override
    protected void onStart() {
        super.onStart();
        oAuthSessionManager=SicnApp.getAppComponent().getOauthSessionManager();
        if (!SicnApp.getAppComponent().getOauthSessionManager().isAuthenticated()
                && !(this instanceof SplashActivity )
                && !SicnApp.IN_APP) {// si no hay sesion se activa se muestra el splia
            startActivity(new Intent(this, SplashActivity.class));
            SicnApp.IN_APP = true;
            finishAffinity();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        Log.d("Creepy","On destroy base activity");
    }

    @Override
    public void onBackPressed() {
        if (baseFragment == null || !baseFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void setFragment(BaseFragment fragment) {
        baseFragment = fragment;
    }

    private void injectDependencies() {
        setUpComponent(SicnApp.getAppComponent());
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    protected void changeFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    protected Fragment changeFragment(int containerViewId, Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, TAG);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        return fragment;
    }

    public void goToLogin(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    private void configureToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void configureToolbarHomeEnabled() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view,Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public abstract void setUpComponent(ApplicationComponent appComponent);
    protected abstract int getLayoutResource();
}