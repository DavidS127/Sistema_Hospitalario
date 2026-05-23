package com.hospital.model;

public class Medicamento {

    private int id;
    private String nombre;
    private String concentracion;
    private String viaAdministracion;
    private String formaFarmaceutica;

    // Constructor vacío
    public Medicamento() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }
}