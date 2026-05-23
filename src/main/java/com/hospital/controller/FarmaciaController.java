package com.hospital.controller;

import com.hospital.model.Farmacia;
import com.hospital.repository.FarmaciaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmacias")
public class FarmaciaController {

    private final FarmaciaRepository farmaciaRepo;

    public FarmaciaController(
            FarmaciaRepository farmaciaRepo) {

        this.farmaciaRepo = farmaciaRepo;
    }

    // GET
    @GetMapping
    public List<Farmacia> listar() {

        return farmaciaRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Farmacia> buscar(
            @PathVariable int id) {

        return farmaciaRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Farmacia> crear(
            @RequestBody Farmacia farmacia) {

        return ResponseEntity
                .status(201)
                .body(farmaciaRepo.save(farmacia));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Farmacia farmacia) {

        return farmaciaRepo.update(id, farmacia)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE -> inactiva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return farmaciaRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}