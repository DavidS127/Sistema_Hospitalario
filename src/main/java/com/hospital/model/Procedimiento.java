package com.hospital.model;

public class Procedimiento {

    private int id;
    private String nombre;
    private int idConsultaMedica;

    // Constructor vacío
    public Procedimiento() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdConsultaMedica() {
        return idConsultaMedica;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdConsultaMedica(int idConsultaMedica) {
        this.idConsultaMedica = idConsultaMedica;
    }
}