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

import com.hospital.model.ConsultaMedica;
import com.hospital.repository.ConsultaMedicaRepository;

@RestController
@RequestMapping("/consultas-medicas")
public class ConsultaMedicaController {

    private final ConsultaMedicaRepository repo;

    public ConsultaMedicaController(
            ConsultaMedicaRepository repo) {

        this.repo = repo;
    }

    // GET ALL
    @GetMapping
    public List<ConsultaMedica> listar() {

        return repo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaMedica> buscar(
            @PathVariable int id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<ConsultaMedica> crear(
            @RequestBody ConsultaMedica consulta) {

        return ResponseEntity
                .status(201)
                .body(repo.save(consulta));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody ConsultaMedica consulta) {

        return repo.update(id, consulta)
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
