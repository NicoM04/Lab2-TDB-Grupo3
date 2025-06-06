package com.tbd.backend.Controller;

import com.tbd.backend.DTO.SectorDTO;
import com.tbd.backend.Entity.Sector;
import com.tbd.backend.Service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sector")
@CrossOrigin("*")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    // Crear un nuevo sector
    @PostMapping("/create")
    public ResponseEntity<Sector> crearSector(@RequestBody Sector sector) {
        Sector creado = sectorService.crearSector(sector);
        return ResponseEntity.ok(creado);
    }

    // Obtener todos los sectores (devuelve lista de SectorDTO)
    @GetMapping("/getAll")
    public ResponseEntity<List<SectorDTO>> obtenerTodos() {
        List<SectorDTO> sectores = sectorService.obtenerTodos();
        return ResponseEntity.ok(sectores);
    }

    // Obtener un sector por id (devuelve SectorDTO)
    @GetMapping("/getById/{id}")
    public ResponseEntity<SectorDTO> obtenerPorId(@PathVariable Long id) {
        Optional<SectorDTO> sectorOpt = sectorService.obtenerPorId(id);
        return sectorOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un sector existente
    @PutMapping("/{id}")
    public ResponseEntity<Sector> actualizarSector(@PathVariable Long id, @RequestBody Sector sectorActualizado) {
        try {
            Sector actualizado = sectorService.actualizarSector(id, sectorActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un sector por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSector(@PathVariable Long id) {
        try {
            sectorService.eliminarSector(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
