package com.teclo.controlasistenciamovil.ui.listpresence;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseFragment;
import com.teclo.controlasistenciamovil.application.SicnApp;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaMoveResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppConectionManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.AsyncTaskGetListAsistanceFrList;
import com.teclo.controlasistenciamovil.utils.CaLoading;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import io.realm.RealmList;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaListPresenceFragment extends BaseFragment implements CaListPresenceContract.View,
        View.OnClickListener,CaListPresenceContract.OnResponseGetAsistance,
        AsyncTaskGetListAsistanceFrList.AsynckTasckResponseListAsistanceFrList{

    public static final String TAG = CaListPresenceFragment.class.getSimpleName();


    @BindView(R.id.tvTurn)
    TextView textViewTurn;

    @BindView(R.id.table)
    TableLayout tl;

    @Inject
    CaListPresenceContract.Presenter presenter;

    @Inject
    @Named(Constants.OAUTH_MS_SERVICE)
    Retrofit retrofit;

    @Inject
    AppSharePreferences sharedPreferences;

    String mensajeSinAcceso="";

    boolean isViewCreated=false;

    Boolean isRunningAsynTack=false;

    public CaListPresenceFragment() {
    }

    public static CaListPresenceFragment newInstance() {
        CaListPresenceFragment fragment = new CaListPresenceFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_list_presence;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setRetainInstance(true);

        CaLoading.show(getContext(),getString(R.string.load_message),false);

        isRunningAsynTack=sharedPreferences.getIsAsyncTaskListAsistanceRunningFrList();

        if(!isRunningAsynTack)
            new AsyncTaskGetListAsistanceFrList(this,presenter,sharedPreferences,getContext(),retrofit,AppConectionManager.isOnline(getContext())).execute();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrList(isRunningAsynTack);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        CaLoading.show(getContext(),getString(R.string.load_message),false);
        isRunningAsynTack=sharedPreferences.getIsAsyncTaskListAsistanceRunningFrList();

        if(isRunningAsynTack == null || !isRunningAsynTack){
            new AsyncTaskGetListAsistanceFrList(this,presenter,sharedPreferences,getContext(),retrofit,AppConectionManager.isOnline(getContext())).execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CaLoading.hide();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() instanceof MenuInferiorActivity)
            ((MenuInferiorActivity)getActivity()).chekcMenuBottomByIndex(R.id.navigation_consulta);
        addHeaders();
    }

    @Override
    public void injectDependencies() {
        super.injectDependencies();
        DaggerCaListPresenceComponent.builder()
                .oAuthComponent(SicnApp.getAppComponent().getOauthSessionManager().getOAuthComponent())
                .caListPresenceModule(new CaListPresenceModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void showProgressBar(boolean show) {

    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 0, 1, 1);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(10, 10, 10, 10);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {
        if (tl != null) {
            tl.removeAllViews();
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(0, "Movimiento", Color.WHITE, Typeface.BOLD, Color.parseColor("#d50000")));
            tr.addView(getTextView(0, "Fecha Registro", Color.WHITE, Typeface.BOLD, Color.parseColor("#d50000")));
            tl.addView(tr, getTblLayoutParams());
        }
    }

    /**
     * This function add the data to the table
     **/
    public void addData(RealmList<CaMoveResponseMod> listModel) {

        if(tl != null && textViewTurn!= null && listModel != null){
            int numCompanies = listModel.size();
            textViewTurn.setText(listModel.get(0).getTurn().toString());
            clearViewTableLayout(tl);
            String cdMov="";
            int colorMov=0;
            for (int i = 0; i < numCompanies; i++) {
                TableRow tr = new TableRow(getContext());
                tr.setTag("listAsistance");
                tr.setLayoutParams(getLayoutParams());
                tr.addView(getTextView(i + 1, listModel.get(i).getMovType(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getContext(), R.color.base_transparent)));

                cdMov=(!listModel.get(i).getMovDate().equals("Pendiente") && !listModel.get(i).getMovDate().equals("Sin registro")) ? (listModel.get(i).getMovDate() + " " + listModel.get(i).getMovHour()) : listModel.get(i).getMovDate();
                colorMov=(!listModel.get(i).getMovDate().equals(Constants.PENDIENTE)) ? Color.BLACK : Color.RED;

                tr.addView(getTextView(i + 1, cdMov, colorMov , Typeface.NORMAL, ContextCompat.getColor(getContext(), R.color.base_transparent)));

                tl.addView(tr, getTblLayoutParams());
            }

        }
    }

    public void clearViewTableLayout(TableLayout tl){
        for (int i = tl.getChildCount(); --i >= 1;) {
            if(tl.getChildAt(i).getTag() != null){
                if(tl.getChildAt(i).getTag().equals("listAsistance")){
                    tl.removeViewAt(i);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccessGetAsistance(CaRegAssistanceResponseMod caRegAssistanceResponseMod, boolean isDateRemote) {

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                addHeaders();

                if(textViewTurn!= null && caRegAssistanceResponseMod.getTpTurno() == null)
                    textViewTurn.setText(caRegAssistanceResponseMod.getMessageMov());

                addData(caRegAssistanceResponseMod.getListAsistance());
                CaLoading.hide();
            }, 500);
    }


    protected View getLayoutRoorView(){
        View viewRoot=getActivity() != null ? getActivity().getCurrentFocus() : null;
        return viewRoot;
    }

    protected View getLayoutRootView(){
        View view=getActivity() != null ? getActivity().getCurrentFocus() == null ? getActivity().findViewById(R.id.containerlistasistance) : getActivity().getCurrentFocus() : null;
        return view;
    }

    @Override
    public void onErrorGetAsistane(String mensaje, boolean isDateRemote) {
        Log.d("Creepy","Error obtener listAsistencia remoto "+mensaje);
        CaLoading.hide();
        if(isDateRemote){
           presenter.getAsistance(false,this);
        }else{
              mensajeSinAcceso="Sin acceso al servidor, intente de nuevo";
                     CaLoading.show(getContext(),mensajeSinAcceso,true);
                 Log.d("Teclo","Error obtener listAsistencia realm "+mensaje);
        }
    }

    @Override
    public void onTasckAsyncListIsRuning(boolean isRunningAsynTack) {
        this.isRunningAsynTack=isRunningAsynTack;
    }

    @Override
    public void onResponseAsyncTasckListAsistance(CaRegAssistanceResponseMod resul, boolean isDateRemote) {

        if(textViewTurn!= null && resul.getTpTurno() == null)
            textViewTurn.setText(resul.getMessageMov());

        addData(resul.getListAsistance());
        CaLoading.hide();
    }

    @Override
    public void onErrorAsyncTasckListAsistance(String message, boolean isDateRemote) {
            presenter.getAsistance(false,this);
    }
}
