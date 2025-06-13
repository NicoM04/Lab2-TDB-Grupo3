package com.example.demo.Repository;

import com.example.demo.DTO.ClienteLejanoDTO;
import com.example.demo.Entity.Cliente;
import com.example.demo.Entity.ZonaCobertura;
import com.example.demo.config.InputVerificationService;
import com.example.demo.config.JwtMiddlewareService;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteRepositoryImp implements ClienteRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public void crear(Cliente cliente) {
        String query = "INSERT INTO Cliente (nombre_cliente, contrasena_cliente, correo_cliente, direccion, telefono, fecha_registro, ubicacion) " +
                "VALUES (:nombre_cliente, :contrasena_cliente, :correo_cliente, :direccion, :telefono, :fecha_registro, ST_GeomFromText(:ubicacion, 4326))";
        try (var con = sql2o.open()) {
            String wkt = cliente.getUbicacion() != null ? cliente.getUbicacion().toText() : null;
            con.createQuery(query)
                    .addParameter("nombre_cliente", cliente.getNombre_cliente())
                    .addParameter("contrasena_cliente", cliente.getContrasena_cliente())
                    .addParameter("correo_cliente", cliente.getCorreo_cliente())
                    .addParameter("direccion", cliente.getDireccion())
                    .addParameter("telefono", cliente.getTelefono())
                    .addParameter("fecha_registro", cliente.getFecha_registro())
                    .addParameter("ubicacion", wkt)
                    .executeUpdate();
        }
    }

    @Override
    public List<Cliente> getAll(int page, int size) {
        String sql = "SELECT * FROM Cliente LIMIT :size OFFSET :offset";
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", (page - 1) * size)
                    .executeAndFetch(Cliente.class);
        }
    }


    @Override
    public String update(Cliente cliente, Integer id) {
        // SQL modificado para actualizar solo los campos que deseas
        String sql = "UPDATE Cliente SET nombre_cliente = :nombre_cliente, telefono = :telefono, correo_cliente = :correo_cliente, direccion = :direccion " +
                "WHERE id_cliente = :id_cliente";
        try (var con = sql2o.open()) {
            // Ejecuta la actualización, pero solo con los parámetros que se deben actualizar
            int affectedRows = con.createQuery(sql)
                    .addParameter("nombre_cliente", cliente.getNombre_cliente())
                    .addParameter("telefono", cliente.getTelefono())  // Solo se actualiza el teléfono
                    .addParameter("correo_cliente", cliente.getCorreo_cliente())  // Solo se actualiza el correo
                    .addParameter("direccion", cliente.getDireccion())  // Solo se actualiza la dirección
                    .addParameter("id_cliente", id)  // Se busca por el ID
                    .executeUpdate()
                    .getResult();

            // Devolver el mensaje correspondiente según el resultado
            return affectedRows > 0 ? "Cliente actualizado exitosamente" : "No se encontró el cliente para actualizar";
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Cliente WHERE id_cliente = :id_cliente";
        try (var con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id_cliente", id)
                    .executeUpdate();
        }
    }

    @Override
    public Cliente findById(Integer id) {
        String sql = "SELECT * FROM Cliente WHERE id_cliente = :id_cliente";
        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_cliente", id)
                    .executeAndFetchFirst(Cliente.class); // Devuelve el primer resultado o null si no existe.
        }
    }

    @Override
    public ResponseEntity<Cliente> findByCorreo(String correo) {
        String sql = """
        SELECT 
            id_cliente, nombre_cliente, contrasena_cliente, correo_cliente, 
            direccion, telefono, fecha_registro,
            ST_AsText(ubicacion) as ubicacion 
        FROM cliente 
        WHERE correo_cliente = :correo
    """;

        try (Connection conn = sql2o.open()) {
            var query = conn.createQuery(sql)
                    .addParameter("correo", correo)
                    .executeAndFetchTable();

            if (query.rows().isEmpty()) {
                return ResponseEntity.status(404).body(null);
            }

            var row = query.rows().get(0);
            Cliente cliente = new Cliente();
            cliente.setId_cliente(row.getInteger("id_cliente"));
            cliente.setNombre_cliente(row.getString("nombre_cliente"));
            cliente.setContrasena_cliente(row.getString("contrasena_cliente"));
            cliente.setCorreo_cliente(row.getString("correo_cliente"));
            cliente.setDireccion(row.getString("direccion"));
            cliente.setTelefono(row.getString("telefono"));

            // Convertimos de WKT a Point
            String ubicacionWKT = row.getString("ubicacion");
            if (ubicacionWKT != null) {
                var reader = new org.locationtech.jts.io.WKTReader();
                Point point = (Point) reader.read(ubicacionWKT);
                cliente.setUbicacion(point);
            }

            return ResponseEntity.ok(cliente);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Override
    public ResponseEntity<Cliente> findByName(String name) {
        try (Connection conn = sql2o.open()) {
            // Asegúrate de que la entidad sea Cliente, no ClienteEntity
            Cliente cliente = conn.createQuery("SELECT * FROM cliente WHERE nombre_cliente = :name")
                    .addParameter("name", name)
                    .executeAndFetchFirst(Cliente.class);

            // Si no se encuentra el cliente, se retorna un error 404
            if (cliente == null) {
                return ResponseEntity.status(404).body(null);
            }

            // Si el cliente se encuentra, se retorna con un status 200
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            // Si hay una excepción, se maneja con un error 500
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Override
    public boolean existeCorreo(String correo) {
        String query = "SELECT COUNT(*) FROM Cliente WHERE correo_cliente = :correo_cliente";
        try (var con = sql2o.open()) {
            Integer count = con.createQuery(query)
                    .addParameter("correo_cliente", correo)
                    .executeScalar(Integer.class);
            return count != null && count > 0; // Si count > 0, existe el correo
        }
    }

    @Override
    public Cliente getClienteMayorGasto() {
        try (var con = sql2o.open()) {
            String sql = """
        SELECT c.* 
        FROM Cliente c 
        JOIN Pedido p ON c.id_cliente = p.id_cliente 
        JOIN Medios_de_pago m ON p.id_pago = m.id_pago 
        WHERE p.estado = 'Finalizado' 
        GROUP BY c.id_cliente, c.nombre_cliente, c.contrasena_cliente, 
                 c.correo_cliente, c.direccion, c.telefono, c.fecha_registro
        ORDER BY SUM(m.monto_total) DESC 
        LIMIT 1
        """;

            return con.createQuery(sql)
                    .executeAndFetchFirst(Cliente.class);
        } catch (Exception e) {
            System.out.println("Error al obtener cliente con mayor gasto: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Integer> getClientesFueraDeRadio(Double distanciaMetros) {
        String sql = """
        SELECT c.id_cliente
        FROM cliente c
        WHERE NOT EXISTS (
            SELECT 1
            FROM empresas_asociadas e
            WHERE ST_Distance(c.ubicacion, e.ubicacion) < :distancia
        )
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("distancia", distanciaMetros)
                    .executeScalarList(Integer.class);
        }
    }

    // Clase auxiliar para transformar zona
    private static class ResultadoZona {
        public Integer zona_id;
        public String nombre;
        public String zona_text;

        public ZonaCobertura toZonaCobertura() {
            try {
                var reader = new org.locationtech.jts.io.WKTReader();
                var polygon = (org.locationtech.jts.geom.Polygon) reader.read(zona_text);
                return new ZonaCobertura(zona_id, nombre, polygon);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //2) Determinar si un cliente se encuentra dentro de una zona de cobertura.
    @Override
    public String verificarZonaDeCliente(Integer idCliente) {
        String sql = """
    SELECT z.nombre
    FROM zonas_cobertura z
    JOIN cliente c ON ST_Within(c.ubicacion, z.geom)
    WHERE c.id_cliente = :idCliente
    """;

        try (Connection conn = sql2o.open()) {
            String nombreZona = conn.createQuery(sql)
                    .addParameter("idCliente", idCliente)
                    .executeAndFetchFirst(String.class);
            return nombreZona; // devuelve null si no hay zona que contenga al cliente
        }
    }

    //6)Determinar los clientes que están a más de 5km de cualquier empresa
    @Override
    public List<ClienteLejanoDTO> getClientesLejanosDeTodasLasEmpresas() {
        String sql = """
        SELECT 
            c.id_cliente AS idCliente,
            c.nombre_cliente AS nombreCliente,
            ST_AsText(c.ubicacion) AS ubicacion
        FROM cliente c
        WHERE NOT EXISTS (
            SELECT 1
            FROM empresas_asociadas e
            WHERE ST_DWithin(
                c.ubicacion::geography,
                e.ubicacion::geography,
                5000 -- 5 km en metros
            )
        )
    """;

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(ClienteLejanoDTO.class);
        }
    }

}
