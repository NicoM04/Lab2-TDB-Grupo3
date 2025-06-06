package com.example.demo.Repository;

import com.example.demo.Entity.ProductoServicio;
import com.example.demo.DTO.ProductoMasPedidoDTO;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductoServicioRepositoryImp implements ProductoServicioRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public ProductoServicio crear(ProductoServicio producto) {
        String sql = "INSERT INTO ProductoServicio (nombre_producto, descripcion, categoria, precio_unitario, stock) " +
                "VALUES (:nombre_producto, :descripcion, :categoria, :precio_unitario, :stock)";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("nombre_producto", producto.getNombre_producto())
                    .addParameter("descripcion", producto.getDescripcion())
                    .addParameter("categoria", producto.getCategoria())
                    .addParameter("precio_unitario", producto.getPrecio_unitario())
                    .addParameter("stock", producto.getStock())
                    .executeUpdate();
            return producto;
        }
    }

    @Override
    public List<ProductoServicio> getAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM ProductoServicio LIMIT :size OFFSET :offset";
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(ProductoServicio.class);
        }
    }

    @Override
    public ProductoServicio getById(Integer id) {
        String sql = "SELECT * FROM ProductoServicio WHERE id_producto = :id_producto";
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_producto", id)
                    .executeAndFetchFirst(ProductoServicio.class); // Devuelve el primer producto encontrado
        }
    }


    @Override
    public String update(ProductoServicio producto, Integer id) {
        String sql = "UPDATE ProductoServicio SET nombre_producto = :nombre_producto, descripcion = :descripcion, " +
                "categoria = :categoria, precio_unitario = :precio_unitario, stock = :stock WHERE id_producto = :id_producto";
        try (var con = sql2o.open()) {
            int result = con.createQuery(sql)
                    .addParameter("nombre_producto", producto.getNombre_producto())
                    .addParameter("descripcion", producto.getDescripcion())
                    .addParameter("categoria", producto.getCategoria())
                    .addParameter("precio_unitario", producto.getPrecio_unitario())
                    .addParameter("stock", producto.getStock())
                    .addParameter("id_producto", id)
                    .executeUpdate()
                    .getResult();
            return result > 0 ? "Producto actualizado exitosamente" : "No se encontró el producto";
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM ProductoServicio WHERE id_producto = :id_producto";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_producto", id)
                    .executeUpdate();
        }
    }

    //CONSULTA COMPLEJA SQL 2)
    @Override
    public List<ProductoMasPedidoDTO> obtenerMasPedidosPorCategoriaUltimoMes(int page, int size) {
        int offset = (page - 1) * size;
        String sql = """
        WITH ProductoPedidos AS (
            SELECT ps.categoria,
                   ps.nombre_producto,
                   SUM(dp.cantidad) AS total_pedidos
            FROM Pedido p
            JOIN Detalle_de_pedido dp ON p.id_pedido = dp.id_pedido
            JOIN ProductoServicio ps ON dp.id_producto = ps.id_producto
            WHERE p.fecha_pedido >= (CURRENT_DATE - INTERVAL '1 month')
            GROUP BY ps.categoria, ps.nombre_producto
        )
        SELECT categoria, nombre_producto, total_pedidos
        FROM (
            SELECT categoria, nombre_producto, total_pedidos,
                   RANK() OVER (PARTITION BY categoria ORDER BY total_pedidos DESC) AS rank
            FROM ProductoPedidos
        ) AS ranked
        WHERE rank = 1
        ORDER BY categoria
        LIMIT :size OFFSET :offset;
    """;
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(ProductoMasPedidoDTO.class);
        }
    }




}
