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

import com.hospital.model.dto.AdministrativoDTO;
import com.hospital.repository.AdministrativoRepository;

@RestController
@RequestMapping("/administrativos")
public class AdministrativoController {

    private final AdministrativoRepository administrativoRepo;

    public AdministrativoController(
            AdministrativoRepository administrativoRepo) {

        this.administrativoRepo = administrativoRepo;
    }

    // GET /administrativos -> Listar todos los administrativos
    @GetMapping
    public List<AdministrativoDTO> listarAdministrativos() {

        return administrativoRepo.findAll();
    }

    // GET /administrativos/{id} -> Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<AdministrativoDTO> buscarAdministrativo(
            @PathVariable int id) {

        return administrativoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /administrativos -> Crear
    @PostMapping
    public ResponseEntity<AdministrativoDTO> crearAdministrativo(
            @RequestBody AdministrativoDTO administrativo) {

        return ResponseEntity
                .status(201)
                .body(administrativoRepo.save(administrativo));
    }

    // PUT /administrativos/{id} -> Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarAdministrativo(
            @PathVariable int id,
            @RequestBody AdministrativoDTO administrativo) {

        return administrativoRepo.update(id, administrativo)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // DELETE /administrativos/{id} -> "Eliminar" (cambiar estado a 'INACTIVO')
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministrativo(
            @PathVariable int id) {

        return administrativoRepo.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

