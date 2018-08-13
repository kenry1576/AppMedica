package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 5/6/18.
 */

public class ArsModel {

    @SerializedName("ver")
    private String ver = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("api")
    private String api = "";

    public String getVer() {
        return ver;
    }

    public String getName() {
        return name;
    }

    public String getApi() {
        return api;
    }
}
