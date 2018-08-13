package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 3/31/18.
 */

public class ApiMessageResponse {
    @SerializedName("status")
    private int amrStatus;
    @SerializedName("message")
    private String amrMessage;

    public ApiMessageResponse(int status, String message) {
        amrStatus = status;
        amrMessage = message;
    }

    public int getStatus() {
        return amrStatus;
    }

    public String getMessage() {
        return amrMessage;
    }
}
