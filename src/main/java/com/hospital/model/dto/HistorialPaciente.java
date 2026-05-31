package com.hospital.model.dto;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
 
public class HistorialPaciente {
 
    // Paciente
    private int       idPaciente;
    private String    paciente;
    private LocalDate fechaNacimiento;
    private String    eps;
 
    // Historia clínica
    private int           idHistoriaClinica;
    private String        estadoHistoria;
    private LocalDateTime fechaAperturaHistoria;
 
    // Evento
    private int       idEvento;
    private LocalDate fechaEvento;
    private LocalTime horaEvento;
    private String    tipoEvento;
    private String    descripcionEvento;
 
    // Consulta
    private Integer idConsulta;
    private String  motivoConsulta;
    private String  tipoConsulta;
 
    // Médico
    private String medico;
    private String especialidad;
    private String departamento;
 
    // Receta
    private Integer       idReceta;
    private LocalDate     fechaReceta;
 
    public HistorialPaciente() {}
 
    // Getters
    public int       getIdPaciente()             { return idPaciente; }
    public String    getPaciente()               { return paciente; }
    public LocalDate getFechaNacimiento()        { return fechaNacimiento; }
    public String    getEps()                    { return eps; }
    public int       getIdHistoriaClinica()      { return idHistoriaClinica; }
    public String    getEstadoHistoria()         { return estadoHistoria; }
    public LocalDateTime getFechaAperturaHistoria() { return fechaAperturaHistoria; }
    public int       getIdEvento()               { return idEvento; }
    public LocalDate getFechaEvento()            { return fechaEvento; }
    public LocalTime getHoraEvento()             { return horaEvento; }
    public String    getTipoEvento()             { return tipoEvento; }
    public String    getDescripcionEvento()      { return descripcionEvento; }
    public Integer   getIdConsulta()             { return idConsulta; }
    public String    getMotivoConsulta()         { return motivoConsulta; }
    public String    getTipoConsulta()           { return tipoConsulta; }
    public String    getMedico()                 { return medico; }
    public String    getEspecialidad()           { return especialidad; }
    public String    getDepartamento()           { return departamento; }
    public Integer   getIdReceta()               { return idReceta; }
    public LocalDate getFechaReceta()            { return fechaReceta; }
 
    // Setters
    public void setIdPaciente(int v)                       { idPaciente = v; }
    public void setPaciente(String v)                      { paciente = v; }
    public void setFechaNacimiento(LocalDate v)            { fechaNacimiento = v; }
    public void setEps(String v)                           { eps = v; }
    public void setIdHistoriaClinica(int v)                { idHistoriaClinica = v; }
    public void setEstadoHistoria(String v)                { estadoHistoria = v; }
    public void setFechaAperturaHistoria(LocalDateTime v)  { fechaAperturaHistoria = v; }
    public void setIdEvento(int v)                         { idEvento = v; }
    public void setFechaEvento(LocalDate v)                { fechaEvento = v; }
    public void setHoraEvento(LocalTime v)                 { horaEvento = v; }
    public void setTipoEvento(String v)                    { tipoEvento = v; }
    public void setDescripcionEvento(String v)             { descripcionEvento = v; }
    public void setIdConsulta(Integer v)                   { idConsulta = v; }
    public void setMotivoConsulta(String v)                { motivoConsulta = v; }
    public void setTipoConsulta(String v)                  { tipoConsulta = v; }
    public void setMedico(String v)                        { medico = v; }
    public void setEspecialidad(String v)                  { especialidad = v; }
    public void setDepartamento(String v)                  { departamento = v; }
    public void setIdReceta(Integer v)                     { idReceta = v; }
    public void setFechaReceta(LocalDate v)                { fechaReceta = v; }
}
