package com.example.demo.Controller;

import com.example.demo.Entity.Notificacion;
import com.example.demo.Service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")

@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // Crear nueva notificación
    @PostMapping
    public Notificacion crearNotificacion(@RequestBody Notificacion notificacion) {
        return notificacionService.crearNotificacion(notificacion);
    }

    // Obtener todas las notificaciones
    @GetMapping
    public List<Notificacion> obtenerNotificaciones(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return notificacionService.obtenerNotificaciones(page, size);
    }

    // Actualizar notificación
    @PutMapping("/{id}")
    public String actualizarNotificacion(@RequestBody Notificacion notificacion, @PathVariable Integer id) {
        return notificacionService.actualizarNotificacion(notificacion, id);
    }

    // Eliminar notificación
    @DeleteMapping("/{id}")
    public void eliminarNotificacion(@PathVariable Integer id) {
        notificacionService.eliminarNotificacion(id);
    }
}
