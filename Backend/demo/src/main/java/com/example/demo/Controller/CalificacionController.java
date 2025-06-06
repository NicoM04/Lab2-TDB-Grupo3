package com.example.demo.Controller;

import com.example.demo.Entity.Calificacion;
import com.example.demo.Entity.DetalleDePedido;
import com.example.demo.Service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")


@RequestMapping("/calificacion")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/create")
    public Calificacion crearCalificacion(@RequestBody Calificacion calificacion) {
        return calificacionService.crear(calificacion);
    }

    @GetMapping("/getAll")
    public List<Calificacion> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return calificacionService.getAll(page, size);
   }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Calificacion> obtenerPorId(@PathVariable("id") Integer id) {
        Calificacion calificacion = calificacionService.findById(id);
        if (calificacion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra el calificacion
        }
        return new ResponseEntity<>(calificacion, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public String actualizarCalificacion(@RequestBody Calificacion calificacion, @PathVariable Integer id) {
        return calificacionService.update(calificacion, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarCalificacion(@PathVariable Integer id) {
        calificacionService.delete(id);
    }

    // Endpoint para obtener todas las calificaciones de un repartidor por su id
    @GetMapping("/getAllByRepartidor/{idRepartidor}")
    public ResponseEntity<List<Calificacion>> getCalificacionesByRepartidorId(@PathVariable("idRepartidor") Integer idRepartidor) {
        List<Calificacion> calificaciones = calificacionService.getCalificacionesByRepartidorId(idRepartidor);

        if (calificaciones.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay calificaciones, retorna un código 204
        }

        return ResponseEntity.ok(calificaciones); // Retorna las calificaciones con un código 200
    }
}
