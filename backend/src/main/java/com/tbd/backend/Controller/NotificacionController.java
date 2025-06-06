package com.tbd.backend.Controller;

import com.tbd.backend.Entity.Notificacion;
import com.tbd.backend.Service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin("*")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // Crear notificación
    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@RequestBody Notificacion notificacion) {
        Notificacion creada = notificacionService.crearNotificacion(notificacion);
        return ResponseEntity.ok(creada);
    }

    // Obtener todas las notificaciones
    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodas() {
        List<Notificacion> notificaciones = notificacionService.obtenerTodas();
        return ResponseEntity.ok(notificaciones);
    }

    // Obtener notificación por id
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        Optional<Notificacion> notificacion = notificacionService.obtenerPorId(id);
        return notificacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    // Marcar notificación como leída
    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id) {
        Notificacion notificacion = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(notificacion);
    }

    // Listar notificaciones por usuario
    @GetMapping("/getNotificacionByUsuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    // Generar notificaciones próximas (manual trigger, opcional)
    @PostMapping("/generarNotificaciones")
    public ResponseEntity<Void> generarNotificacionesProximas() {
        notificacionService.generarNotificacionesProximas();
        return ResponseEntity.ok().build();
    }
}
