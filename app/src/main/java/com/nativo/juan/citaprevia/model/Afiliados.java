package com.nativo.juan.citaprevia.model;

/**
 * Created by juan on 3/31/18.
 * Clase que representa a los afiliados en la aplicacion, tomando como argumentos
 * sus nombres, direcciones, generos y token creado en el servidor.
 */

public class Afiliados {

    private String id;
    private String nombre;
    private String direccion;
    private String genero;
    private String token;

    public Afiliados(String id, String nombre, String direccion, String genero, String token) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.genero = genero;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
