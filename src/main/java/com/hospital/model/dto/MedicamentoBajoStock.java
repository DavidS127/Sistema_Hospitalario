package com.hospital.model.dto;

public class MedicamentoBajoStock {

    private String farmacia;
    private String medicamento;
    private String concentracion;
    private Integer stockActual;
    private Double stockPromedioGlobal;

    public MedicamentoBajoStock() {}

    public String getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(String farmacia) {
        this.farmacia = farmacia;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Double getStockPromedioGlobal() {
        return stockPromedioGlobal;
    }

    public void setStockPromedioGlobal(Double stockPromedioGlobal) {
        this.stockPromedioGlobal = stockPromedioGlobal;
    }
}