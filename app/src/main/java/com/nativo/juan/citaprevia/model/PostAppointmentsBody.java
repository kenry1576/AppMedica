package com.nativo.juan.citaprevia.model;

/**
 * Created by juan on 4/1/18.
 */

public class PostAppointmentsBody {

    private String datetime;
    private String service;
    private String doctor;

    public PostAppointmentsBody(String datetime, String service, String doctor) {
        this.datetime = datetime;
        this.service = service;
        this.doctor = doctor;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getService() {
        return service;
    }


    public String getDoctor() {
        return doctor;
    }
}
