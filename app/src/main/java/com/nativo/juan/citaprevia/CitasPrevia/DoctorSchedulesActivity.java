package com.nativo.juan.citaprevia.CitasPrevia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nativo.juan.citaprevia.Adapters.DoctorSchedulesAdapter;
import com.nativo.juan.citaprevia.R;
import com.nativo.juan.citaprevia.Res.DoctorsAvailabilityRes;
import com.nativo.juan.citaprevia.Retrofit.CitaPreviaApi;
import com.nativo.juan.citaprevia.Utils.ApiError;
import com.nativo.juan.citaprevia.Utils.DateTimeUtils;
import com.nativo.juan.citaprevia.Utils.SessionPrefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.nativo.juan.citaprevia.model.Doctor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoctorSchedulesActivity extends AppCompatActivity {

    private static final String TAG = DoctorSchedulesActivity.class.getSimpleName();

    public static final String EXTRA_DOCTOR_ID = "com.nativo.juan.citaprevia.EXTRA_DOCTOR_ID";
    public static final String EXTRA_DOCTOR_NAME = "com.nativo.juan.citaprevia.EXTRA_DOCTOR_NAME";
    public static final String EXTRA_TIME_SLOT_PICKED = "com.nativo.juan.citaprevia.EXTRA_TIME_SLOT_PICKED";

    private RecyclerView mList;
    private DoctorSchedulesAdapter mListAdapter;
    private ProgressBar mProgress;
    private View mEmptyView;

    private Retrofit mRestAdapter;
    private CitaPreviaApi citaPreviaApi;

    private Date mDateSchedulePicked;
    private String mMedicalCenterId;
    private String mTimeSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        mProgress = findViewById(R.id.progress);
        mEmptyView = findViewById(R.id.doctors_schedules_empty);

        mList = findViewById(R.id.doctors_schedules_list);
        mListAdapter = new DoctorSchedulesAdapter(this,
                new ArrayList<Doctor>(0),
                new DoctorSchedulesAdapter.OnItemListener() {
                    @Override
                    public void onBookingButtonClicked(Doctor bookedDoctor,
                                                       String timeScheduleSelected) {
                        Intent responseIntent = new Intent();
                        responseIntent.putExtra(EXTRA_DOCTOR_ID, bookedDoctor.getId());
                        responseIntent.putExtra(EXTRA_DOCTOR_NAME, bookedDoctor.getName());
                        responseIntent.putExtra(EXTRA_TIME_SLOT_PICKED, timeScheduleSelected);
                        setResult(Activity.RESULT_OK, responseIntent);
                        finish();
                    }
                });

        mList.setAdapter(mListAdapter);


        Intent intent = getIntent();
        mDateSchedulePicked = new Date(intent.getLongExtra(AddAppointmentActivity.EXTRA_DATE_PICKED, -1));
        mMedicalCenterId = intent.getStringExtra(AddAppointmentActivity.EXTRA_MEDICAL_CENTER_ID);
        mTimeSchedule = intent.getStringExtra(AddAppointmentActivity.EXTRA_TIME_SHEDULE_PICKED);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create();
        Log.d("AQUIIIII: ", gson.toString());

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(CitaPreviaApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        citaPreviaApi = mRestAdapter.create(CitaPreviaApi.class);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadDoctorsSchedules() {
        showLoadingIndicator(true);

        String token = SessionPrefs.get(this).getToken();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("date", DateTimeUtils.formatDateForUi(mDateSchedulePicked));
        parameters.put("medical-center", mMedicalCenterId);
        parameters.put("times", mTimeSchedule);

        citaPreviaApi.getDoctorsSchedules(token, parameters).enqueue(
                new Callback<DoctorsAvailabilityRes>() {
                    @Override
                    public void onResponse(Call<DoctorsAvailabilityRes> call,
                                           Response<DoctorsAvailabilityRes> response) {
                        //Log.d("HOLA::::", call.request().toString());
                        if (response.isSuccessful()) {
                            DoctorsAvailabilityRes res = response.body();
                            List<Doctor> doctors = res.getResults();

                            Log.d("HOLA::::", doctors.toString());

                            if (doctors.size() > 0) {
                                showDoctors(doctors);
                            } else {
                                showEmptyView();
                            }

                        } else {
                            String error = "Ha ocurrido un error. Contacte al administrador";

                            if (response.errorBody().contentType().subtype().equals("json")) {
                                ApiError apiError = ApiError.fromResposeBody(response.errorBody());
                                error = apiError.getMessage();
                                Log.d(TAG, apiError.getDeveloperMessage());
                            } else {
                                try {
                                    Log.d(TAG, response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            showApiError(error);
                        }

                        showLoadingIndicator(false);
                    }

                    @Override
                    public void onFailure(Call<DoctorsAvailabilityRes> call, Throwable t) {
                        showLoadingIndicator(false);
                        showApiError(t.getMessage());
                    }
                });
    }

    private void showApiError(String error) {
        Snackbar.make(findViewById(android.R.id.content),
                error, Snackbar.LENGTH_LONG).show();
    }

    private void showDoctors(List<Doctor> doctors) {
        mListAdapter.setDoctors(doctors);
        mList.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showLoadingIndicator(boolean show) {
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        mList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctorsSchedules();
    }

}
