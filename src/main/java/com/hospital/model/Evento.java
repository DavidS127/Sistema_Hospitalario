package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {

    private int id;

    private LocalDate fecha;
    private LocalTime hora;
    private String tipo;
    private String descripcion;
    private int idHistoriaclinica;
    private int idDepartamento;
    private int idMedico;
    private int idConsultamedica;

    public Evento() {}

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdHistoriaclinica() {
        return idHistoriaclinica;
    }

    public void setIdHistoriaclinica(int idHistoriaclinica) {
        this.idHistoriaclinica = idHistoriaclinica;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public int getIdConsultamedica() {
        return idConsultamedica;
    }

    public void setIdConsultamedica(int idConsultamedica) {
        this.idConsultamedica = idConsultamedica;
    }
}