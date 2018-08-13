package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by juan on 4/1/18.
 */

public class Doctor {

    @SerializedName("_id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("speciality")
    private String mSpeciality;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("medical_center_id")
    public MedicalCenter medical_centers;
    @SerializedName("availability")
    private List<String> mAvailabilityTimes;

    public Doctor(String id, String name, String specialty,
                  String description, List<String> availabilityTimes) {
        mId = id;
        mName = name;
        mSpeciality = specialty;
        mDescription = description;
        mAvailabilityTimes = availabilityTimes;
    }

    public String getId() {
        return mId;
    }


    public String getName() {
        return mName;
    }


    public String getSpecialty() {
        return mSpeciality;
    }

    public String getDescription() {
        return mDescription;
    }

    public MedicalCenter getMedicalCenter() {
        return medical_centers;
    }


    public List<String> getAvailabilityTimes() {
        return mAvailabilityTimes;
    }
}
