package com.hospital.controller;

import com.hospital.model.Medicamento;
import com.hospital.repository.MedicamentoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    private final MedicamentoRepository medicamentoRepo;

    public MedicamentoController(
            MedicamentoRepository medicamentoRepo) {

        this.medicamentoRepo = medicamentoRepo;
    }

    // GET /medicamentos
    @GetMapping
    public List<Medicamento> listar() {

        return medicamentoRepo.findAll();
    }

    // GET BY ID /medicamentos/1
    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> buscar(
            @PathVariable int id) {

        return medicamentoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /medicamentos
    @PostMapping
    public ResponseEntity<Medicamento> crear(
            @RequestBody Medicamento medicamento) {

        return ResponseEntity
                .status(201)
                .body(medicamentoRepo.save(medicamento));
    }

    // PUT /medicamentos/1
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Medicamento medicamento) {

        return medicamentoRepo.update(id, medicamento)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE /medicamentos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return medicamentoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}