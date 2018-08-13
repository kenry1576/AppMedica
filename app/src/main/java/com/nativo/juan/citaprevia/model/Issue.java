package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan on 4/1/18.
 */

public class Issue {

    @SerializedName("id")
    private String mId;
    @SerializedName("affiliate_id")
    private String mAffiliate;
    @SerializedName("type_id")
    private String mType;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("created_at")
    private String mCreatedAt;

    public Issue(String id, String affiliate, String type, String description, String createdAt) {
        mId = id;
        mAffiliate = affiliate;
        mType = type;
        mDescription = description;
        mCreatedAt = createdAt;
    }

    public String getId() {
        return mId;
    }

    public String getAffiliate() {
        return mAffiliate;
    }

    public String getType() {
        return mType;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "mId='" + mId + '\'' +
                ", mAffiliate='" + mAffiliate + '\'' +
                ", mType='" + mType + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                '}';
    }
}
