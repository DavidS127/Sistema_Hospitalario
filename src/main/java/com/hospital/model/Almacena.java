package com.hospital.model;

public class Almacena {

    private int idFarmacia;
    private int idMedicamento;
    private int stock;

    // Constructor vacío
    public Almacena() {}

    // Getters
    public int getIdFarmacia() {
        return idFarmacia;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public int getStock() {
        return stock;
    }

    // Setters
    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}