package com.hospital.controller;

import com.hospital.model.Equipamiento;
import com.hospital.repository.EquipamientoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipamientos")
public class EquipamientoController {

    private final EquipamientoRepository equipamientoRepo;

    public EquipamientoController(
            EquipamientoRepository equipamientoRepo) {

        this.equipamientoRepo = equipamientoRepo;
    }

    // GET
    @GetMapping
    public List<Equipamiento> listar() {

        return equipamientoRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Equipamiento> buscar(
            @PathVariable int id) {

        return equipamientoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Equipamiento> crear(
            @RequestBody Equipamiento equipamiento) {

        return ResponseEntity
                .status(201)
                .body(equipamientoRepo.save(equipamiento));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Equipamiento equipamiento) {

        return equipamientoRepo.update(id, equipamiento)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE -> fuera_servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return equipamientoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}