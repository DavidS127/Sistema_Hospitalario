package com.hospital.controller;

import com.hospital.model.Paciente;
import com.hospital.repository.PacienteRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepo;

    public PacienteController(PacienteRepository pacienteRepo) {
        this.pacienteRepo = pacienteRepo;
    }

    // GET /pacientes
    // Lista todos los pacientes activos
    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteRepo.findAll();
    }

    // GET /pacientes/1
    // Buscar paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPaciente(@PathVariable int id) {
        return pacienteRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /pacientes
    // Crear paciente
    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity
                .status(201)
                .body(pacienteRepo.save(paciente));
    }

    // PUT /pacientes/1
    // Actualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarPaciente(
            @PathVariable int id,
            @RequestBody Paciente paciente) {

        return pacienteRepo.update(id, paciente)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE /pacientes/1
    // Desactivar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable int id) {
        return pacienteRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}