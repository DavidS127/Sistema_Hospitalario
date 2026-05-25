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

import com.hospital.model.Evento;
import com.hospital.repository.EventoRepository;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoRepository repo;

    public EventoController(
            EventoRepository repo) {

        this.repo = repo;
    }

    // GET ALL
    @GetMapping
    public List<Evento> listar() {

        return repo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscar(
            @PathVariable int id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Evento> crear(
            @RequestBody Evento evento) {

        return ResponseEntity
                .status(201)
                .body(repo.save(evento));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Evento evento) {

        return repo.update(id, evento)
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