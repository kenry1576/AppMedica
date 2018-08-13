package com.nativo.juan.citaprevia.Res;

import com.nativo.juan.citaprevia.model.Doctor;

import java.util.List;

/**
 * Created by juan on 4/1/18.
 */

public class DoctorsAvailabilityRes {

    private List<Doctor> results;

    public DoctorsAvailabilityRes(List<Doctor> results) {
        this.results = results;
    }

    public List<Doctor> getResults() {
        return results;
    }
}
