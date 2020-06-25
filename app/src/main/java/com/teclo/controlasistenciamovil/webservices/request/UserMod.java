package com.teclo.controlasistenciamovil.webservices.request;

public class UserMod {
    private String username;
    private String password;
    private String tokenGcm;

    public UserMod(String username, String password,String tokenGcm) {
        this.username = username;
        this.password = password;
        this.tokenGcm = tokenGcm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenGcm() {
        return tokenGcm;
    }

    public void setTokenGcm(String tokenGcm) {
        this.tokenGcm = tokenGcm;
    }
}
