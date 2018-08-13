package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 4/1/18.
 */

public class MedicalCenter {

    @SerializedName("_id")
    private String mcId;
    @SerializedName("name")
    private String mcName;
    @SerializedName("address")
    private String mcAddress;


    public MedicalCenter(String id, String name, String address) {
        mcId = id;
        mcName = name;
        mcAddress = address;
    }

    public String getId() {
        return mcId;
    }

    public void seId(String mId) {
        this.mcId = mId;
    }

    public String getName() {
        return mcName;
    }

    public void setName(String mName) {
        this.mcName = mName;
    }

    public String getAddress() {
        return mcAddress;
    }

    public void setAddress(String mAddress) {
        this.mcAddress = mAddress;
    }
}