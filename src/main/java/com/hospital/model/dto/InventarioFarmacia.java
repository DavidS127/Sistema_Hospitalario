package com.hospital.model.dto;
 
public class InventarioFarmacia {
 
    // Farmacia
    private int    idFarmacia;
    private String farmacia;
    private String ubicacionFarmacia;
    private String estadoFarmacia;
 
    // Medicamento
    private int    idMedicamento;
    private String medicamento;
    private String concentracion;
    private String formaFarmaceutica;
    private String viaAdministracion;
 
    // Stock
    private int    stock;
    private String alertaStock;     // 'CRITICO' | 'BAJO' | 'OK'
 
    public InventarioFarmacia() {}
 
    // Getters
    public int    getIdFarmacia()        { return idFarmacia; }
    public String getFarmacia()          { return farmacia; }
    public String getUbicacionFarmacia() { return ubicacionFarmacia; }
    public String getEstadoFarmacia()    { return estadoFarmacia; }
    public int    getIdMedicamento()     { return idMedicamento; }
    public String getMedicamento()       { return medicamento; }
    public String getConcentracion()     { return concentracion; }
    public String getFormaFarmaceutica() { return formaFarmaceutica; }
    public String getViaAdministracion() { return viaAdministracion; }
    public int    getStock()             { return stock; }
    public String getAlertaStock()       { return alertaStock; }
 
    // Setters
    public void setIdFarmacia(int v)          { idFarmacia = v; }
    public void setFarmacia(String v)         { farmacia = v; }
    public void setUbicacionFarmacia(String v){ ubicacionFarmacia = v; }
    public void setEstadoFarmacia(String v)   { estadoFarmacia = v; }
    public void setIdMedicamento(int v)       { idMedicamento = v; }
    public void setMedicamento(String v)      { medicamento = v; }
    public void setConcentracion(String v)    { concentracion = v; }
    public void setFormaFarmaceutica(String v){ formaFarmaceutica = v; }
    public void setViaAdministracion(String v){ viaAdministracion = v; }
    public void setStock(int v)               { stock = v; }
    public void setAlertaStock(String v)      { alertaStock = v; }
}