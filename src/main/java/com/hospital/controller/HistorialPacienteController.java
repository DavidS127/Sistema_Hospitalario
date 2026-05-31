package com.hospital.controller;
 
import com.hospital.model.dto.HistorialPaciente;
import com.hospital.repository.HistorialPacienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
/**
 * CONTROLLER: HistorialPacienteController
 *
 * PROPÓSITO:
 *   Expone el historial clínico completo de un paciente consultando
 *   la vista vw_historial_paciente. Solo lectura.
 *
 * BASE: /vistas/historial
 */
@RestController
@RequestMapping("/vistas/historial")
public class HistorialPacienteController {
 
    private final HistorialPacienteRepository repo;
 
    public HistorialPacienteController(HistorialPacienteRepository repo) {
        this.repo = repo;
    }
 
    // GET /vistas/historial/{idPaciente}
    // Historial clínico completo de un paciente
    @GetMapping("/{idPaciente}")
    public ResponseEntity<List<HistorialPaciente>> historialCompleto(
            @PathVariable int idPaciente) {
 
        List<HistorialPaciente> lista = repo.findByPaciente(idPaciente);
        return lista.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(lista);
    }
}