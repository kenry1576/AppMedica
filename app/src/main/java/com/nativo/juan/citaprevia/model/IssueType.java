package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 4/1/18.
 */

public class IssueType {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public IssueType(String id, String name) {
        mId = id;
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
