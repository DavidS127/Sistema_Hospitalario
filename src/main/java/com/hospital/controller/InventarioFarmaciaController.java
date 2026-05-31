package com.hospital.controller;
 
import com.hospital.model.dto.InventarioFarmacia;
import com.hospital.repository.InventarioFarmaciaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
/**
 * CONTROLLER: InventarioFarmaciaController
 *
 * PROPÓSITO:
 *   Expone el inventario de farmacias con alertas de stock consultando
 *   la vista vw_inventario_farmacia. Solo lectura.
 *
 * BASE: /vistas/inventario
 */
@RestController
@RequestMapping("/vistas/inventario")
public class InventarioFarmaciaController {
 
    private final InventarioFarmaciaRepository repo;
 
    public InventarioFarmaciaController(InventarioFarmaciaRepository repo) {
        this.repo = repo;
    }
 
    // GET /vistas/inventario
    // Inventario completo de todas las farmacias
    @GetMapping
    public List<InventarioFarmacia> inventarioCompleto() {
        return repo.findAll();
    }
 
    // GET /vistas/inventario/farmacia/{idFarmacia}
    // Inventario de una farmacia específica
    @GetMapping("/farmacia/{idFarmacia}")
    public ResponseEntity<List<InventarioFarmacia>> inventarioPorFarmacia(
            @PathVariable int idFarmacia) {
 
        List<InventarioFarmacia> lista = repo.findByFarmacia(idFarmacia);
        return lista.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(lista);
    }
 
    // GET /vistas/inventario/criticos
    // Todos los medicamentos con alerta CRITICO o BAJO en cualquier farmacia
    @GetMapping("/criticos")
    public List<InventarioFarmacia> medicamentosCriticos() {
        return repo.findCriticos();
    }
 
    // GET /vistas/inventario/farmacia/{idFarmacia}/criticos
    // Medicamentos críticos o bajos de una farmacia específica
    @GetMapping("/farmacia/{idFarmacia}/criticos")
    public ResponseEntity<List<InventarioFarmacia>> criticosPorFarmacia(
            @PathVariable int idFarmacia) {
 
        List<InventarioFarmacia> lista = repo.findCriticosByFarmacia(idFarmacia);
        return lista.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(lista);
    }
}