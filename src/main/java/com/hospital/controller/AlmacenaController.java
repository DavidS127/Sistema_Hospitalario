package com.hospital.controller;

import com.hospital.model.Almacena;
import com.hospital.repository.AlmacenaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/almacena")
public class AlmacenaController {

    private final AlmacenaRepository almacenaRepo;

    public AlmacenaController(
            AlmacenaRepository almacenaRepo) {

        this.almacenaRepo = almacenaRepo;
    }

    // GET
    @GetMapping
    public List<Almacena> listar() {

        return almacenaRepo.findAll();
    }

    // GET BY IDS
    @GetMapping("/{idFarmacia}/{idMedicamento}")
    public ResponseEntity<Almacena> buscar(
            @PathVariable int idFarmacia,
            @PathVariable int idMedicamento) {

        return almacenaRepo.findByIds(idFarmacia, idMedicamento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<Almacena> crear(
            @RequestBody Almacena almacena) {

        return ResponseEntity
                .status(201)
                .body(almacenaRepo.save(almacena));
    }

    // PUT
    @PutMapping("/{idFarmacia}/{idMedicamento}")
    public ResponseEntity<Void> actualizar(
            @PathVariable int idFarmacia,
            @PathVariable int idMedicamento,
            @RequestBody Almacena almacena) {

        return almacenaRepo.update(
                idFarmacia,
                idMedicamento,
                almacena)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{idFarmacia}/{idMedicamento}")
    public ResponseEntity<Void> eliminar(
            @PathVariable int idFarmacia,
            @PathVariable int idMedicamento) {

        return almacenaRepo.delete(
                idFarmacia,
                idMedicamento)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}