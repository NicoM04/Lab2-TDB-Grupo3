package com.example.demo.Repository;

import com.example.demo.DTO.PedidoCercanoDTO;
import com.example.demo.DTO.PedidoCompletoDTO;
import com.example.demo.DTO.PedidoRutaDTO;
import com.example.demo.DTO.ResumenPedidoDTO;
import com.example.demo.Entity.Pedido;
import com.example.demo.Repository.PedidoRepository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class PedidoRepositoryImp implements PedidoRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public Pedido crear(Pedido pedido) {
        String sql = "INSERT INTO Pedido (id_cliente, id_empresa, id_repartidor, id_pago, fecha_pedido, fecha_entrega, estado, urgente) " +
                "VALUES (:id_cliente, :id_empresa, :id_repartidor, :id_pago, :fecha_pedido, :fecha_entrega, :estado, :urgente)";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_cliente", pedido.getId_cliente())
                    .addParameter("id_empresa", pedido.getId_empresa())
                    .addParameter("id_repartidor", pedido.getId_repartidor())
                    .addParameter("id_pago", pedido.getId_pago())
                    .addParameter("fecha_pedido", pedido.getFecha_pedido())
                    .addParameter("fecha_entrega", pedido.getFecha_entrega())
                    .addParameter("estado", pedido.getEstado())
                    .addParameter("urgente", pedido.getUrgente())
                    .executeUpdate();
            return pedido;
        }
    }

    @Override
    public List<Pedido> getAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM Pedido LIMIT :size OFFSET :offset";

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(Pedido.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Override
    public Pedido getById(Integer id) {
        String sql = "SELECT * FROM Pedido WHERE id_pedido = :id_pedido";
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_pedido", id)
                    .executeAndFetchFirst(Pedido.class);  // Devuelve el primer resultado o null si no existe
        }
    }

    @Override
    public String update(Pedido pedido, Integer id) {
        String sql = "UPDATE Pedido SET id_cliente = :id_cliente, id_empresa = :id_empresa, id_repartidor = :id_repartidor, " +
                "id_pago = :id_pago, fecha_pedido = :fecha_pedido, fecha_entrega = :fecha_entrega, estado = :estado, urgente = :urgente " +
                "WHERE id_pedido = :id_pedido";
        try (var con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("id_cliente", pedido.getId_cliente())
                    .addParameter("id_empresa", pedido.getId_empresa())
                    .addParameter("id_repartidor", pedido.getId_repartidor())
                    .addParameter("id_pago", pedido.getId_pago())
                    .addParameter("fecha_pedido", pedido.getFecha_pedido())
                    .addParameter("fecha_entrega", pedido.getFecha_entrega())
                    .addParameter("estado", pedido.getEstado())
                    .addParameter("urgente", pedido.getUrgente())
                    .addParameter("id_pedido", id)
                    .executeUpdate()
                    .getResult();

            return affectedRows > 0 ? "Pedido actualizado exitosamente" : "No se encontró el pedido para actualizar";
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Pedido WHERE id_pedido = :id_pedido";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_pedido", id)
                    .executeUpdate();
        }
    }

    @Override
    public List<ResumenPedidoDTO> obtenerResumenPorCliente(Integer idCliente) {
        String sqlPedidos = """
        SELECT id_pedido, fecha_pedido, estado
        FROM pedido
        WHERE id_cliente = :idCliente
    """;

        String sqlProductos = """
        SELECT p.nombre_producto AS nombreProducto, dp.cantidad, dp.subtotal
        FROM detalle_de_pedido dp
        JOIN ProductoServicio p ON dp.id_producto = p.id_producto
        WHERE dp.id_pedido = :idPedido
    """;


        try (var con = sql2o.open()) {
            var pedidos = con.createQuery(sqlPedidos)
                    .addParameter("idCliente", idCliente)
                    .executeAndFetchTable()
                    .asList();

            List<ResumenPedidoDTO> resumenes = new java.util.ArrayList<>();

            for (var row : pedidos) {
                Integer idPedido = (Integer) row.get("id_pedido");
                String fecha = row.get("fecha_pedido").toString();
                String estado = (String) row.get("estado");

                var productos = con.createQuery(sqlProductos)
                        .addParameter("idPedido", idPedido)
                        .executeAndFetch(com.example.demo.DTO.ProductoCantidadDTO.class);

                double total = productos.stream().mapToDouble(p -> p.getSubtotal()).sum();

                resumenes.add(new ResumenPedidoDTO(idPedido, fecha, estado, total, productos));
            }

            return resumenes;
        }
    }


    //--------------------------------------- PROCEDIMIENTOS ALMACENADOS ------------------------------------------

    //PROCEDIMIENTO ALMACENADO 7)
    public void registrarPedidoCompleto(PedidoCompletoDTO dto) {
        try (Connection conn = sql2o.open()) {
            java.sql.Connection jdbcConn = conn.getJdbcConnection();

            Array productosArray = jdbcConn.createArrayOf("INTEGER", dto.getProductos());
            Array cantidadesArray = jdbcConn.createArrayOf("INTEGER", dto.getCantidades());

            conn.createQuery("CALL registrar_pedido_completo(:idCliente, :idEmpresa, :idRepartidor, :fechaPedido, :fechaEntrega, :estado, :urgente, :metodoPago, :productos, :cantidades)")
                    .addParameter("idCliente", dto.getIdCliente())
                    .addParameter("idEmpresa", dto.getIdEmpresa())
                    .addParameter("idRepartidor", dto.getIdRepartidor())
                    .addParameter("fechaPedido", dto.getFechaPedido())
                    .addParameter("fechaEntrega", dto.getFechaEntrega())
                    .addParameter("estado", dto.getEstado())
                    .addParameter("urgente", dto.getUrgente())
                    .addParameter("metodoPago", dto.getMetodoPago())
                    .addParameter("productos", productosArray)
                    .addParameter("cantidades", cantidadesArray)
                    .executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar pedido completo", e);
        }
    }

    //PROCEDIMIENTO ALMACENADO 8)
    @Override
    public void cambiarEstadoPedido(int idPedido, String nuevoEstado) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("CALL cambiar_estado_pedido(:idPedido, :nuevoEstado)")
                    .addParameter("idPedido", idPedido)
                    .addParameter("nuevoEstado", nuevoEstado)
                    .executeUpdate(); // correcto porque el procedimiento es VOID
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar el estado del pedido", e);
        }
    }

    //PROCEDIMIENTO ALMACENADO 9)
    public void confirmarPedidoYDescontarStock(int idPedido) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("CALL confirmar_pedido_y_descontar_stock(:idPedido)")
                    .addParameter("idPedido", idPedido)
                    .executeUpdate(); // correcto para funciones/procedimientos VOID
        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar el pedido y descontar stock", e);
        }
    }


    // Método para obtener los pedidos por id_cliente
    @Override
    public List<Pedido> getPedidosByCliente(Integer idCliente, int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM Pedido WHERE id_cliente = :id_cliente LIMIT :size OFFSET :offset";

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_cliente", idCliente)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(Pedido.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pedido> getPedidosMasCercanos(Integer idEmpresa, int limite) {
        String sql = """
        SELECT p.*
        FROM pedido p
        JOIN cliente c ON p.id_cliente = c.id_cliente
        JOIN empresas_asociadas e ON p.id_empresa = e.id_empresa
        WHERE e.id_empresa = :idEmpresa
          AND c.ubicacion IS NOT NULL
          AND e.ubicacion IS NOT NULL
        ORDER BY ST_Distance(c.ubicacion, e.ubicacion)
        LIMIT :limite
        """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("idEmpresa", idEmpresa)
                    .addParameter("limite", limite)
                    .executeAndFetch(Pedido.class);
        }
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------

    //1) Encontrar los 5 puntos de entrega más cercanos a una empresa asociada.
    @Override
    public List<PedidoCercanoDTO> getPedidosMasCercanosEmpresa(Integer idEmpresa, int limite) {
        String sql = """
    SELECT 
        p.id_pedido AS idPedido,
        c.id_cliente AS idCliente,
        ea.id_empresa AS idEmpresa,
        ea.nombre_empresa AS nombreEmpresa,
        ST_AsText(ST_EndPoint(p.ruta_estimada)) AS puntoEntrega,
        ST_AsText(ea.ubicacion) AS ubicacionEmpresa,
        ST_Distance(
            ST_EndPoint(p.ruta_estimada)::geography,
            ea.ubicacion::geography
        ) AS distanciaMetros
    FROM pedido p
    JOIN cliente c ON p.id_cliente = c.id_cliente
    JOIN empresas_asociadas ea ON p.id_empresa = ea.id_empresa
    WHERE p.ruta_estimada IS NOT NULL
      AND ea.id_empresa = :idEmpresa
      AND p.estado NOT IN ('Finalizado', 'Cancelado') 
    ORDER BY distanciaMetros
    LIMIT :limite
    """;

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("idEmpresa", idEmpresa)
                    .addParameter("limite", limite)
                    .executeAndFetch(PedidoCercanoDTO.class);
        }
    }

    //4) Identificar el punto de entrega más lejano desde cada empresa asociada.
    @Override
    public List<PedidoCercanoDTO> getPedidosMasLejanosPorEmpresa() {
        String sql = """
    SELECT DISTINCT ON (e.id_empresa)
        p.id_pedido AS idPedido,
        c.id_cliente AS idCliente,
        e.id_empresa AS idEmpresa,
        e.nombre_empresa AS nombreEmpresa,
        ST_AsText(ST_EndPoint(p.ruta_estimada)) AS puntoEntrega,
        ST_AsText(e.ubicacion) AS ubicacionEmpresa,
        ST_Distance(
            e.ubicacion::geography,
            ST_EndPoint(p.ruta_estimada)::geography
        ) AS distanciaMetros
    FROM pedido p
    JOIN cliente c ON p.id_cliente = c.id_cliente
    JOIN empresas_asociadas e ON p.id_empresa = e.id_empresa
    WHERE p.ruta_estimada IS NOT NULL
      AND e.ubicacion IS NOT NULL
      AND p.estado NOT IN ('Finalizado', 'Cancelado')
    ORDER BY e.id_empresa, distanciaMetros DESC
    """;

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(PedidoCercanoDTO.class);
        }
    }

    @Override
    public List<PedidoRutaDTO> getPedidosConMasDeDosZonas() {
        String sql = """
        SELECT 
            p.id_pedido AS idPedido,
            p.id_cliente AS idCliente,
            p.id_empresa AS idEmpresa,
            e.nombre_empresa AS nombreEmpresa,
            ST_AsText(ST_EndPoint(p.ruta_estimada)) AS puntoEntrega,
            ST_AsText(e.ubicacion) AS ubicacionEmpresa,
            ST_Distance(
                ST_EndPoint(p.ruta_estimada)::geography,
                e.ubicacion::geography
            ) AS distanciaMetros,
            COUNT(z.zona_id) AS cantidadZonas
        FROM pedido p
        JOIN zonas_cobertura z ON ST_Intersects(p.ruta_estimada, z.geom)
        JOIN empresas_asociadas e ON p.id_empresa = e.id_empresa
        WHERE p.ruta_estimada IS NOT NULL
        GROUP BY p.id_pedido, p.id_cliente, p.id_empresa, e.nombre_empresa, e.ubicacion, p.ruta_estimada
        HAVING COUNT(z.zona_id) > 2
    """;

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(PedidoRutaDTO.class);
        }
    }


}
