package com.example.demo.Repository;

import com.example.demo.Entity.DetalleDePedido;
import com.example.demo.Repository.DetalleDePedidoRepository;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DetalleDePedidoRepositoryImp implements DetalleDePedidoRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public DetalleDePedido crear(DetalleDePedido detalle) {
        String sql = "INSERT INTO Detalle_de_pedido (id_producto, id_pedido, cantidad, subtotal) " +
                "VALUES (:id_producto, :id_pedido, :cantidad, :subtotal)";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_producto", detalle.getId_producto())
                    .addParameter("id_pedido", detalle.getId_pedido())
                    .addParameter("cantidad", detalle.getCantidad())
                    .addParameter("subtotal", detalle.getSubtotal())
                    .executeUpdate();
            return detalle;
        }
    }

    @Override
    public List<DetalleDePedido> getAll(int page, int size) {
        // Calculamos el offset basado en el número de página y tamaño de página
        int offset = (page - 1) * size;

        // Consulta SQL con LIMIT y OFFSET
        String sql = "SELECT * FROM Detalle_de_pedido LIMIT :size OFFSET :offset";

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(DetalleDePedido.class);
        }
    }


    @Override
    public DetalleDePedido getById(Integer id) {
        String sql = "SELECT * FROM Detalle_de_pedido WHERE id_detalle = :id_detalle";
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_detalle", id)
                    .executeAndFetchFirst(DetalleDePedido.class);  // Devuelve solo el primer resultado o null si no hay coincidencias
        }
    }

    @Override
    public String update(DetalleDePedido detalle, Integer id) {
        String sql = "UPDATE Detalle_de_pedido SET id_producto = :id_producto, id_pedido = :id_pedido, " +
                "cantidad = :cantidad, subtotal = :subtotal WHERE id_detalle = :id_detalle";
        try (var con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("id_producto", detalle.getId_producto())
                    .addParameter("id_pedido", detalle.getId_pedido())
                    .addParameter("cantidad", detalle.getCantidad())
                    .addParameter("subtotal", detalle.getSubtotal())
                    .addParameter("id_detalle", id)
                    .executeUpdate()
                    .getResult();

            return affectedRows > 0 ? "Detalle actualizado exitosamente" : "No se encontró el detalle para actualizar";
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Detalle_de_pedido WHERE id_detalle = :id_detalle";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_detalle", id)
                    .executeUpdate();
        }
    }
}
