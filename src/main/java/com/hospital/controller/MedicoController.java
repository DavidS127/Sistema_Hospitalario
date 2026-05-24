package com.hospital.controller;

import com.hospital.model.dto.MedicoDTO;
import com.hospital.repository.MedicoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoRepository medicoRepo;

    public MedicoController(
            MedicoRepository medicoRepo) {

        this.medicoRepo = medicoRepo;
    }

    // GET /medicos -> Listar todos los médicos
    @GetMapping
    public List<MedicoDTO> listarMedicos() {

        return medicoRepo.findAll();
    }

    // GET /medicos/{id} -> Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarMedico(
            @PathVariable int id) {

        return medicoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /medicos -> Crear
    @PostMapping
    public ResponseEntity<MedicoDTO> crearMedico(
            @RequestBody MedicoDTO medico) {

        return ResponseEntity
                .status(201)
                .body(medicoRepo.save(medico));
    }

    // PUT /medicos/{id} -> Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarMedico(
            @PathVariable int id,
            @RequestBody MedicoDTO medico) {

        return medicoRepo.update(id, medico)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE /medicos/{id} -> "Eliminar" (cambiar estado a 'INACTIVO')
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(
            @PathVariable int id) {

        return medicoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}