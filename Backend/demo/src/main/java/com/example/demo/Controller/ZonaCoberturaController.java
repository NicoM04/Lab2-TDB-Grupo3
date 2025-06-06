package com.example.demo.Controller;

import com.example.demo.Entity.ZonaCobertura;
import com.example.demo.Service.ZonaCoberturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zonas")
@CrossOrigin("*")
public class ZonaCoberturaController {

    @Autowired
    private ZonaCoberturaService zonaCoberturaService;

    // Crear una nueva zona
    @PostMapping("/create")
    public ResponseEntity<ZonaCobertura> crearZona(@RequestBody ZonaCobertura zona) {
        ZonaCobertura creada = zonaCoberturaService.crear(zona);
        return ResponseEntity.ok(creada);
    }

    // Obtener todas las zonas con paginación
    @GetMapping("/getAll")
    public ResponseEntity<List<ZonaCobertura>> obtenerZonas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ZonaCobertura> zonas = zonaCoberturaService.getAll(page, size);
        return zonas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(zonas);
    }

    // Obtener una zona por su ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<ZonaCobertura> obtenerZonaPorId(@PathVariable Integer id) {
        ZonaCobertura zona = zonaCoberturaService.getById(id);
        return zona != null ? ResponseEntity.ok(zona) : ResponseEntity.notFound().build();
    }

    // Actualizar una zona por ID
    @PutMapping("/update/{id}")
    public ResponseEntity<String> actualizarZona(@PathVariable Integer id, @RequestBody ZonaCobertura zona) {
        String resultado = zonaCoberturaService.update(zona, id);
        return resultado.contains("correctamente") ? ResponseEntity.ok(resultado)
                : ResponseEntity.badRequest().body(resultado);
    }

    // Eliminar una zona por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarZona(@PathVariable Integer id) {
        zonaCoberturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
