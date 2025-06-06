package com.tbd.backend.Service;

import com.tbd.backend.Entity.Notificacion;
import com.tbd.backend.Entity.Tarea;
import com.tbd.backend.Repository.NotificacionRepository;
import com.tbd.backend.Repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private TareaRepository tareaRepository;

    // ----------- CRUD básico -----------
    public Notificacion crearNotificacion(Notificacion notificacion) {
        notificacion.setLeida(false);
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> obtenerPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }

    public Notificacion marcarComoLeida(Long id) {
        Notificacion notif = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));
        notif.setLeida(true);
        return notificacionRepository.save(notif);
    }

    // ----------- Generar notificaciones por vencimiento -----------

    //Genera notificaciones para todas las tareas proximas a vencer (para todos los usuarios)
    public void generarNotificacionesProximas() {
        Date hoy = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(Calendar.DAY_OF_YEAR, 1); // tareas que vencen dentro de 1 día
        Date manana = cal.getTime();

        List<Tarea> tareas = tareaRepository.findByFechaTerminoBetweenAndCompletada(hoy, manana, false);

        for (Tarea tarea : tareas) {
            Optional<Notificacion> yaExiste = notificacionRepository.findByTareaId(tarea.getId());
            if (yaExiste.isEmpty()) {
                Notificacion notif = new Notificacion();
                notif.setTarea(tarea);
                notif.setMensaje("La tarea '" + tarea.getNombre() + "' está por vencer.");
                notif.setLeida(false);
                notificacionRepository.save(notif);
            }
        }
    }

    // Listar notificaciones por usuario
    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByTarea_Usuario_Id(usuarioId);
    }
}
