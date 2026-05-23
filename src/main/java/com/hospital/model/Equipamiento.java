package com.hospital.model;

public class Equipamiento {

    private int id;
    private String nombre;
    private String estado;
    private int idDepartamento;

    // Constructor vacío
    public Equipamiento() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
}