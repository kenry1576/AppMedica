package com.nativo.juan.citaprevia.Res;

import com.nativo.juan.citaprevia.Retrofit.CitaPreviaApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by juan on 4/1/18.
 */

public class RestClient {

    private static CitaPreviaApi mCitaPreviaApi = null;

    public static CitaPreviaApi getClient() {
        if (mCitaPreviaApi == null) {

            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(CitaPreviaApi.BASE_URL)
                    .build();

            mCitaPreviaApi = retrofit.create(CitaPreviaApi.class);
        }
        return mCitaPreviaApi;
    }
}
