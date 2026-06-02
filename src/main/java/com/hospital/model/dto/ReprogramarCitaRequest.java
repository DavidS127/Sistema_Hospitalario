package com.hospital.model.dto;
 
import java.time.LocalDate;
import java.time.LocalTime;
 
/**
 * DTO de REQUEST para la transacción de reprogramación de cita.
 *   Transporta el ID de la cita a cancelar y la nueva fecha/hora
 *   a la que se reprogramará.
 */
public class ReprogramarCitaRequest {
 
    private Integer   idCitaOriginal;  // Cita a cancelar
    private LocalDate nuevaFecha;      // Nueva fecha de la cita
    private LocalTime nuevaHora;       // Nueva hora de la cita
 
    public ReprogramarCitaRequest() {}
 
    public Integer   getIdCitaOriginal() { return idCitaOriginal; }
    public LocalDate getNuevaFecha()     { return nuevaFecha; }
    public LocalTime getNuevaHora()      { return nuevaHora; }
 
    public void setIdCitaOriginal(Integer v) { idCitaOriginal = v; }
    public void setNuevaFecha(LocalDate v)   { nuevaFecha = v; }
    public void setNuevaHora(LocalTime v)    { nuevaHora = v; }
}