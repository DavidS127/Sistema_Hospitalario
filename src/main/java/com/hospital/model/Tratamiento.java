package com.hospital.model;

public class Tratamiento {

    private int id;
    private String descripcion;
    private int idConsultaMedica;

    // Constructor vacío
    public Tratamiento() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIdConsultaMedica() {
        return idConsultaMedica;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdConsultaMedica(int idConsultaMedica) {
        this.idConsultaMedica = idConsultaMedica;
    }
}
