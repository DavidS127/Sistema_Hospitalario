package com.hospital.model.dto;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
 
public class DetalleCita {
 
    private int       idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String    estado;
    private LocalDateTime fechaCreacion;
 
    // Paciente
    private int    idPaciente;
    private String paciente;
    private String documentoPaciente;
    private String telefonoPaciente;
    private String eps;
 
    // Médico
    private int    idMedico;
    private String medico;
    private String especialidad;
 
    // Departamento
    private int    idDepartamento;
    private String departamento;
    private String ubicacionDepartamento;
 
    public DetalleCita() {}
 
    // Getters
    public int       getIdCita()               { return idCita; }
    public LocalDate getFecha()                { return fecha; }
    public LocalTime getHora()                 { return hora; }
    public String    getEstado()               { return estado; }
    public LocalDateTime getFechaCreacion()    { return fechaCreacion; }
    public int       getIdPaciente()           { return idPaciente; }
    public String    getPaciente()             { return paciente; }
    public String    getDocumentoPaciente()    { return documentoPaciente; }
    public String    getTelefonoPaciente()     { return telefonoPaciente; }
    public String    getEps()                  { return eps; }
    public int       getIdMedico()             { return idMedico; }
    public String    getMedico()               { return medico; }
    public String    getEspecialidad()         { return especialidad; }
    public int       getIdDepartamento()       { return idDepartamento; }
    public String    getDepartamento()         { return departamento; }
    public String    getUbicacionDepartamento(){ return ubicacionDepartamento; }
 
    // Setters
    public void setIdCita(int v)                    { idCita = v; }
    public void setFecha(LocalDate v)               { fecha = v; }
    public void setHora(LocalTime v)                { hora = v; }
    public void setEstado(String v)                 { estado = v; }
    public void setFechaCreacion(LocalDateTime v)   { fechaCreacion = v; }
    public void setIdPaciente(int v)                { idPaciente = v; }
    public void setPaciente(String v)               { paciente = v; }
    public void setDocumentoPaciente(String v)      { documentoPaciente = v; }
    public void setTelefonoPaciente(String v)       { telefonoPaciente = v; }
    public void setEps(String v)                    { eps = v; }
    public void setIdMedico(int v)                  { idMedico = v; }
    public void setMedico(String v)                 { medico = v; }
    public void setEspecialidad(String v)           { especialidad = v; }
    public void setIdDepartamento(int v)            { idDepartamento = v; }
    public void setDepartamento(String v)           { departamento = v; }
    public void setUbicacionDepartamento(String v)  { ubicacionDepartamento = v; }
}
