package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Cita {

    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private LocalDateTime fechaCreacion;

    private int idPaciente;
    private int idDepartamento;
    private int idMedico;
    private Integer idConsultamedica;

    // Constructor vacío
    public Cita() {}

    // Getters
    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public Integer getIdConsultamedica() {
        return idConsultamedica;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public void setIdConsultamedica(Integer idConsultamedica) {
        this.idConsultamedica = idConsultamedica;
    }
}