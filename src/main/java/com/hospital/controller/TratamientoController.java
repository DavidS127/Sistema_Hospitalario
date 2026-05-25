package com.hospital.controller;

import com.hospital.model.Tratamiento;
import com.hospital.repository.TratamientoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tratamiento")
public class TratamientoController {

    private final TratamientoRepository tratamientoRepo;

    public TratamientoController(
            TratamientoRepository tratamientoRepo) {

        this.tratamientoRepo = tratamientoRepo;
    }

    // GET
    @GetMapping
    public List<Tratamiento> listar() {

        return tratamientoRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Tratamiento> buscar(
            @PathVariable int id) {

        return tratamientoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Tratamiento> crear(
            @RequestBody Tratamiento tratamiento) {

        return ResponseEntity
                .status(201)
                .body(tratamientoRepo.save(tratamiento));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Tratamiento tratamiento) {

        return tratamientoRepo.update(id, tratamiento)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return tratamientoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}