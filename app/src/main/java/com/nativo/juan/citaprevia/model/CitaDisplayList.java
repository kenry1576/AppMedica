package com.nativo.juan.citaprevia.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by juan on 3/31/18.
 */

public class CitaDisplayList {

    public static List<String> STATES_VALUES = Arrays.asList("Todas", "Activas", "Cumplidas", "Canceladas");

    @SerializedName("_id")
    private String cdlId;
    @SerializedName("date_and_time")
    private Date cdlDateAndTime;
    @SerializedName("speciality")
    //private String cdlSpecialities;
    public Specialities cdlSpecialities;
    @SerializedName("status")
    private String cdlStatus;
    @SerializedName("doctor_id")
   // private String cdlDoctor;
    public Doctor doctors;
    @SerializedName("medical_center_id")
    public MedicalCenter medical_centers;
    //private String cdlMedicalCenter;

//    public CitaDisplayList(String mId, Date mDateAndTime, String mStatus, String mDoctor, String mMedicalCenter) {
//        this.cdlId = mId;
//        this.cdlDateAndTime = mDateAndTime;
//        //this.cdlSpecialities = mService;
//        this.cdlStatus = mStatus;
//        //this.cdlDoctor = mDoctor;
//        this.cdlMedicalCenter = mMedicalCenter;
//    }


//    public CitaDisplayList(String mId, Date mDateAndTime, String mStatus) {
//        this.cdlId = mId;
//        this.cdlDateAndTime = mDateAndTime;
//        //this.cdlSpecialities = mService;
//        this.cdlStatus = mStatus;
//        //this.cdlDoctor = mDoctor;
//        //this.cdlMedicalCenter = mMedicalCenter;
//    }

    public CitaDisplayList(String cdlId, Date cdlDateAndTime, Specialities cdlSpecialities, String cdlStatus, Doctor doctors, MedicalCenter medical_centers) {
        this.cdlId = cdlId;
        this.cdlDateAndTime = cdlDateAndTime;
        this.cdlSpecialities = cdlSpecialities;
        this.cdlStatus = cdlStatus;
        this.doctors = doctors;
        this.medical_centers = medical_centers;
    }

    public String getCdlId() {
        return cdlId;
    }

    public void setCdlId(String cdlId) {
        this.cdlId = cdlId;
    }

    public Date getCdlDateAndTime() {
        return cdlDateAndTime;
    }

    public String getDateAndTimeForList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss", Locale.getDefault());
        return sdf.format(cdlDateAndTime);
    }

    public Specialities getCdlSpecialities() {
        return cdlSpecialities;
    }

    public void setCdlSpecialities(Specialities cdlSpecialities) {
        this.cdlSpecialities = cdlSpecialities;
    }

    public String getCdlStatus() {
        return cdlStatus;
    }

    public void setCdlStatus(String cdlStatus) {
        this.cdlStatus = cdlStatus;
    }

    public Doctor getDoctors() {
        return doctors;
    }

    public void setDoctors(Doctor doctors) {
        this.doctors = doctors;
    }

    public MedicalCenter getMedicalCenter() {
        return medical_centers;
    }

    public void setMedicalCenter(MedicalCenter medical_center) {
        this.medical_centers = medical_center;
    }
}
