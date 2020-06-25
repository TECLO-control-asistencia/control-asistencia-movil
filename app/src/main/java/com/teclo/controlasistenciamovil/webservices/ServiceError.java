package com.teclo.controlasistenciamovil.webservices;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import com.teclo.controlasistenciamovil.data.model.Status;

/**
 * Created by omhack on 4/9/18.
 */

//moficar con el cuerpo de error que regresa el servicio
public class ServiceError {
    private Integer clientErrorCode;
    private String clientErrorMessage;
    private Integer errorCode;
    private List<String> errorMessages = null;
    private String externalErrorCode;
    public Status status;

    public ServiceError() {
        clientErrorCode = 9999;
        errorCode = 9999;
    }

    public ServiceError(String errorMessage) {
        this.errorMessages = new ArrayList<>();
        this.errorMessages.add(errorMessage);
        this.clientErrorMessage = errorMessage;
    }

    public Integer getClientErrorCode() {
        return clientErrorCode;
    }

    public void setClientErrorCode(Integer clientErrorCode) {
        this.clientErrorCode = clientErrorCode;
    }

    public String getClientErrorMessage() {
        return TextUtils.isEmpty(clientErrorMessage)? "Servicio no disponible. Intenta nuevamente" : clientErrorMessage;
    }

    public void setClientErrorMessage(String clientErrorMessage) {
        this.clientErrorMessage = clientErrorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getExternalErrorCode() {
        return externalErrorCode;
    }

    public void setExternalErrorCode(String externalErrorCode) {
        this.externalErrorCode = externalErrorCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
