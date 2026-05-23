package com.hospital.controller;

import com.hospital.model.HistoriaClinica;
import com.hospital.repository.HistoriaClinicaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historias-clinicas")
public class HistoriaClinicaController {

    private final HistoriaClinicaRepository historiaRepo;

    public HistoriaClinicaController(
            HistoriaClinicaRepository historiaRepo) {

        this.historiaRepo = historiaRepo;
    }

    // GET
    @GetMapping
    public List<HistoriaClinica> listar() {

        return historiaRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinica> buscar(
            @PathVariable int id) {

        return historiaRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<HistoriaClinica> crear(
            @RequestBody HistoriaClinica historia) {

        return ResponseEntity
                .status(201)
                .body(historiaRepo.save(historia));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody HistoriaClinica historia) {

        return historiaRepo.update(id, historia)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE -> archivada
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return historiaRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}