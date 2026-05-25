package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Receta {

    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private int idConsultamedica;

    public Receta() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getIdConsultamedica() {
        return idConsultamedica;
    }

    public void setIdConsultamedica(int idConsultamedica) {
        this.idConsultamedica = idConsultamedica;
    }
}
