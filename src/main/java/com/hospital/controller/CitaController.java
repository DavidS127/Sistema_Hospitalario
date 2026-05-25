package com.hospital.controller;

import com.hospital.model.Cita;
import com.hospital.repository.CitaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}