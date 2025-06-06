package com.example.demo.Service;

import com.example.demo.Entity.Notificacion;
import com.example.demo.Repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    // Crear nueva notificación
    public Notificacion crearNotificacion(Notificacion notificacion) {
        return notificacionRepository.crear(notificacion);
    }

    // Obtener todas las notificaciones
    public List<Notificacion> obtenerNotificaciones(int page, int size) {
        return notificacionRepository.getAll(page, size);
    }

    // Actualizar notificación
    public String actualizarNotificacion(Notificacion notificacion, Integer id) {
        return notificacionRepository.update(notificacion, id);
    }

    // Eliminar notificación
    public void eliminarNotificacion(Integer id) {
        notificacionRepository.delete(id);
    }
}
