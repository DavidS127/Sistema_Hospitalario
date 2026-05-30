package com.hospital.model.dto;

import java.time.LocalDate;

public class PacienteResumen {

    private int id;
    private String paciente;
    private String eps;
    private String telefono;
    private LocalDate ultimaCita;
    private int totalMedicamentosRecetados;

    public PacienteResumen() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getUltimaCita() {
        return ultimaCita;
    }

    public void setUltimaCita(LocalDate ultimaCita) {
        this.ultimaCita = ultimaCita;
    }

    public int getTotalMedicamentosRecetados() {
        return totalMedicamentosRecetados;
    }

    public void setTotalMedicamentosRecetados(int totalMedicamentosRecetados) {
        this.totalMedicamentosRecetados = totalMedicamentosRecetados;
    }
}