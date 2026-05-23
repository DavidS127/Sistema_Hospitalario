package com.hospital.controller;

import com.hospital.model.Departamento;
import com.hospital.repository.DepartamentoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoRepository departamentoRepo;

    public DepartamentoController(
            DepartamentoRepository departamentoRepo) {

        this.departamentoRepo = departamentoRepo;
    }

    // GET
    @GetMapping
    public List<Departamento> listar() {

        return departamentoRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> buscar(
            @PathVariable int id) {

        return departamentoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Departamento> crear(
            @RequestBody Departamento departamento) {

        return ResponseEntity
                .status(201)
                .body(departamentoRepo.save(departamento));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Departamento departamento) {

        return departamentoRepo.update(id, departamento)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return departamentoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}