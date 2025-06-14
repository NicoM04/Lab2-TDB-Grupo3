package com.example.demo.Repository;

import com.example.demo.DTO.RepartidorDTO;
import com.example.demo.Entity.Repartidor;
//import org.sql2o.Connection;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.geom.Point;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;


@Repository
public class RepartidoresRepositoryImp implements RepartidoresRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public Repartidor crear(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre_repartidor, rut, telefono, fecha_contratacion, activo, cantidad_entregas) " +
                "VALUES (:nombre_repartidor, :rut, :telefono, :fecha_contratacion, :activo, :cantidad_entregas)";
        String wkt = repartidor.getUbicacion_actual() != null ? repartidor.getUbicacion_actual().toText() : null;
        try (var con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .addParameter("nombre_repartidor", repartidor.getNombre_repartidor())
                    .addParameter("rut", repartidor.getRut())
                    .addParameter("telefono", repartidor.getTelefono())
                    .addParameter("fecha_contratacion", repartidor.getFecha_contratacion())
                    .addParameter("activo", repartidor.getActivo())
                    .addParameter("cantidad_entregas", repartidor.getCantidad_entregas())
                    .addParameter("ubicacion", wkt)
                    .executeUpdate()
                    .getKey();
            repartidor.setId_repartidor(id);
            return repartidor;
        }
    }

    @Override
    public List<RepartidorDTO> getAll(int page, int size) {
        int offset = (page - 1) * size;

            String sql = """
        SELECT 
            r.id_repartidor, 
            r.nombre_repartidor, 
            r.rut, 
            r.telefono, 
            r.fecha_contratacion, 
            r.activo, 
            r.cantidad_entregas, 
            ST_AsText(r.ubicacion_actual) AS ubicacion,
            AVG(c.puntuacion) AS puntuacion
        FROM repartidores r
        LEFT JOIN calificaciones c ON r.id_repartidor = c.id_repartidor
        GROUP BY r.id_repartidor, r.nombre_repartidor, r.rut, r.telefono,
                 r.fecha_contratacion, r.activo, r.cantidad_entregas, r.ubicacion_actual
        LIMIT :size OFFSET :offset
        """;


        try (Connection con = sql2o.open()) {
            var query = con.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetchTable();

            List<RepartidorDTO> resultado = new ArrayList<>();
            var reader = new org.locationtech.jts.io.WKTReader();

            for (var row : query.rows()) {
                RepartidorDTO dto = new RepartidorDTO();
                dto.setId_repartidor(row.getInteger("id_repartidor"));
                dto.setNombre_repartidor(row.getString("nombre_repartidor"));
                dto.setRut(row.getString("rut"));
                dto.setTelefono(row.getString("telefono"));
                dto.setActivo(row.getBoolean("activo"));
                dto.setCantidad_entregas(row.getInteger("cantidad_entregas"));

                resultado.add(dto);
            }

            return resultado;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



    @Override
    public RepartidorDTO findById(Integer id) {
        String sql = """
    SELECT 
        r.id_repartidor, 
        r.nombre_repartidor, 
        r.rut, 
        r.telefono, 
        r.fecha_contratacion, 
        r.activo, 
        r.cantidad_entregas, 
        ST_AsText(r.ubicacion_actual) AS ubicacion,
        AVG(c.puntuacion) AS puntuacion
    FROM repartidores r
    LEFT JOIN calificaciones c ON r.id_repartidor = c.id_repartidor
    WHERE r.id_repartidor = :id_repartidor
    GROUP BY r.id_repartidor, r.nombre_repartidor, r.rut, r.telefono,
             r.fecha_contratacion, r.activo, r.cantidad_entregas, r.ubicacion_actual
    """;


        try (var con = sql2o.open()) {
            var row = con.createQuery(sql)
                    .addParameter("id_repartidor", id)
                    .executeAndFetchTable()
                    .rows()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (row == null) return null;

            RepartidorDTO dto = new RepartidorDTO();
            dto.setId_repartidor(row.getInteger("id_repartidor"));
            dto.setNombre_repartidor(row.getString("nombre_repartidor"));
            dto.setRut(row.getString("rut"));
            dto.setTelefono(row.getString("telefono"));
            dto.setActivo(row.getBoolean("activo"));
            dto.setCantidad_entregas(row.getInteger("cantidad_entregas"));
            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String update(Repartidor repartidor, Integer id) {
        String sql = "UPDATE repartidor SET nombre_repartidor = :nombre_repartidor, rut = :rut, telefono = :telefono, " +
                "fecha_contratacion = :fecha_contratacion, activo = :activo, cantidad_entregas = :cantidad_entregas " +
                "WHERE id_repartidor = :id_repartidor";
        try (var con = sql2o.open()) {
            int result = con.createQuery(sql)
                    .addParameter("nombre_repartidor", repartidor.getNombre_repartidor())
                    .addParameter("rut", repartidor.getRut())
                    .addParameter("telefono", repartidor.getTelefono())
                    .addParameter("fecha_contratacion", repartidor.getFecha_contratacion())
                    .addParameter("activo", repartidor.getActivo())
                    .addParameter("cantidad_entregas", repartidor.getCantidad_entregas())
                    .addParameter("id_repartidor", id)
                    .executeUpdate()
                    .getResult();
            return result > 0 ? "Repartidor actualizado correctamente" : "No se encontró el repartidor";
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM repartidor WHERE id_repartidor = :id_repartidor";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_repartidor", id)
                    .executeUpdate();
        }
    }

    //CONSULTA SQL COMPLEJA 4)
    @Override
    public List<Map<String, Object>> obtenerTiempoPromedioEntregaPorRepartidor(int page, int size) {
        int offset = (page - 1) * size;
        String sql = """
        SELECT id_repartidor,
               AVG( fecha_entrega - fecha_pedido) AS tiempo_promedio_dias
        FROM pedido
        WHERE fecha_entrega IS NOT NULL AND fecha_pedido IS NOT NULL
        GROUP BY id_repartidor
        LIMIT :size OFFSET :offset
    """;

        try (var conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            System.out.println("Error al calcular tiempo promedio de entrega: " + e.getMessage());
            return null;
        }
    }


    //CONSULTA SQL COMPLEJA 5)
    @Override
    public List<Map<String, Object>> obtenerTop3RepartidoresConMejorRendimiento() {
        String sql = """
    SELECT r.id_repartidor,
           r.nombre_repartidor,
           r.cantidad_entregas,
           COALESCE(AVG(c.puntuacion), 0) AS promedio_calificacion,
           (r.cantidad_entregas * 0.5 + COALESCE(AVG(c.puntuacion), 0) * 10) AS puntaje_rendimiento
    FROM Repartidores r
    LEFT JOIN Calificaciones c ON r.id_repartidor = c.id_repartidor
    GROUP BY r.id_repartidor, r.nombre_repartidor, r.cantidad_entregas
    ORDER BY puntaje_rendimiento DESC
    LIMIT 3
    """;

        try (var conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            System.out.println("Error al obtener los mejores repartidores: " + e.getMessage());
            return null;
        }
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //3) Calcular la distancia total recorrida por un repartidor en el último mes.
    @Override
    public Double obtenerDistanciaTotalRecorridaEnUltimoMes(int idRepartidor, int ultimosMeses) {
        String sql = """
        SELECT SUM(ST_Length(ruta_estimada::geography)) AS distancia_total_metros
        FROM pedido
        WHERE id_repartidor = :idRepartidor
          AND fecha_entrega IS NOT NULL
          AND fecha_entrega >= (CURRENT_DATE - INTERVAL ':ultimosMeses months')
          AND fecha_entrega <= CURRENT_DATE
          AND estado = 'Finalizado'
    """;

        try (var con = sql2o.open()) {
            // Debido a que no se puede parametrizar directamente el INTERVAL en SQL, reemplazamos :ultimosMeses manualmente
            String sqlFinal = sql.replace(":ultimosMeses", String.valueOf(ultimosMeses));

            Double distanciaTotal = con.createQuery(sqlFinal)
                    .addParameter("idRepartidor", idRepartidor)
                    .executeScalar(Double.class);

            return distanciaTotal != null ? distanciaTotal : 0.0;
        } catch (Exception e) {
            System.err.println("Error al calcular distancia total recorrida: " + e.getMessage());
            return 0.0;
        }
    }

}
