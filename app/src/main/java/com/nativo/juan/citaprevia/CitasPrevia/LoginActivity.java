package com.nativo.juan.citaprevia.CitasPrevia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nativo.juan.citaprevia.model.Afiliados;
import com.nativo.juan.citaprevia.Utils.ApiError;
import com.nativo.juan.citaprevia.R;
import com.nativo.juan.citaprevia.Retrofit.CitaPreviaApi;
import com.nativo.juan.citaprevia.Utils.SessionPrefs;

import com.nativo.juan.citaprevia.model.LoginBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ImageView cpLogoView;
    private EditText cpUserIdView;
    private EditText cpPasswordView;
    private TextInputLayout cpFloatLabelUserId;
    private TextInputLayout cpFloatLabelPassword;
    private View cpProgressView;
    private View cpLoginFormView;
    private Button cpEntrarButton;

    private Retrofit cpRetroAdapter;
    private CitaPreviaApi citaPreviaApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        //Crear la conexion con el servicio Restful
        cpRetroAdapter = new Retrofit.Builder()
                .baseUrl(CitaPreviaApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Asigna la conexion creada a el api CitaPreviaApi
        citaPreviaApi = cpRetroAdapter.create(CitaPreviaApi.class);

        //Inicializa el editor del campo password relacionando el id de usuario y el evento
        //Enter del boton
        cpPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.user_id || id == EditorInfo.IME_NULL) {
                    if(!isOnline()){
                        muestraErrorDeLogin(getString(R.string.error_network));
                        return false;
                    }
                    citaPreviaLogin();
                    return true;
                }
                return false;
            }
        });

        //Boton que inicia el login
        cpEntrarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                citaPreviaLogin();
            }
        });

    }

    //Inicializacion de las vistas y componentes
    public void initViews()
    {
        cpLogoView = (ImageView)findViewById(R.id.image_logo);
        cpUserIdView = (EditText) findViewById(R.id.user_id);
        cpPasswordView = (EditText) findViewById(R.id.password);
        cpFloatLabelUserId = (TextInputLayout)findViewById(R.id.float_label_user_id);
        cpFloatLabelPassword = (TextInputLayout)findViewById(R.id.float_label_password);
        cpEntrarButton = (Button)findViewById(R.id.email_sign_in_button);
        cpLoginFormView = findViewById(R.id.login_form);
        cpProgressView = findViewById(R.id.login_progress);

    }

    private void citaPreviaLogin() {
        //Resetea los errores
        cpFloatLabelUserId.setError(null);
        cpFloatLabelPassword.setError(null);

        String userId = cpUserIdView.getText().toString();
        String password = cpPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Chequea si la clave introducida es valida
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            cpFloatLabelPassword.setError(getString(R.string.error_incorrect_password));
            focusView = cpFloatLabelPassword;
            cancel = true;
        }

        // Chequea si el usuario es valido
        if (TextUtils.isEmpty(userId)) {
            cpFloatLabelUserId.setError(getString(R.string.error_field_required));
            focusView = cpFloatLabelUserId;
            cancel = true;
        } else if (!isUserlValid(userId)) {
            cpFloatLabelUserId.setError(getString(R.string.error_invalid_email));
            focusView = cpFloatLabelUserId;
            cancel = true;
        }

        if (cancel) {
            // Si hubo un error, no se loguea y mantiene el foco el el campo usuario
            focusView.requestFocus();
        } else {
            // Muestra un spinner de progreso, e finaliza la tarea fondo
            // realiza un intento de logueo.
            showProgress(true);

            Call<Afiliados> loginCall = citaPreviaApi.login(new LoginBody(userId, password));

            loginCall.enqueue(new Callback<Afiliados>() {
                @Override
                public void onResponse(Call<Afiliados> call, Response<Afiliados> response) {
                    showProgress(false);

                    if(!response.isSuccessful())
                    {
                        String error;
                        if(response.errorBody()
                            .contentType()
                            .subtype()
                            .equals("application/json")){
                            ApiError apiError = ApiError.fromResposeBody(response.errorBody());
                            error = apiError.getMessage();
                            Log.d("LoginActivity", apiError.getDeveloperMessage());
                        }else{
                            error = response.message();
                        }

                        muestraErrorDeLogin(error);
                        return;
                    }

                    SessionPrefs.get(LoginActivity.this).saveAffilite(response.body());
                    muestraLasCitasPrevias();

                }

                @Override
                public void onFailure(Call<Afiliados> call, Throwable t) {

                    showProgress(false);
                    muestraErrorDeLogin(t.getMessage());
                }
            });
        }
    }

    private boolean isUserlValid(String userId) {
        return userId.length() > 4;
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showProgress(final boolean show) {
        cpProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        cpLogoView.setVisibility(visibility);
        cpLoginFormView.setVisibility(visibility);
    }

    private void muestraLasCitasPrevias()
    {
        Intent intent = new Intent(LoginActivity.this,
                CitaPrevia.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

        finish();
    }

    private void muestraErrorDeLogin(String error)
    {
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
    }

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
