package com.example.demo.Controller;

import com.example.demo.Entity.RutaEstimada;
import com.example.demo.Service.RutaEstimadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ruta")
@CrossOrigin("*")
public class RutaEstimadaController {

    @Autowired
    private RutaEstimadaService rutaEstimadaService;

    // Crear una nueva ruta
    @PostMapping("/create")
    public ResponseEntity<RutaEstimada> crearRuta(@RequestBody RutaEstimada ruta) {
        RutaEstimada creada = rutaEstimadaService.crear(ruta);
        return ResponseEntity.ok(creada);
    }

    // Obtener todas las rutas con paginación
    @GetMapping("/getAll")
    public ResponseEntity<List<RutaEstimada>> obtenerTodas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<RutaEstimada> rutas = rutaEstimadaService.getAll(page, size);
        return rutas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(rutas);
    }

    // Obtener ruta por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<RutaEstimada> obtenerPorId(@PathVariable("id") Integer id) {
        RutaEstimada ruta = rutaEstimadaService.getById(id);
        return ruta != null ? ResponseEntity.ok(ruta) : ResponseEntity.notFound().build();
    }

    // Obtener ruta por ID de pedido
    @GetMapping("/getByPedido/{idPedido}")
    public ResponseEntity<RutaEstimada> obtenerPorPedido(@PathVariable("idPedido") Integer idPedido) {
        RutaEstimada ruta = rutaEstimadaService.getByPedido(idPedido);
        return ruta != null ? ResponseEntity.ok(ruta) : ResponseEntity.notFound().build();
    }

    // Actualizar ruta
    @PutMapping("/update/{id}")
    public ResponseEntity<String> actualizarRuta(@PathVariable("id") Integer id, @RequestBody RutaEstimada ruta) {
        String resultado = rutaEstimadaService.update(ruta, id);
        return resultado.contains("correctamente")
                ? ResponseEntity.ok(resultado)
                : ResponseEntity.badRequest().body(resultado);
    }

    // Eliminar ruta
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarRuta(@PathVariable("id") Integer id) {
        rutaEstimadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
