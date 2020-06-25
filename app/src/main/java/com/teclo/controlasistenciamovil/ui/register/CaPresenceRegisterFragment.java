package com.teclo.controlasistenciamovil.ui.register;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.teclo.controlasistenciamovil.R;
import com.teclo.controlasistenciamovil.application.BaseFragment;
import com.teclo.controlasistenciamovil.application.SicnApp;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.data.model.CaJustificationResponseMod;
import com.teclo.controlasistenciamovil.data.model.CaRegAssistanceResponseMod;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppConectionManager;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.AsyncTaskGetListAsistance;
import com.teclo.controlasistenciamovil.utils.AsyncTaskSavedAsistance;
import com.teclo.controlasistenciamovil.utils.CaLoading;
import com.teclo.controlasistenciamovil.utils.CaPermissionsDialogFrag;
import com.teclo.controlasistenciamovil.utils.TypeOfPermissionUtil;
import com.teclo.controlasistenciamovil.webservices.response.UserResponseMod;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaPresenceRegisterFragment extends BaseFragment
        implements CaPresenceRegisterContract.View,
        CaPresenceRegisterContract.OnResponseGetAsistance,
        CaPresenceRegisterContract.OnResponseGetJustify,
        OnMapReadyCallback,GoogleMap.OnMapLoadedCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,GoogleMap.OnMyLocationButtonClickListener,
        CaPermissionsDialogFrag.PermissionsGrantedCallback,
        AsyncTaskGetListAsistance.AsynckTasckResponseListAsistance,
        AsyncTaskSavedAsistance.AsynckTasckResponseSaveAsistance{

    public static final String TAG = CaPresenceRegisterFragment.class.getSimpleName();

    public static CaPresenceRegisterFragment newInstance() {
        CaPresenceRegisterFragment fragment = new CaPresenceRegisterFragment();
        return fragment;
    }

    @BindView(R.id.btnRegister)
    Button buttonRegister;

    @BindView(R.id.txtNextMov)
    TextView textViewNextMov;

    @Inject
    CaPresenceRegisterContract.Presenter presenter;

    @Inject
    AppSharePreferences sharedPreferences;

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Circle mCircle;
    private Marker mMarker;

    Location currentLoction;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;

    private double latitude;
    private double longitude;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location locationNow;

    List<TypeOfPermissionUtil> listTipePermissionUtil;

    private AlertDialog alert = null;

    private CaRegAssistanceResponseMod caRegAssistanceResponseMod;
    private CaJustificationResponseMod caJustificationResponseMod;

    private boolean isPause=false;
    private boolean isResumen=false;

    @Inject
    @Named(Constants.OAUTH_MS_SERVICE)
    Retrofit retrofit;

    Boolean isRunningAsynTackSaved=false;
    Boolean isRunningAsyncTaskSavedOffline=false;
    Boolean isRunningAsynTackList=false;
    Boolean isShowDialogPermission=false;

    View rootView;

    TelephonyManager telephonyManager;

    String mensaje="Sin acceso al servidor, intente de nuevo";


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_presence_register;
    }

    protected View getLayoutRootView(){
        View view=getActivity().getCurrentFocus() == null ? getActivity().findViewById(R.id.container_presence) : getActivity().getCurrentFocus();
        return view;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        listTipePermissionUtil=new ArrayList<>();
        listTipePermissionUtil.add(new TypeOfPermissionUtil(Constants.TXT_GPS,Manifest.permission.ACCESS_FINE_LOCATION,null));
        callPermission();
        presenter.getJustify(this);

        getBooleansAsyncTask();

        if(bundle != null){
            isShowDialogPermission=bundle.getBoolean(Constants.IS_SHOW_DIALOG_PERMISSION);
        }

        if(!isShowDialogPermission || isRunningAsyncTaskSavedOffline || isRunningAsynTackList || isRunningAsynTackSaved)
            CaLoading.show(getContext(),getString(R.string.load_message),false);

        if(!isRunningAsynTackList)
            new AsyncTaskGetListAsistance(this,presenter,sharedPreferences,getContext(),retrofit,AppConectionManager.isOnline(getContext())).execute();


        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(getContext());
        fetchLastLocation();

    }

    private void fetchLastLocation(){

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Task<Location> task=fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        currentLoction=location;
                        initSupportMapFragment();
                        latitude=currentLoction.getLatitude();
                        longitude=currentLoction.getLongitude();
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable(Constants.IS_SHOW_DIALOG_PERMISSION,isShowDialogPermission);
        sharedPreferences.saveIsAsyncTaskListAsistanceRunningFrReg(isRunningAsynTackList);
        sharedPreferences.saveIsAsyncTaskSaveAsistanceRunning(isRunningAsynTackSaved);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_presence_register, container, false);
        return rootView;
    }

    private void initSupportMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!isShowDialogPermission)
            CaLoading.show(getContext(),getString(R.string.load_message),false);

        fetchLastLocation();

        if(getActivity() instanceof MenuInferiorActivity)
            ((MenuInferiorActivity)getActivity()).chekcMenuBottomByIndex(R.id.nav_asistance_register);

        connectGoogleApiClient();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!validateServiceGPS()){
            if(alert!=null) {
                if(!alert.isShowing()) {
                    showDialogGPS();
                }
            }
        }else{
            if(alert!=null)
                alert.dismiss ();
        }

        buildGoogleApiClient();
    }

    public void getBooleansAsyncTask(){
        isRunningAsynTackList=sharedPreferences.getIsAsyncTaskListAsistanceRunningFrReg();
        isRunningAsynTackSaved=sharedPreferences.getIsAsyncTaskSaveAsistanceRunning();
        isRunningAsyncTaskSavedOffline=sharedPreferences.getIsAsyncTaskSaveAsistanceOfflineRunning();
        ((MenuInferiorActivity)getContext()).setProsesSaveRemoteDateOffline(isRunningAsyncTaskSavedOffline);
    }

    @Override
    public void onResume() {
        super.onResume();

        getBooleansAsyncTask();


        if((isRunningAsynTackList == null && isRunningAsynTackSaved == null) || (!isRunningAsynTackList && !isRunningAsynTackSaved)){
            new AsyncTaskGetListAsistance(this,presenter,sharedPreferences,getContext(),retrofit,AppConectionManager.isOnline(getContext())).execute();
        }


        if(!isShowDialogPermission || isRunningAsyncTaskSavedOffline || isRunningAsynTackList || isRunningAsynTackSaved)
            CaLoading.show(getContext(),getString(R.string.load_message),false);

        connectGoogleApiClient();

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fetchLastLocation();
            onMapLoaded();
        }

        //callPermission();
        if(!validateServiceGPS()){
            if(alert==null) {

                showDialogGPS();
                isShowDialogPermission=true;
            }
        }

        isResumen=true;
        isPause=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        CaLoading.hide();
        disconnectGoogleApiClient();
        if(alert != null) {
            alert.dismiss ();
            alert=null;
        }

        isPause=true;
        isResumen=false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        disconnectGoogleApiClient();
        if(mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(alert != null) {
            alert.dismiss ();
            alert=null;
        }
        CaLoading.hide();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void injectDependencies() {
        super.injectDependencies();
        DaggerCaPresenceRegisterComponent.builder()
                .oAuthComponent(SicnApp.getAppComponent().getOauthSessionManager().getOAuthComponent())
                .caPresenceRegisterModule(new CaPresenceRegisterModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.btnRegister)
    public void savePrecense(){

        getBooleansAsyncTask();

        if((isRunningAsynTackList != null && isRunningAsynTackList) || isRunningAsyncTaskSavedOffline || isRunningAsynTackSaved)
            return;

        CaLoading.show(getContext(),getString(R.string.load_message),false);
        requestLocationUpdates();

        if(!validateServiceGPS() || !getPermission(listTipePermissionUtil)){
            callPermission();
        }else{

            //setLocation();
            //fetchLastLocation();

            if(this.caJustificationResponseMod != null){
                this.caRegAssistanceResponseMod.setDescripcion(caJustificationResponseMod.getDescription()==null ? "" :caJustificationResponseMod.getDescription());
                this.caRegAssistanceResponseMod.setIdCat(caJustificationResponseMod.getIdJustify() == null ? 0L : caJustificationResponseMod.getIdJustify());
            }else{
                this.caRegAssistanceResponseMod.setDescripcion("");
                this.caRegAssistanceResponseMod.setIdCat(0L);
            }

            setLatitudeAndLogitude(this.caRegAssistanceResponseMod);
            this.caRegAssistanceResponseMod.setDateRemote(AppConectionManager.isOnline(getContext()));
            UserResponseMod user=sharedPreferences.getDateUserLogin();

            this.caRegAssistanceResponseMod.setWifiOn(AppConectionManager.isWifiOn(getContext()));
            this.caRegAssistanceResponseMod.setDataMovilOn(AppConectionManager.isDataMovileOn(getContext()));

            new AsyncTaskSavedAsistance(this,sharedPreferences,presenter,getContext(),retrofit,AppConectionManager.isOnline(getContext())).execute(this.caRegAssistanceResponseMod);
        }
    }

    public void setLatitudeAndLogitude(CaRegAssistanceResponseMod caRegAssistanceResponseMod){
        if(locationNow != null){
            caRegAssistanceResponseMod.setLatitude(locationNow.getLatitude());
            caRegAssistanceResponseMod.setLongitude(locationNow.getLongitude());
        }else{
            caRegAssistanceResponseMod.setLatitude(latitude);
            caRegAssistanceResponseMod.setLongitude(longitude);
        }
    }


    private void setLocation() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                locationNow=location;// Logic to handle location object
                            }
                        }
                    });

        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                locationNow = locationResult.getLastLocation();
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.clear();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);

            //LatLngBounds BOUNDS_CDMX = new LatLngBounds(new LatLng(19.1887101, -99.3267771), new LatLng(19.5927571, -98.9604482));
           // drawMarkerWithCircle( new LatLng(19.3016988, -99.1906757));
        }

        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {

        //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS_CDMX, 10));
        //fetchLastLocation();// se descomenta para mostrar siempre la pocicion mietras la app esta en primer plano
        //setLocation();//
        if(mMap!= null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        requestLocationUpdates();
    }


    public void requestLocationUpdates(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }



    @Override
    public void showProgressBar(boolean show) {
    }

    private void drawMarkerWithCircle(LatLng position){
        double radiusInMeters = 100.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);

        mCircle = mMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mMap.addMarker(markerOptions);

    }

    protected synchronized void buildGoogleApiClient() {
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    //.enableAutoManage(this, 0 /* clientId */, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    private void disconnectGoogleApiClient() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void onLocationChanged(Location location) {
        float[] distance = new float[2];

        latitude   = location.getLatitude();
        longitude  = location.getLongitude();

        if(mCircle != null) {
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

            if (distance[0] > mCircle.getRadius()) {
                //     Toast.makeText(mContext, "Outside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
            } else {
                //    Toast.makeText(mContext, "Inside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void createLocationRequest() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);
    }

    private boolean validateServiceGPS() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    public void showDialogGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(Constants.MSJ_PERMISSION_GPS)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        isShowDialogPermission=false;
                        alert.dismiss();
                        alert=null;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onSuccessGetAsistance(CaRegAssistanceResponseMod caRegAssistanceResponseMod,boolean isDateRemote) {
        this.caRegAssistanceResponseMod=caRegAssistanceResponseMod;

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            nextMovRegister(caRegAssistanceResponseMod);
            CaLoading.hide();
        }, 500);
    }

    @Override
    public void onErrorGetAsistane(String mensaje,boolean isDateRemote) {

        CaLoading.hide();
        if(isDateRemote){
            Log.d("Teclo","Error obtener listAsistencia remoto "+mensaje);
            presenter.getAsistance(false,this);
        }else{
            buttonRegister =(Button)rootView.findViewById(R.id.btnRegister);
            textViewNextMov=(TextView)rootView.findViewById(R.id.txtNextMov);
            buttonRegister.setEnabled(false);
            buttonRegister.getBackground().setAlpha(64);
            textViewNextMov.setTextColor(Color.parseColor("#d50000"));
            CaLoading.show(getContext(),"Sin acceso al servidor, intente de nuevo",true);
        }
    }

    public void nextMovRegister(CaRegAssistanceResponseMod assistanceResponse){
        if (assistanceResponse != null) {

            boolean btnDisabled = assistanceResponse.isBtnRegisterDisabled();
            buttonRegister =(Button)rootView.findViewById(R.id.btnRegister);
            textViewNextMov=(TextView)rootView.findViewById(R.id.txtNextMov);

            if (btnDisabled) {
                buttonRegister.setEnabled(false);
                buttonRegister.getBackground().setAlpha(64);
                textViewNextMov.setTextColor(Color.parseColor("#d50000"));
            } else {
                buttonRegister.setEnabled(true);
                textViewNextMov.setTextColor(Color.parseColor("#00897b"));
            }
            textViewNextMov.setText(assistanceResponse.getMessageMov());
        }
    }

    private Boolean getPermission(List<TypeOfPermissionUtil> listTipePermissionUtil){// se queda

        boolean isGranted=true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (TypeOfPermissionUtil typeOfPermissionUtil : listTipePermissionUtil) {
                if (getActivity().checkSelfPermission(typeOfPermissionUtil.getTpPermission()) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void callPermission() {

        if(!isShowDialogPermission){

            if(!validateServiceGPS()){
                if(alert==null) {

                        showDialogGPS();
                        isShowDialogPermission=true;
                }
            }else{
                if(alert!=null){
                    alert.dismiss();
                    alert=null;
                }

                if(!getPermission(listTipePermissionUtil)){
                    isShowDialogPermission=true;
                    CaPermissionsDialogFrag.newInstance(listTipePermissionUtil).show(getActivity().getSupportFragmentManager(), CaPermissionsDialogFrag.TAG);
                }else{
                    isShowDialogPermission=false;
                }
            }
        }

    }

    @Override
    public void onSuccessGetJustify(CaJustificationResponseMod caJustificationResponseMod) {
        Log.d("Teclo","onSuccessGetJustify Remote");
        this.caJustificationResponseMod=caJustificationResponseMod;
    }

    @Override
    public void onErrorGetJustify(String mensaje) {
        Log.d("Teclo","onErrorGetJustify Remote "+mensaje);
    }

    @Override
    public void onTasckAsyncListIsRuning(boolean isRunningAsynTack) {
        this.isRunningAsynTackList=isRunningAsynTack;
        this.isRunningAsynTackSaved=false;
    }

    @Override
    public void onResponseAsyncTasckListAsistance(CaRegAssistanceResponseMod resul, boolean isDateRemote) {

        this.caRegAssistanceResponseMod=resul;
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            nextMovRegister(caRegAssistanceResponseMod);
            CaLoading.hide();
        }, 500);
    }

    @Override
    public void onErrorAsyncTasckListAsistance(String message, boolean isDateRemote) {
        presenter.getAsistance(false,this);
    }

    @Override
    public void onTasckAsyncSaveAsistanceIsRuning(boolean isRunningAsynTack) {
        this.isRunningAsynTackSaved=isRunningAsynTack;
        this.isRunningAsynTackList=false;
    }

    @Override
    public void onResponseAsyncTasckSavedAsistance(CaRegAssistanceResponseMod resul, boolean isDateRemote) {

        this.caRegAssistanceResponseMod=resul;

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            nextMovRegister(caRegAssistanceResponseMod);
            CaLoading.hide();
        }, 500);

    }

    @Override
    public void onErrorAsyncTasckSavedAsistance(String cdResponse,String message, boolean isDateRemote) {

        if(isDateRemote && cdResponse != null && cdResponse.equals(Constants.STATUS_ERROR_SAVED_ASISTANCE_204)){

            ((MenuInferiorActivity)getContext()).showSnackBar(message,rootView, 0);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if(!isRunningAsynTackList && !isRunningAsynTackSaved){
                    CaLoading.show(getContext(),getString(R.string.load_message),false);
                    new AsyncTaskGetListAsistance(this,presenter,sharedPreferences,getContext(),retrofit,AppConectionManager.isOnline(getContext())).execute();
                }
            }, 700);

        }

        presenter.getAsistance(false,this);
    }
}