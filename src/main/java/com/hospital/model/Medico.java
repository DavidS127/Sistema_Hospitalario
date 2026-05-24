package com.hospital.model;

public class Medico {

    private int idEmpleado;
    private String especialidad;
    private String registroMedico;

    public Medico() {}

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getRegistroMedico() {
        return registroMedico;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setRegistroMedico(String registroMedico) {
        this.registroMedico = registroMedico;
    }
}