package com.hospital.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.DetalleReceta;
import com.hospital.repository.DetalleRecetaRepository;

@RestController
@RequestMapping("/detalle-recetas")
public class DetalleRecetaController {

    private final DetalleRecetaRepository repo;

    public DetalleRecetaController(
            DetalleRecetaRepository repo) {

        this.repo = repo;
    }

    // GET ALL
    @GetMapping
    public List<DetalleReceta> listar() {

        return repo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleReceta> buscar(
            @PathVariable int id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<DetalleReceta> crear(
            @RequestBody DetalleReceta detalle) {

        return ResponseEntity
                .status(201)
                .body(repo.save(detalle));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody DetalleReceta detalle) {

        return repo.update(id, detalle)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return repo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}