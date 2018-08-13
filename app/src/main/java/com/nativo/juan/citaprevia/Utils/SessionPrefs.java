package com.nativo.juan.citaprevia.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nativo.juan.citaprevia.model.Afiliados;

/**
 * Created by juan on 3/31/18.
 */

public class SessionPrefs {

    public static final String PREFS_NOMBRE = "CITAPREVIA_PREFS";
    public static final String PREF_AFILIADO_ID = "PREF_USER_ID";
    public static final String PREF_AFILIADO_NOMBRE = "PREF_AFILIADO_NOMBRE";
    public static final String PREF_AFILIADO_DIRECCION = "PREF_AFILIADO_DIRECCION";
    public static final String PREF_AFILIADO_GENERO = "PREF_AFILIADO_GENERO";
    public static final String PREF_AFILIADO_TOKEN = "PREF_AFILIADO_TOKEN";

    private SharedPreferences cpPrefs;

    private boolean cpIsLoggedIn;

    private static SessionPrefs INSTANCE;


    public SessionPrefs()
    {

    }

    private SessionPrefs(Context context){
        cpPrefs = context.getApplicationContext().getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE);
        cpIsLoggedIn = !TextUtils.isEmpty(cpPrefs.getString(PREF_AFILIADO_TOKEN, null));
    }

    public static SessionPrefs get(Context context){
        if(INSTANCE == null){
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    public boolean isLoggedIn()
    {
        return cpIsLoggedIn;
    }

    public String getToken(){
        return cpPrefs.getString(PREF_AFILIADO_TOKEN, null);
    }

    public void saveAffilite(Afiliados afiliados)
    {
        if(afiliados != null){
            SharedPreferences.Editor editor = cpPrefs.edit();
            editor.putString(PREF_AFILIADO_ID, afiliados.getId());
            editor.putString(PREF_AFILIADO_NOMBRE, afiliados.getNombre());
            editor.putString(PREF_AFILIADO_DIRECCION, afiliados.getDireccion());
            editor.putString(PREF_AFILIADO_GENERO, afiliados.getGenero());
            editor.putString(PREF_AFILIADO_TOKEN, afiliados.getToken());
            editor.apply();
        }

        cpIsLoggedIn = true;
    }

    public void logOut(){

        cpIsLoggedIn = false;

        SharedPreferences.Editor editor = cpPrefs.edit();
        editor.putString(PREF_AFILIADO_ID, null);
        editor.putString(PREF_AFILIADO_NOMBRE, null);
        editor.putString(PREF_AFILIADO_DIRECCION, null);
        editor.putString(PREF_AFILIADO_GENERO, null);
        editor.putString(PREF_AFILIADO_TOKEN, null);
        editor.apply();
    }


}
