package com.teclo.controlasistenciamovil.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.teclo.controlasistenciamovil.application.SicnApp;

/**
 * Created by omhack on 4/8/18.
 */

public class SicnAppLifecycleObserver implements LifecycleObserver {

    private AppSharePreferences sharePrefs;
    private AppRealmManager appRealmManager;

    public SicnAppLifecycleObserver(AppSharePreferences sharePrefs,AppRealmManager appRealmManager){
        this.sharePrefs = sharePrefs;
        this.appRealmManager=appRealmManager;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppStop() {
       /* if(sharePrefs.getUserStatus() == User.STATUS_ACTIVE) {
            sharePrefs.setUserStatus(User.STATUS_LOGGED_OUT);
            sharePrefs.setProfileLoaded(false);
            SicnApp.getAppComponent().getOauthSessionManager().logout();
        }*/
        SicnApp.IN_APP = false;
    }
}
