package com.teclo.controlasistenciamovil.webservices;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.teclo.controlasistenciamovil.data.model.Status;

/**
 * Created by omhack on 4/9/18.
 */

public class RetrofitCallback <T> implements Callback<T> {

    private BaseCallback<T> callback;

    public RetrofitCallback(BaseCallback<T> baseCallback) {
        this.callback = baseCallback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            callback.onSuccess(response.body());
        } else {
            ServiceError serviceError = new ServiceError();

            try {
                if (response.code() == 404 || response.code() == 503) {


                    String resultStatusJSON=response.errorBody().string();
                    Status status=new Status();

                    if(resultStatusJSON.contains("message")){
                        resultStatusJSON= resultStatusJSON.replace("message","descripcion");
                        JsonElement mJson = new JsonParser().parse(resultStatusJSON);
                        status = new Gson().fromJson(mJson, Status.class);
                    }

                    serviceError.setStatus(status);
                    serviceError.setErrorCode(response.code());
                } else if (response.errorBody() != null) {
                    JsonElement mJson = new JsonParser().parse(response.errorBody().string());
                    serviceError = new Gson().fromJson(mJson, ServiceError.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
                serviceError.setClientErrorMessage("Hubo un problema de comunicación, intente más tarde");
            }
            callback.onFailure(serviceError);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        ServiceError serviceError = new ServiceError(t.toString());
        if(t instanceof UnknownHostException || t instanceof SSLException){
            serviceError.setClientErrorMessage("Tu conexión a la red ha fallado, por favor intenta nuevamente");
        }
        callback.onFailure(serviceError);
    }
}
