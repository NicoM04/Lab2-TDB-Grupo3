package com.example.demo.Repository;

import com.example.demo.Entity.RutaEstimada;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RutaEstimadaRepositoryImp implements RutaEstimadaRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public RutaEstimada crear(RutaEstimada ruta) {
        String sql = "INSERT INTO ruta_estimada (id_pedido, ruta) " +
                "VALUES (:id_pedido, ST_GeomFromText(:ruta, 4326))";
        String wkt = ruta.getRuta() != null ? ruta.getRuta().toText() : null;

        try (Connection conn = sql2o.open()) {
            int id = (int) conn.createQuery(sql, true)
                    .addParameter("id_pedido", ruta.getId_pedido())
                    .addParameter("ruta", wkt)
                    .executeUpdate()
                    .getKey();
            ruta.setId_ruta(id);
            return ruta;
        }
    }

    @Override
    public RutaEstimada findById(Integer id_ruta) {
        String sql = "SELECT id_ruta, id_pedido, ST_AsText(ruta) AS ruta_text " +
                "FROM ruta_estimada WHERE id_ruta = :id";
        try (Connection conn = sql2o.open()) {
            ResultadoRuta resultado = conn.createQuery(sql)
                    .addParameter("id", id_ruta)
                    .executeAndFetchFirst(ResultadoRuta.class);
            return resultado != null ? resultado.toRutaEstimada() : null;
        }
    }

    @Override
    public RutaEstimada findByPedido(Integer id_pedido) {
        String sql = "SELECT id_ruta, id_pedido, ST_AsText(ruta) AS ruta_text " +
                "FROM ruta_estimada WHERE id_pedido = :id";
        try (Connection conn = sql2o.open()) {
            ResultadoRuta resultado = conn.createQuery(sql)
                    .addParameter("id", id_pedido)
                    .executeAndFetchFirst(ResultadoRuta.class);
            return resultado != null ? resultado.toRutaEstimada() : null;
        }
    }

    @Override
    public List<RutaEstimada> getAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT id_ruta, id_pedido, ST_AsText(ruta) AS ruta_text " +
                "FROM ruta_estimada LIMIT :size OFFSET :offset";
        try (Connection conn = sql2o.open()) {
            List<ResultadoRuta> resultados = conn.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(ResultadoRuta.class);
            return resultados.stream().map(ResultadoRuta::toRutaEstimada).collect(Collectors.toList());
        }
    }

    @Override
    public String update(RutaEstimada ruta, Integer id_ruta) {
        String sql = "UPDATE ruta_estimada SET id_pedido = :id_pedido, ruta = ST_GeomFromText(:ruta, 4326) " +
                "WHERE id_ruta = :id_ruta";
        String wkt = ruta.getRuta() != null ? ruta.getRuta().toText() : null;

        try (Connection conn = sql2o.open()) {
            int updated = conn.createQuery(sql)
                    .addParameter("id_pedido", ruta.getId_pedido())
                    .addParameter("ruta", wkt)
                    .addParameter("id_ruta", id_ruta)
                    .executeUpdate()
                    .getResult();
            return updated > 0 ? "Ruta actualizada correctamente" : "No se encontró la ruta";
        }
    }

    @Override
    public void delete(Integer id_ruta) {
        String sql = "DELETE FROM ruta_estimada WHERE id_ruta = :id_ruta";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id_ruta", id_ruta)
                    .executeUpdate();
        }
    }

    // Clase auxiliar para convertir el WKT a LineString
    private static class ResultadoRuta {
        public Integer id_ruta;
        public Integer id_pedido;
        public String ruta_text;

        public RutaEstimada toRutaEstimada() {
            GeometryFactory factory = new GeometryFactory();
            LineString linea = null;
            if (ruta_text != null) {
                try {
                    linea = (LineString) new WKTReader(factory).read(ruta_text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new RutaEstimada(id_ruta, id_pedido, linea);
        }
    }

    @Override
    public Double calcularDistanciaTotalPorRepartidorEnMes(Integer idRepartidor, String fechaInicio, String fechaFin) {
        String sql = """
        SELECT SUM(ST_Length(r.ruta::geography)) AS total_distancia
        FROM ruta_estimada r
        JOIN pedido p ON r.id_pedido = p.id_pedido
        WHERE p.id_repartidor = :idRepartidor
          AND p.fecha_pedido BETWEEN :inicio AND :fin
          AND r.ruta IS NOT NULL
    """;

        try (Connection conn = sql2o.open()) {
            Double total = conn.createQuery(sql)
                    .addParameter("idRepartidor", idRepartidor)
                    .addParameter("inicio", fechaInicio)
                    .addParameter("fin", fechaFin)
                    .executeScalar(Double.class);
            return total != null ? total : 0.0;
        }
    }

    @Override
    public List<Integer> getPedidosConRutaEnMasDeDosZonas() {
        String sql = """
        SELECT r.id_pedido
        FROM ruta_estimada r
        JOIN zona_cobertura z ON ST_Intersects(r.ruta, z.zona)
        GROUP BY r.id_pedido
        HAVING COUNT(z.zona_id) > 2
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeScalarList(Integer.class);
        }
    }


}
