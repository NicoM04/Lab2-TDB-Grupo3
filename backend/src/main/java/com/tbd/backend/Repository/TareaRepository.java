package com.tbd.backend.Repository;

import com.tbd.backend.Entity.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // Obtener todas las tareas de un usuario específico
    List<Tarea> findByUsuarioId(Long usuarioId);

    // Obtener todas las tareas de un sector específico
    List<Tarea> findBySectorId(Long sectorId);

    // Obtener todas las tareas completadas o no completadas
    List<Tarea> findByCompletada(Boolean completada);

    // Buscar por palabra clave en nombre o descripción (ignora mayúsculas/minúsculas)
    List<Tarea> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion);

    // Buscar por estado y palabra clave combinados
    List<Tarea> findByCompletadaAndNombreContainingIgnoreCaseOrCompletadaAndDescripcionContainingIgnoreCase(
            Boolean completada1, String nombre, Boolean completada2, String descripcion);

    // Tareas que vencen entre ahora y cierta fecha futura, y que aún no están completadas
    List<Tarea> findByFechaTerminoBetweenAndCompletada(Date desde, Date hasta, Boolean completada);

    List<Tarea> findByUsuarioIdAndCompletada(Long usuarioId, Boolean completada);

    List<Tarea> findByUsuarioIdAndNombreContainingIgnoreCaseOrUsuarioIdAndDescripcionContainingIgnoreCase(
            Long usuarioId, String nombre, Long usuarioId2, String descripcion
    );

    List<Tarea> findByUsuarioIdAndCompletadaAndNombreContainingIgnoreCaseOrUsuarioIdAndCompletadaAndDescripcionContainingIgnoreCase(
            Long usuarioId, Boolean completada, String nombre, Long usuarioId2, Boolean completada2, String descripcion
    );

    Page<Tarea> findByCompletadaAndNombreContainingIgnoreCaseOrCompletadaAndDescripcionContainingIgnoreCase(
            Boolean c1, String nombre,
            Boolean c2, String descripcion,
            Pageable pageable
    );

    Page<Tarea> findByUsuarioId(Long usuarioId, Pageable pageable);

    Page<Tarea> findBySectorId(Long sectorId, Pageable pageable);

    Page<Tarea> findByCompletada(Boolean completada, Pageable pageable);

    Page<Tarea> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion, Pageable pageable);

    // ------------------------------ PREGUNTAS -------------------------

    //¿Cuántas tareas ha hecho el usuario por sector?
    @Query(value = """
    SELECT s.nombre AS sector, COUNT(*) AS cantidad
    FROM tarea t
    JOIN sector s ON t.sector_id = s.id
    WHERE t.usuario_id = :usuarioId AND t.completada = TRUE
    GROUP BY s.nombre
""", nativeQuery = true)
    List<Map<String, Object>> contarTareasPorSectorUsuario(@Param("usuarioId") Long usuarioId);

    //Tareas pendientes asociadas a un usuario
    List<Tarea> findByUsuarioIdAndCompletadaFalse(Long usuarioId);


    //¿Cuál es el sector con más tareas completadas en un radio de 2 kilómetros del usuario?
    @Query(value = """
    SELECT s.nombre, COUNT(*) AS cantidad, ST_Distance(s.ubicacion::geography, u.ubicacion::geography) AS distancia
    FROM tarea t
    JOIN sector s ON t.sector_id = s.id
    JOIN usuario u ON u.id = :usuarioId
    WHERE t.completada = TRUE
      AND ST_DWithin(s.ubicacion::geography, u.ubicacion::geography, 2000)
    GROUP BY s.nombre, s.ubicacion, u.ubicacion
    ORDER BY cantidad DESC, ABS(ST_Distance(s.ubicacion::geography, u.ubicacion::geography) - 2000) ASC
    LIMIT 1
    """, nativeQuery = true)
    Map<String, Object> sectorConMasTareasEnRadio(@Param("usuarioId") Long usuarioId);


    //¿Cuál es el promedio de distancia de las tareas completadas respecto a la
    //ubicación del usuario?
    @Query(value = """
    SELECT AVG(ST_Distance(s.ubicacion::geography, u.ubicacion::geography)) AS promedio_distancia
    FROM tarea t
    JOIN sector s ON t.sector_id = s.id
    JOIN usuario u ON u.id = :usuarioId
    WHERE t.completada = TRUE
""", nativeQuery = true)
    Double promedioDistanciaTareasCompletadas(@Param("usuarioId") Long usuarioId);


    //¿En qué sectores geográficos se concentran la mayoría de las tareas
    //pendientes? (utilizando agrupación espacial).
    @Query(value = """
    WITH sectores_agrupados AS (
        SELECT 
            s.id AS sector_id,
            s.nombre AS sector_nombre,
            s.ubicacion AS geom
        FROM tarea t
        JOIN sector s ON s.id = t.sector_id
        WHERE t.completada = FALSE
    ), 
    grupos AS (
        SELECT 
            unnest(ST_ClusterWithin(geom, 2000)) AS geom_grupo
        FROM sectores_agrupados
    ), 
    numerados AS (
        SELECT 
            ROW_NUMBER() OVER () AS grupo_id,
            geom_grupo
        FROM grupos
    ), 
    asignados AS (
        SELECT 
            s.sector_id,
            s.sector_nombre,
            t.id AS tarea_id,
            n.grupo_id
        FROM sectores_agrupados s
        JOIN tarea t ON t.sector_id = s.sector_id AND t.completada = FALSE
        JOIN numerados n ON ST_Intersects(s.geom, n.geom_grupo)
    )
    SELECT 
        a.grupo_id,
        COUNT(a.tarea_id) AS total_tareas_pendientes,
        array_agg(DISTINCT a.sector_nombre) AS sectores
    FROM asignados a
    GROUP BY a.grupo_id
    ORDER BY total_tareas_pendientes DESC
""", nativeQuery = true)
    List<Map<String, Object>> sectoresConMasTareasPendientes();

    //¿Cuál es la tarea más cercana al usuario (que esté pendiente)?
    @Query(value = """
    SELECT t.*
    FROM tarea t
    JOIN sector s ON t.sector_id = s.id
    JOIN usuario u ON t.usuario_id = u.id
    WHERE t.completada = FALSE AND u.id = :usuarioId
    ORDER BY ST_Distance(u.ubicacion::geography, s.ubicacion::geography)
    LIMIT 1
""", nativeQuery = true)
    Tarea tareaPendienteMasCercana(@Param("usuarioId") Long usuarioId);

    //¿Cuántas tareas ha realizado cada usuario por sector?
    @Query(value = """
    SELECT u.username AS usuario, s.nombre AS sector, COUNT(*) AS total_tareas
    FROM tarea t
    JOIN usuario u ON t.usuario_id = u.id
    JOIN sector s ON t.sector_id = s.id
    WHERE t.completada = TRUE
    GROUP BY u.username, s.nombre
    ORDER BY u.username, s.nombre
""", nativeQuery = true)
    List<Map<String, Object>> contarTareasPorUsuarioYSector();

    //¿Cuál es el sector con más tareas completadas dentro de un radio de 5 km
    //desde la ubicación del usuario?
    @Query(value = """
    SELECT s.nombre AS sector, COUNT(*) AS total_tareas
    FROM tarea t
    JOIN sector s ON t.sector_id = s.id
    JOIN usuario u ON u.id = :usuarioId
    WHERE t.completada = TRUE
      AND ST_DWithin(s.ubicacion::geography, u.ubicacion::geography, 5000)
    GROUP BY s.nombre, s.ubicacion, u.ubicacion
    ORDER BY total_tareas DESC, MIN(ST_Distance(s.ubicacion::geography, u.ubicacion::geography)) ASC
    LIMIT 1
""", nativeQuery = true)
    Map<String, Object> sectorConMasCompletadasEnRadio5km(@Param("usuarioId") Long usuarioId);

    //¿Cuál es el promedio de distancia entre las tareas completadas y el punto
    //registrado del usuario?
    @Query(value = """
    SELECT AVG(ST_Distance(s.ubicacion::geography, u.ubicacion::geography)) AS promedio_distancia
    FROM tarea t
    JOIN sector s ON t.sector_id = s.id
    JOIN usuario u ON u.id = :usuarioId
    WHERE t.completada = TRUE
      AND t.usuario_id = :usuarioId
""", nativeQuery = true)
    Double promedioDistanciaTodasCompletadasAUsuario(@Param("usuarioId") Long usuarioId);

}
