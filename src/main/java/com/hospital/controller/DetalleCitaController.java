package com.hospital.controller;
 
import com.hospital.model.dto.DetalleCita;
import com.hospital.repository.DetalleCitaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
/**
 * CONTROLLER: DetalleCitaController
 *
 * PROPÓSITO:
 *   Expone endpoints de solo lectura sobre la vista vw_detalle_cita.
 *   Todos los endpoints son GET porque las vistas no se modifican directamente.
 *
 * BASE: /vistas/citas
 */
@RestController
@RequestMapping("/vistas/citas")
public class DetalleCitaController {
 
    private final DetalleCitaRepository repo;
 
    public DetalleCitaController(DetalleCitaRepository repo) {
        this.repo = repo;
    }
 
    // GET /vistas/citas
    // Todas las citas con su detalle completo
    @GetMapping
    public List<DetalleCita> listarTodas() {
        return repo.findAll();
    }
 
    // GET /vistas/citas/hoy
    // Agenda del día: citas de la fecha actual ordenadas por hora
    @GetMapping("/hoy")
    public List<DetalleCita> citasHoy() {
        return repo.findHoy();
    }
 
    // GET /vistas/citas/medico/{idMedico}
    // Todas las citas de un médico específico
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<DetalleCita>> citasPorMedico(
            @PathVariable int idMedico) {
 
        List<DetalleCita> lista = repo.findByMedico(idMedico);
        return lista.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(lista);
    }
 
    // GET /vistas/citas/paciente/{idPaciente}
    // Todas las citas de un paciente específico
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<DetalleCita>> citasPorPaciente(
            @PathVariable int idPaciente) {
 
        List<DetalleCita> lista = repo.findByPaciente(idPaciente);
        return lista.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(lista);
    }
 
    // GET /vistas/citas/departamento/{idDepartamento}
    // Todas las citas de un departamento
    @GetMapping("/departamento/{idDepartamento}")
    public ResponseEntity<List<DetalleCita>> citasPorDepartamento(
            @PathVariable int idDepartamento) {
 
        List<DetalleCita> lista = repo.findByDepartamento(idDepartamento);
        return lista.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(lista);
    }
}