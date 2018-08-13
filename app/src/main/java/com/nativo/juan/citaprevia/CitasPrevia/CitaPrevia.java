package com.nativo.juan.citaprevia.CitasPrevia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nativo.juan.citaprevia.Adapters.ArsAdapter;
import com.nativo.juan.citaprevia.Adapters.CitasAdapter;
import com.nativo.juan.citaprevia.R;
import com.nativo.juan.citaprevia.Res.apiRespuestaCitas;
import com.nativo.juan.citaprevia.Retrofit.CitaPreviaApi;
import com.nativo.juan.citaprevia.Utils.ApiError;
import com.nativo.juan.citaprevia.Utils.SessionPrefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nativo.juan.citaprevia.model.ApiMessageResponse;
import com.nativo.juan.citaprevia.model.CitaDisplayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CitaPrevia extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView cpListaDeCitas;
    private View cpEmptyStateContainer;
    private CitasAdapter citasAdapter;
    private Retrofit cpRetroAdapter;
    private CitaPreviaApi citaPreviaApi;
    private Spinner statusFilterSpinner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton cpFab;

    public static final String TAG = CitaPrevia.class.getSimpleName();

    private static final int STATUS_FILTER_DEFAULT_VALUE = 0;

    private static final int REQUEST_ADD_APPOINMENT = 1;

    public static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //La clase SessionPrefs encargada de iniciar el login desde la Clase CitaPrevia
        //via intent y finalizando cuando el login es un exito
        if(!SessionPrefs.get(this).isLoggedIn()){
            Intent intent = new Intent(CitaPrevia.this,
                    LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_cita_previa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remover título de la action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        statusFilterSpinner = (Spinner)findViewById(R.id.toolbar_spinner);
        cpListaDeCitas = (RecyclerView)findViewById(R.id.list_appointments);
        cpEmptyStateContainer = findViewById(R.id.empty_state_container);
        cpFab = (FloatingActionButton) findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_swipe_refresh_layout);

        //Filtrado del espiner donde se muestran las citas con los paramentros
        //Todas, Cumplidas, Canceladas y cumplidas
        statusFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String status = parent.getItemAtPosition(position).toString();
                cargarCitas(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Carga de spinner
        ArrayAdapter<String> statusFilterAdapter = new ArrayAdapter<String>(
                toolbar.getContext(),
                R.layout.spinner_items,
                CitaDisplayList.STATES_VALUES);
        statusFilterSpinner.setAdapter(statusFilterAdapter);
        statusFilterAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        //Llamada de la clase que muestra el despliegue de las citas y ejecuta
        //La cancelacion de las citas
        citasAdapter = new CitasAdapter(this, new ArrayList<CitaDisplayList>(0));
        citasAdapter.setOnItemClickListener(new CitasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CitaDisplayList clickedCita) {

            }

            @Override
            public void onCancelCita(CitaDisplayList cancelCita) {

                cancelarCitas(cancelCita.getCdlId());
            }
        });


        cpListaDeCitas.setAdapter(citasAdapter);

        //Icono por donde se accede a la carga de nuevas citas
        cpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muestraAgregaCitas();
            }
        });

        //Refrescado de la carga de las citas
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                statusFilterSpinner.setSelection(STATUS_FILTER_DEFAULT_VALUE);
                cargarCitas(getCurrentState());
            }
        });


        //Llamada del json con las citas desde el servidor
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        cpRetroAdapter = new Retrofit.Builder()
                .baseUrl(CitaPreviaApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        citaPreviaApi = cpRetroAdapter.create(CitaPreviaApi.class);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Muestra del mensaje que valida las cargas de las citas
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ADD_APPOINMENT == requestCode
                && RESULT_OK == resultCode) {
            muestraMensajeSalvadoExitoso();
        }
    }

    //Metodo sobre cargado de onResume para la carga de las citas
    @Override
    protected void onResume() {
        super.onResume();
        cargarCitas(getCurrentState());
    }

    //Status dee carga de las citas
    private String getCurrentState()
    {
        String status = (String) statusFilterSpinner.getSelectedItem();
        return status;
    }

    //Metodo que carga las citas, tomando el toquen enviado por el servidor
    //con los status, activas, canceladas, cumplidas y todas
    public void cargarCitas(String rawStatus)
    {

        muestraIndicadorCargando(true);

        String token = SessionPrefs.get(this).getToken();

        String status;

        // Elegir valor del estado según la opción del spinner
        switch (rawStatus) {
            case "Activas":
                status = "Activa";
                break;
            case "Canceladas":
                status = "Cancelada";
                break;
            case "Cumplidas":
                status = "Cumplida";
                break;
            default:
                status = "Todas";
        }

        // Construir mapa de parámetros
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("status", status);
        parameters.put("display", "list");


        //Llamada de el listado de citas con el token y los parametros, donde se
        //verifica si hay algun error en la carga de las citas
        Call<apiRespuestaCitas> call = citaPreviaApi.getCitas(token, parameters);
        call.enqueue(new Callback<apiRespuestaCitas>() {
            @Override
            public void onResponse(Call<apiRespuestaCitas> call,
                                   Response<apiRespuestaCitas> response) {
                if(!response.isSuccessful()) {
                    String error = "Ha ocurrido un error. Contacte al administrador";
                    if (response.errorBody()
                            .contentType()
                            .subtype()
                            .equals("json")) {
                        ApiError apiError = ApiError.fromResposeBody(response.errorBody());
                        error = apiError.getMessage();
                      //  Log.d(TAG, apiError.getDeveloperMessage());
                    } else {

                        try {
                            Log.d(TAG, response.errorBody().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    muestraIndicadorCargando(false);
                    muestraErrorMensaje(error);
                    return;
                }

                List<CitaDisplayList> citasServidor = response.body().getResults();


                if(citasServidor.size() > 0)
                {
                    citasAdapter.swapItems(citasServidor);
                    mostrarCitas(citasServidor);
                }else {

                    noMostrarCitas();
                }

                muestraIndicadorCargando(false);
            }

            @Override
            public void onFailure(Call<apiRespuestaCitas> call, Throwable t) {
                muestraIndicadorCargando(false);
                Log.d("Falla Retrofit", t.getMessage());
            }
        });
    }

    //Metodo para cancelar las citas
    private void cancelarCitas(String citaId) {

        // TODO: Mostrar estado de carga

        // Obtener token de usuario
        String token = SessionPrefs.get(this).getToken();

        // Preparar cuerpo de la petición
        HashMap<String, String> statusMap = new HashMap<>();
        statusMap.put("status", "Cancelada");

        // Enviar petición
        citaPreviaApi.cancelarCitas(citaId, token, statusMap).enqueue(
                new Callback<ApiMessageResponse>() {
                    @Override
                    public void onResponse(Call<ApiMessageResponse> call,
                                           Response<ApiMessageResponse> response) {
                        if (!response.isSuccessful()) {
                            // Procesar error de API
                            String error = "Ha ocurrido un error. Contacte al administrador";
                            if (response.errorBody()
                                    .contentType()
                                    .subtype()
                                    .equals("json")) {
                                ApiError apiError = ApiError.fromResposeBody(response.errorBody());

                                error = apiError.getMessage();
                                Log.d(TAG, apiError.getDeveloperMessage());
                            } else {
                                try {
                                    // Reportar causas de error no relacionado con la API
                                    Log.d(TAG, response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            // TODO: Ocultar estado de carga
                            muestraErrorMensaje(error);
                            return;
                        }

                        // Cancelación Exitosa
                        Log.d(TAG, response.body().getMessage());
                        cargarCitas(getCurrentState());
                        // TODO: Ocultar estado de carga
                    }

                    @Override
                    public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                        // TODO: Ocultar estado de carga
                        Log.d(TAG, "Petición rechazada:" + t.getMessage());
                        muestraErrorMensaje("Error de comunicación");
                    }
                }
        );

    }

    //Muestra el indicador de las citas cargadas
    private void muestraIndicadorCargando(final boolean show) {
        final SwipeRefreshLayout refreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh_layout);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
    }

    //Muestra el mensaje de error
    private void muestraErrorMensaje(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    //Muestra las citas
    private void mostrarCitas(List<CitaDisplayList> serverCitas) {
        citasAdapter.swapItems(serverCitas);

        cpListaDeCitas.setVisibility(View.VISIBLE);
        cpEmptyStateContainer.setVisibility(View.GONE);
    }

    //No mostrar las citas
    private void noMostrarCitas() {
        cpListaDeCitas.setVisibility(View.GONE);
        cpEmptyStateContainer.setVisibility(View.VISIBLE);
    }

    //Muestra mensaje de salvado exitoso
    private void muestraMensajeSalvadoExitoso() {
        Snackbar.make(cpFab, R.string.message_appointment_succesfully_saved,
                Snackbar.LENGTH_LONG).show();
    }

    //Muestra agrega citas
    private void muestraAgregaCitas(){
        Intent intent = new Intent(this, AddAppointmentActivity.class);
        startActivityForResult(intent, REQUEST_ADD_APPOINMENT);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cita_previa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(isNetworkAvailable()) {
            loading("Buscando...");
        if (id == R.id.nav_hospital) {
             Intent intent = new Intent(CitaPrevia.this,
                     ListHealthCenters.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_ars) {
            Intent intent = new Intent(CitaPrevia.this,
                    ARSActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        } else if (id == R.id.nav_tarjeta) {

        } else if (id == R.id.nav_afiliado) {

        } else if (id == R.id.nav_config) {

        } else if (id == R.id.nav_back) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * isNetworkAvailable
     * @return true if internet connection is available and @return false if not available
     * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * show progress dialog while loading maps or problems
     * */
    void loading(String message){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
