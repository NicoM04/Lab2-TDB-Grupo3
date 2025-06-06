package com.tbd.backend.Repository;

import com.tbd.backend.Entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Para obtener todas las notificaciones de una tarea específica
    Optional<Notificacion> findByTareaId(Long tareaId);

    List<Notificacion> findByTarea_Usuario_Id(Long usuarioId);
}
