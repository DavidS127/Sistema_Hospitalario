package com.hospital.model;

public class Departamento {

    private int id;
    private String nombre;
    private String telefono;
    private String ubicacion;

    // Constructor vacío
    public Departamento() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}