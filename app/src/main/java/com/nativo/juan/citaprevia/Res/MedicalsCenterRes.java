package com.nativo.juan.citaprevia.Res;

import java.util.List;

import com.nativo.juan.citaprevia.model.MedicalCenter;

/**
 * Created by juan on 4/1/18.
 */

public class MedicalsCenterRes {

    private List<MedicalCenter> results;

    public MedicalsCenterRes(List<MedicalCenter> results) {
        this.results = results;
    }

    public List<MedicalCenter> getResults() {
        return results;
    }
}
