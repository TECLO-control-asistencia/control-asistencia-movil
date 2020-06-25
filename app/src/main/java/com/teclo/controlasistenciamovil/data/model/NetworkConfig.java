package com.teclo.controlasistenciamovil.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omhack on 4/7/18.
 */

public class NetworkConfig {
    private String baseNonSecureURL;
    private String baseSecureURL;
    private String paymentsURL;
    private String publicKey;
    private String publicApiKey;
    private List<String> allowedVersions = new ArrayList<>();
    private List<String> allowVersionNames = new ArrayList<>();
    private boolean paymentEnabled;
    private int smartWalletAcceptedID;
    private boolean prepaymentEnabled;
    private double prepaymentMax;
    private String publicExp;

    public String getBaseNonSecureURL() {
        return baseNonSecureURL;
    }

    public String getBaseSecureURL() {
        return baseSecureURL;
    }

    public String getPaymentsURL() {
        return paymentsURL;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPublicApiKey() { return publicApiKey; }

    public List<String> getAllowedVersions() {
        return allowedVersions;
    }

    public List<String> getAllowVersionNames() {
        return allowVersionNames;
    }

    public boolean isPaymentEnabled() {
        return paymentEnabled;
    }

    public int getSmartWalletAcceptedID() {
        return smartWalletAcceptedID;
    }

    public boolean isPrepaymentEnabled() {
        return prepaymentEnabled;
    }

    public double getPrepaymentMax() {
        return prepaymentMax;
    }

    public String getPublicExp() {
        return publicExp;
    }
}
