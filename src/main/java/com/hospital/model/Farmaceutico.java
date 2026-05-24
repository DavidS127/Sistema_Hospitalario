package com.hospital.model;

public class Farmaceutico {

    private int idEmpleado;
    private String licencia_profesional;
    private int idFarmacia;

    public Farmaceutico() {}

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getLicenciaProfesional() {
        return licencia_profesional;
    }

    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setLicenciaProfesional(String licencia_profesional) {
        this.licencia_profesional = licencia_profesional;
    }

    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }

}
