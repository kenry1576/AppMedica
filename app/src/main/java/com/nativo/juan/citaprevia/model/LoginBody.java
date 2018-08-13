package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 3/31/18.
 * Clase LoginBody la cual maneja el cuerpo de los datos de credenciales de logueo
 */

public class LoginBody {
    @SerializedName("userId")
    private String userId;
    private String password;

    public LoginBody(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
