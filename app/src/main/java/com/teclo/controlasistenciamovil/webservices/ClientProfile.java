package com.teclo.controlasistenciamovil.webservices;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

import com.teclo.controlasistenciamovil.BuildConfig;
import com.teclo.controlasistenciamovil.commons.Constants;
import com.teclo.controlasistenciamovil.ui.login.LoginActivity;
import com.teclo.controlasistenciamovil.ui.menuinferior.MenuInferiorActivity;
import com.teclo.controlasistenciamovil.utils.AppSharePreferences;
import com.teclo.controlasistenciamovil.utils.SSLSocketFactoryHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Created by omhack on 4/6/18.
 */

public class ClientProfile {

    public final static String TAG = ClientProfile.class.getSimpleName();

    private static final int RESPONSE_UNAUTHORIZED_401 = 401;
    private static final int RESPONSE_HTTP_RANK_2XX = 2;
    private static final int RESPONSE_HTTP_CLIENT_ERROR = 4;
    private static final int RESPONSE_HTTP_SERVER_ERROR = 5;
    private Map<String,String> header;
    private int apiTimer;
    OkHttpClient.Builder httpClientBuilder;

    AppSharePreferences sharePreferences;
    private Context context;


    ClientProfile(){
        httpClientBuilder = new OkHttpClient.Builder();
    }
    ClientProfile(Builder builder){
        this.sharePreferences=builder.sharePreferences;
        this.context=builder.context;

        this.header = builder.header;
        this.apiTimer = builder.apiTimer;
        httpClientBuilder = new OkHttpClient.Builder();
        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            SSLSocketFactory sslSocketFactory = new SSLSocketFactoryHelper(sslContext.getSocketFactory());
            httpClientBuilder.sslSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();

            String token=null;
            try {
                token=sharePreferences.getEncryptedToken();
            }catch (Exception e){
                e.printStackTrace();
            }

            List<String> customHeaders = original.headers().values("@");
            Request.Builder requestBuilder = original.newBuilder().removeHeader("@");

            if(token!= null)
                setAuthHeader(requestBuilder,token);


            for(Map.Entry<String,String> entry:header.entrySet()){
                requestBuilder.addHeader(entry.getKey(),entry.getValue());

                if(entry.getKey().equals("Authorization"))
                    setAuthHeader(requestBuilder,entry.getValue());
            }
            if (!customHeaders.isEmpty() && customHeaders.contains("NoAuth")) {
                requestBuilder.removeHeader("Authorization");
            }
            requestBuilder.method(original.method(),original.body());

            Response response = chain.proceed(requestBuilder.build());



            if (response.code() == RESPONSE_UNAUTHORIZED_401) { //If unauthorized (Token expired)...
                Log.w(TAG, "Request responses code: " + response.code());

                synchronized (this) {
                    String currentToken = sharePreferences.getEncryptedToken(); //Get currently stored token (...)

                    if(currentToken != null && currentToken.equals(token)) {

                        // --- REFRESHING TOKEN --- --- REFRESHING TOKEN --- --- REFRESHING TOKEN ------

                        int code = refreshToken() / 100;                    //Refactor resp. cod ranking

                        if(code != RESPONSE_HTTP_RANK_2XX) {                // If refresh token failed

                            if(code == RESPONSE_HTTP_CLIENT_ERROR           // If failed by error 4xx...
                                    ||
                                    code == RESPONSE_HTTP_SERVER_ERROR ){   // If failed by error 5xx...

                                logout();                                  // ToDo GoTo login screen
                                return response;                            // Todo Shows auth error to user
                            }
                        }else{
                            logout();
                        }   // <<--------------------------------------------New Auth. Token acquired --

                    }

                    // --- --- RETRYING ORIGINAL REQUEST --- --- RETRYING ORIGINAL REQUEST --- --------|

                    String tokenUpdate = sharePreferences.getEncryptedToken();
                    if(tokenUpdate != null) {                  // Checks new Auth. Token
                        setAuthHeader(requestBuilder, tokenUpdate);   // Add Current Auth. Token
                        original = requestBuilder.build();                          // O/w the original request

                        Log.d(TAG,
                                ">>> Retrying original Request >>>\n"
                                        +"To: "+original.url()+"\n"
                                        +"Headers:"+original.headers()+"\n"
                                        +"Body: "+bodyToString(original));  //Shows the magic...

                        //-----------------------------------------------------------------------------|
                        Response responseRetry = chain.proceed(original);// Sends request (w/ New Auth.)
                        //-----------------------------------------------------------------------------|

                        Log.d(TAG,
                                "<<< Receiving Retried Request response <<<\n"
                                        +"To: "+responseRetry.request().url()+"\n"
                                        +"Headers: "+responseRetry.headers()+"\n"
                                        +"Code: "+responseRetry.code()+"\n"
                                        +"Body: "+bodyToString(response.request()));  //Shows the magic.

                        return responseRetry;
                    }

                }
            }else{
                //------------------- 200 --- 200 --- AUTHORIZED --- 200 --- 200 -----------------------
                Log.w(TAG,"Request responses code: "+response.code());
            }

            return response;
        });



        httpClientBuilder.connectTimeout(apiTimer, TimeUnit.SECONDS)
                .readTimeout(apiTimer, TimeUnit.SECONDS)
                .writeTimeout(apiTimer, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(getLogLevel());

        httpClientBuilder.addInterceptor(loggingInterceptor);
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("X-Auth-Token", token); //String.format("Bearer %s", token)
    }

    public OkHttpClient getOkHttpClient(){
        return httpClientBuilder.build();
    }

    private HttpLoggingInterceptor.Level getLogLevel(){
        if(BuildConfig.BUILD_TYPE.equals("debug")){
            return HttpLoggingInterceptor.Level.BODY;
        }
        return HttpLoggingInterceptor.Level.NONE;
    }


    //borarr posible mente
    // Refresh/renew Synchronously Authentication Token & refresh token----------------------------|
    public int refreshToken() {
        Log.w(TAG,"Refreshing tokens... ;o");

        // Builds a client...
        OkHttpClient client = new OkHttpClient.Builder().build();

        // Builds a Request Body...for renewing token...
        //MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
        //JsonObject json = new JsonObject();
        //---
        //json.addProperty(BODY_PARAM_KEY_GRANT_TYPE, BODY_PARAM_VALUE_GRANT_TYPE);
        //json.addProperty(BODY_PARAM_KEY_REFRESH_TOKEN,Session.getRefreshAccessToken());
        //---
        //RequestBody body = RequestBody.create(jsonType,json.toString());
        // Builds a request with request body...
        Request request = new Request.Builder()
                .header("X-Auth-Token",sharePreferences.getEncryptedToken())
                .url(Constants.BASE_URL+"login/refresh")
                //.post(body)                     //<<<--------------Adds body (Token renew by the way)
                .build();


        Response response = null;
        int code = 0;

        Log.d(TAG,
                ">>> Sending Refresh Token Request >>>\n"
                        +"To: "+request.url()+"\n"
                        +"Headers:"+request.headers()+"\n"
                        +"Body: "+bodyToString(request));  //Shows the magic...
        try {
            //--------------------------------------------------------------------------------------
            response = client.newCall(request).execute();       //Sends Refresh token request
            //--------------------------------------------------------------------------------------

            Log.d(TAG,
                    "<<< Receiving Refresh Token Request Response <<<\n"
                            +"To: "+response.request().url()+"\n"
                            +"Headers:"+response.headers()+"\n"
                            +"Code: "+response.code()+"\n"
                            +"Body: "+bodyToString(response.request()));  //Shows the magic...

            if (response != null) {
                code = response.code();
                Log.i(TAG,"Token Refresh responses code: "+code);

                switch (code){
                    case 200:
                        // READS NEW TOKENS AND SAVES THEM -----------------------------------------
                        try {
                            //Log.i(TAG,"Decoding tokens start");
                            JSONObject jsonBody = null;
                            jsonBody = new JSONObject(response.body().string());
                            String newAuthtoken = jsonBody.getString("token");
                            //String tokenRefresh = jsonBody.getString("refresh_token");

                            Log.i(TAG,"New Access Token = "+newAuthtoken);
                            // Log.i(TAG,"New Refresh Token = "+tokenRefresh);

                            sharePreferences.setEncryptedToken(newAuthtoken);
                            //Session.setRefreshAccessToken(tokenRefresh);
                            //Log.i(TAG,"Decoding tokens finish.");

                        } catch (JSONException e) {
                            Log.w(TAG,"Responses code "+ code
                                    +" but error getting response body.\n"
                                    + e.getMessage());
                        }

                        break;

                    default:

                        // READS ERROR -------------------------------------------------------------

                        try {
                            //Log.i(TAG,"Decoding error start");
                            JSONObject jsonBodyE = null;
                            jsonBodyE = new JSONObject(response.body().string());
                            String error = jsonBodyE.getString("message");
                            //String errorDescription = jsonBodyE.getString("error_description");

                            //Log.i(TAG,"Decoding tokens finish.");

                        } catch (JSONException e) {
                            Log.w(TAG,"Responses code "+ code
                                    +" but error getting response body.\n"
                                    + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                }


                response.body().close(); //ToDo check this line
            }

        } catch (IOException e) {
            Log.w(TAG,"Error while Sending Refresh Token Request\n"+e.getMessage());
            e.printStackTrace();
        }
        return code;
    }

    private void logout() {
        Log.d(TAG, "go to logout");

        sharePreferences.setEncryptedToken(null);
        sharePreferences.saveDateUserLogin(null);
        sharePreferences.saveImeiDevice(null);
        sharePreferences.deleteDataJustify();
        sharePreferences.saveIsAsyncTaskSaveAsistanceOfflineRunning(false);
        sharePreferences.saveIsAsyncTaskListAsistanceRunningFrList(false);
        sharePreferences.saveIsAsyncTaskListAsistanceRunningFrReg(false);
        sharePreferences.saveIsAsyncTaskSaveAsistanceRunning(false);
        sharePreferences.saveIsAsyncTaskListAsistanceRunningFrReg(false);

        if(context!=null){

            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(Constants.IS_SHOW_OUT_SESSION,true);
            intent.putExtra(Constants.MENSAJE_ESTRA,Constants.MSJ_SESSION_FINISH);
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);// SE AGREGA PARA BORRAR LA PILA DEL BACK

            context.startActivity(intent);

        }

    }

    @Deprecated
    private static String bodyToString(final Request request){


        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if(copy.body() != null) {
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (final IOException e) {
            Log.w(TAG,"Error while trying to get body to string.");
            return "Null";
        }

        return "Null";
    }


    public static final class Builder{
        Map<String,String> header;
        int apiTimer;


        AppSharePreferences sharePreferences;
        Context context;

        public Builder() {

            header = new HashMap<>();
            header.put("Accept", "application/json");
            apiTimer = 60; //DEFAULT timer 60s
        }

        public Builder addOAuth(String token){
            if(!TextUtils.isEmpty(token)){
                header.put("Authorization",token);
            }
            return this;
        }

        public Builder addBasicAuth(String userName, String password){
            if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(password)){
                StringBuilder credentials = new StringBuilder();
                credentials.append(userName).append(":").append(password);
                header.put("Authorization","Basic " + Base64.encodeToString(credentials.toString().getBytes(), Base64.NO_WRAP));
            }
            return this;
        }

        public Builder addAdditionalHeader(Map<String,String> additionalHeader){
            header.putAll(additionalHeader);
            return this;
        }

        public Builder addMetaHeader(Context context){
            this.context=context;
            sharePreferences = new AppSharePreferences(context);
            String token=null;
            header.put("os-version", Build.VERSION.RELEASE);
            header.put("device-id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            try {
                header.put("app-version", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
                token=sharePreferences.getEncryptedToken();
                if(token != null)
                    header.put("Authorization",token);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder setTimeOut(int timeOutInSeconds){
            this.apiTimer = timeOutInSeconds;
            return this;
        }


        public ClientProfile build() {
            return new ClientProfile(this);
        }

    }
}
