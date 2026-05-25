package com.hospital.controller;

import com.hospital.model.Procedimiento;
import com.hospital.repository.ProcedimientoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedimiento")
public class ProcedimientoController {

    private final ProcedimientoRepository procedimientoRepo;

    public ProcedimientoController(
            ProcedimientoRepository procedimientoRepo) {

        this.procedimientoRepo = procedimientoRepo;
    }

    // GET
    @GetMapping
    public List<Procedimiento> listar() {

        return procedimientoRepo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Procedimiento> buscar(
            @PathVariable int id) {

        return procedimientoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Procedimiento> crear(
            @RequestBody Procedimiento procedimiento) {

        return ResponseEntity
                .status(201)
                .body(procedimientoRepo.save(procedimiento));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int id,
            @RequestBody Procedimiento procedimiento) {

        return procedimientoRepo.update(id, procedimiento)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int id) {

        return procedimientoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}