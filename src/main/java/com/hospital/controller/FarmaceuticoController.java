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

import com.hospital.model.dto.FarmaceuticoDTO;
import com.hospital.repository.FarmaceuticoRepository;

@RestController
@RequestMapping("/farmaceuticos")
public class FarmaceuticoController {

    private final FarmaceuticoRepository farmaceuticoRepo;

    public FarmaceuticoController(
            FarmaceuticoRepository farmaceuticoRepo) {

        this.farmaceuticoRepo = farmaceuticoRepo;
    }

    // GET /farmaceuticos -> Listar todos los farmaceuticos
    @GetMapping
    public List<FarmaceuticoDTO> listarFarmaceuticos() {

        return farmaceuticoRepo.findAll();
    }

    // GET /farmaceuticos/{id} -> Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmaceuticoDTO> buscarFarmaceutico(
            @PathVariable int id) {

        return farmaceuticoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /farmaceuticos -> Crear
    @PostMapping
    public ResponseEntity<FarmaceuticoDTO> crearFarmaceutico(
            @RequestBody FarmaceuticoDTO farmaceutico) {

        return ResponseEntity
                .status(201)
                .body(farmaceuticoRepo.save(farmaceutico));
    }

    // PUT /farmaceuticos/{id} -> Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarFarmaceutico(
            @PathVariable int id,
            @RequestBody FarmaceuticoDTO farmaceutico) {

        return farmaceuticoRepo.update(id, farmaceutico)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE /farmaceuticos/{id} -> "Eliminar" (cambiar estado a 'INACTIVO')
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFarmaceutico(
            @PathVariable int id) {

        return farmaceuticoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}