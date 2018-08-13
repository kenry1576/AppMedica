package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 4/1/18.
 */

public class Specialities {

    @SerializedName("_id")
    private String spId;
    @SerializedName("name")
    private String spName;


    public Specialities(String id, String name, String address) {
        spId = id;
        spName = name;
    }

    public String getId() {
        return spId;
    }

    public void seId(String mId) {
        this.spId = mId;
    }

    public String getName() {
        return spName;
    }

    public void setName(String mName) {
        this.spName = mName;
    }
}