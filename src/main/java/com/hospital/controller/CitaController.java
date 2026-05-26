package com.hospital.controller;

import com.hospital.model.Cita;
import com.hospital.repository.CitaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepo;

    public CitaController(CitaRepository citaRepo) {
        this.citaRepo = citaRepo;
    }

    // GET
    @GetMapping
    public List<Cita> listarCitas() {
        return citaRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarCita(
            @PathVariable int id) {

        return citaRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Cita> crearCita(
            @RequestBody Cita cita) {

        return ResponseEntity
            .status(201)
            .body(citaRepo.save(cita));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarCita(
            @PathVariable int id,
            @RequestBody Cita cita) {

        return citaRepo.update(id, cita)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(
            @PathVariable int id) {

        return citaRepo.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
    
    // ─────────────────────────────────────────────────────────────
    // ENDPOINT DE NEGOCIO: POST /citas/agendar
    //
    // PROPÓSITO:
    //   Agenda una cita pasando por todas las validaciones de negocio
    //   definidas en fn_agendar_cita (PostgreSQL). Es diferente al
    //   POST /citas básico, que inserta directo sin validaciones.
    //
    // REQUEST BODY (JSON):
    // {
    //   "idPaciente": 1,
    //   "idMedico": 1,
    //   "idDepartamento": 1,
    //   "fecha": "2026-06-10",
    //   "hora": "09:00:00"
    // }
    //
    // RESPONSE 201 - Cita agendada correctamente:
    // {
    //   "idCita": 7,
    //   "mensaje": "Cita agendada correctamente."
    // }
    //
    // RESPONSE 400 - Alguna validación falló:
    // {
    //   "error": "El médico 1 ya tiene una cita programada el 2026-06-10 a las 09:00."
    // }
    // ─────────────────────────────────────────────────────────────
    @PostMapping("/agendar")
    public ResponseEntity<?> agendarCita(@RequestBody Cita cita) {
        try {
 
            int idCita = citaRepo.agendarCita(cita);
 
            return ResponseEntity
                .status(201)
                .body(Map.of(
                    "idCita",  idCita,
                    "mensaje", "Cita agendada correctamente."
                ));
 
        } catch (Exception e) {
            // Las excepciones de fn_agendar_cita (RAISE EXCEPTION en PL/pgSQL)
            // llegan aquí. Se extrae el mensaje para retornarlo al cliente.
            String mensajeError = extraerMensajePgSql(e.getMessage());
            return ResponseEntity
                .status(400)
                .body(Map.of("error", mensajeError));
        }
    }
 
    /*
     * MÉTODO AUXILIAR: extraerMensajePgSql
     *
     * El driver JDBC envuelve el mensaje del RAISE EXCEPTION con texto
     * adicional de PostgreSQL. Este método extrae solo la parte legible.
     *
     * Ejemplo raw: "ERROR: El médico 1 ya tiene una cita...\n  Where: PL/pgSQL..."
     * Resultado:   "El médico 1 ya tiene una cita..."
     */
    private String extraerMensajePgSql(String mensajeCompleto) {
        if (mensajeCompleto == null) return "Error desconocido en el servidor.";
 
        int inicio = mensajeCompleto.indexOf("ERROR: ");
        if (inicio >= 0) {
            String desde = mensajeCompleto.substring(inicio + 7);
            int fin = desde.indexOf('\n');
            return fin >= 0 ? desde.substring(0, fin) : desde;
        }
        return mensajeCompleto;
    }
}