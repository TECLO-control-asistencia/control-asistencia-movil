package com.teclo.controlasistenciamovil.webservices;

/**
 * Created by omhack on 4/9/18.
 */

public interface BaseCallback<T> {

    void onSuccess(T t);

    void onFailure(ServiceError serviceError);
}
