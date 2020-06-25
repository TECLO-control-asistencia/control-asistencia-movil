package com.teclo.controlasistenciamovil.ui.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseFragment;
import com.teclo.controlasistenciamovil.application.SicnApp;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements SettingsContract.View{

    public static final String TAG = SettingsFragment.class.getSimpleName();


    @BindView(R.id.card_view_log_out)
    CardView cardViewLogOut;

    @BindView(R.id.userName)
    TextView txtViewNameUser;

    @Inject
    AppSharePreferences sharedPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserResponseMod user=sharedPreferences.getDateUserLogin();
        txtViewNameUser.setText(user.getUserName() == null ? "Usuario en sesión" : user.getUserName());

        if(getActivity() instanceof MenuInferiorActivity)
            ((MenuInferiorActivity)getActivity()).chekcMenuBottomByIndex(R.id.navigation_settings);

    }

    @Override
    public void injectDependencies() {
        super.injectDependencies();
        DaggerSettingsComponent.builder()
                .oAuthComponent(SicnApp.getAppComponent().getOauthSessionManager().getOAuthComponent())
                .settingsModule(new SettingsModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.card_view_log_out)
    public void logOutAplication(){
        ((MenuInferiorActivity)getContext()).showLogOutDialog();
    }

    @Override
    public void showProgressBar(boolean show) {

    }
}
