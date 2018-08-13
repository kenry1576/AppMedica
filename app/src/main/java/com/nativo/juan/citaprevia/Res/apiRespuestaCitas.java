package com.nativo.juan.citaprevia.Res;

import java.util.List;

import com.nativo.juan.citaprevia.model.CitaDisplayList;

/**
 * Created by juan on 3/31/18.
 */

public class apiRespuestaCitas {

    private List<CitaDisplayList> results;

    public apiRespuestaCitas(List<CitaDisplayList> results) {
        this.results = results;
    }

    public List<CitaDisplayList> getResults() {
        return results;
    }
}
