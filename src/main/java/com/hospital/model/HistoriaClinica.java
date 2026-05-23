package com.hospital.model;

import java.time.LocalDateTime;

public class HistoriaClinica {

    private int id;
    private LocalDateTime fechaCreacion;
    private String estado;
    private int idPaciente;

    // Constructor vacío
    public HistoriaClinica() {}

    // Getters
    public int getId() {
        return id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
}