package com.example.demo.Repository;

import com.example.demo.Entity.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class NotificacionRepositoryImp implements NotificacionRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public Notificacion crear(Notificacion notificacion) {
        try (Connection conn = sql2o.open()) {
            String sql = "INSERT INTO notificacion (id_pedido, fecha_creacion, mensaje, tipo, leida, descripcion) " +
                    "VALUES (:id_pedido, :fecha_creacion, :mensaje, :tipo, :leida, :descripcion)";
            conn.createQuery(sql)
                    .addParameter("id_pedido", notificacion.getId_pedido())
                    .addParameter("fecha_creacion", notificacion.getFecha_creacion())
                    .addParameter("mensaje", notificacion.getMensaje())
                    .addParameter("tipo", notificacion.getTipo())
                    .addParameter("leida", notificacion.getLeida())
                    .addParameter("descripcion", notificacion.getDescripcion())
                    .executeUpdate();
            return notificacion;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Notificacion> getAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM notificacion LIMIT :size OFFSET :offset";

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(Notificacion.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Override
    public String update(Notificacion notificacion, Integer id) {
        try (Connection conn = sql2o.open()) {
            String sql = "UPDATE notificacion SET id_pedido = :id_pedido, fecha_creacion = :fecha_creacion, mensaje = :mensaje, " +
                    "tipo = :tipo, leida = :leida, descripcion = :descripcion WHERE id_notificacion = :id";
            conn.createQuery(sql)
                    .addParameter("id_pedido", notificacion.getId_pedido())
                    .addParameter("fecha_creacion", notificacion.getFecha_creacion())
                    .addParameter("mensaje", notificacion.getMensaje())
                    .addParameter("tipo", notificacion.getTipo())
                    .addParameter("leida", notificacion.getLeida())
                    .addParameter("descripcion", notificacion.getDescripcion())
                    .addParameter("id", id)
                    .executeUpdate();
            return "Actualizado correctamente";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            String sql = "DELETE FROM notificacion WHERE id_notificacion = :id";
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
