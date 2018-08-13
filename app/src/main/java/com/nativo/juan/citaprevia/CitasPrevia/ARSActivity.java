package com.nativo.juan.citaprevia.CitasPrevia;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.nativo.juan.citaprevia.Adapters.ArsAdapter;
import com.nativo.juan.citaprevia.R;
import com.nativo.juan.citaprevia.Retrofit.CitaPreviaApi;
import com.nativo.juan.citaprevia.model.ArsModel;
import com.nativo.juan.citaprevia.model.JSONResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ARSActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ArsModel> data;
    private ArsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ars);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initViews();
    }


    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view_ars);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.learn2crack.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CitaPreviaApi request = retrofit.create(CitaPreviaApi.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getARS()));
                adapter = new ArsAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
