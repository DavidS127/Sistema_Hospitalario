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

import com.hospital.model.Receta;
import com.hospital.repository.RecetaRepository;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private final RecetaRepository repo;

    public RecetaController(
            RecetaRepository repo) {

        this.repo = repo;
    }

    // GET ALL
    @GetMapping
    public List<Receta> listar() {

        return repo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Receta> buscar(
            @PathVariable int id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Receta> crear(
            @RequestBody Receta receta) {

        return ResponseEntity
                .status(201)
                .body(repo.save(receta));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Receta receta) {

        return repo.update(id, receta)
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
