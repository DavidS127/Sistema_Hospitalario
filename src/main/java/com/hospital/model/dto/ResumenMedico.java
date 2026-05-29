package com.hospital.model.dto;

public class ResumenMedico {

    private String medico;
    private String especialidad;
    private String departamento;
    private int totalConsultas;
    private int totalRecetas;

    public ResumenMedico() {}

    // Getters
    public String getMedico() {
        return medico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public int getTotalConsultas() {
        return totalConsultas;
    }

    public int getTotalRecetas() {
        return totalRecetas;
    }

    // Setters
    public void setMedico(String medico) {
        this.medico = medico;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setTotalConsultas(int totalConsultas) {
        this.totalConsultas = totalConsultas;
    }

    public void setTotalRecetas(int totalRecetas) {
        this.totalRecetas = totalRecetas;
    }
}