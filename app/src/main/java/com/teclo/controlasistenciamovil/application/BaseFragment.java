package com.teclo.controlasistenciamovil.application;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.teclo.controlasistenciamovil.utils.IBackHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by omhack on 4/4/18.
 */

public abstract class BaseFragment extends Fragment {
    protected IBackHandler backHandler;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        if (getActivity() instanceof IBackHandler) {
            backHandler = (IBackHandler) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        backHandler.setFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbindViews();
    }

    private void bindViews(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
    }

    private void unbindViews() {
        unbinder.unbind();
    }

    public void injectDependencies() {
    }

    protected abstract int getFragmentLayout();

    protected boolean onBackPressed() {
        return false;
    }

    protected void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    protected  boolean isLockScreenOrientation(){
        return getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    }

    protected void unlockScreenOrientation() {
        if(getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public static void hideKeyboardFrom(Context context, View view) {

        if (view == null)
            view = new View(context);

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        view.clearFocus();
        /**
         * example
         * Dentro de una clase de fragmentos:
         *
         * view = getView().getRootView().getWindowToken();
         * Dado un fragmento fragmentcomo parámetro:
         *
         * view = fragment.getView().getRootView().getWindowToken();
         * A partir de su cuerpo de contenido:
         *
         * view = findViewById(android.R.id.content).getRootView().getWindowToken();
         */

    }


}
