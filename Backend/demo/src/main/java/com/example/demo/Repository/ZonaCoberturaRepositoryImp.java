package com.example.demo.Repository;

import com.example.demo.Entity.ZonaCobertura;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ZonaCoberturaRepositoryImp implements ZonaCoberturaRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public ZonaCobertura crear(ZonaCobertura zona) {
        String sql = "INSERT INTO zona_cobertura (nombre_zona, zona) " +
                "VALUES (:nombre_zona, ST_GeomFromText(:zona, 4326))";

        String wkt = zona.getZona() != null ? zona.getZona().toText() : null;

        try (Connection conn = sql2o.open()) {
            int id = (int) conn.createQuery(sql, true)
                    .addParameter("nombre_zona", zona.getNombre_zona())
                    .addParameter("zona", wkt)
                    .executeUpdate()
                    .getKey();
            zona.setId_zona(id);
            return zona;
        }
    }

    @Override
    public List<ZonaCobertura> getAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT id_zona, nombre_zona, ST_AsText(zona) AS zona_text " +
                "FROM zona_cobertura LIMIT :size OFFSET :offset";

        try (Connection conn = sql2o.open()) {
            List<ResultadoZona> resultados = conn.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(ResultadoZona.class);
            return resultados.stream().map(ResultadoZona::toZonaCobertura).collect(Collectors.toList());
        }
    }

    @Override
    public ZonaCobertura findById(Integer id) {
        String sql = "SELECT id_zona, nombre_zona, ST_AsText(zona) AS zona_text " +
                "FROM zona_cobertura WHERE id_zona = :id";

        try (Connection conn = sql2o.open()) {
            ResultadoZona resultado = conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(ResultadoZona.class);
            return resultado != null ? resultado.toZonaCobertura() : null;
        }
    }

    @Override
    public String update(ZonaCobertura zona, Integer id) {
        String sql = "UPDATE zona_cobertura SET nombre_zona = :nombre_zona, zona = ST_GeomFromText(:zona, 4326) " +
                "WHERE id_zona = :id";

        String wkt = zona.getZona() != null ? zona.getZona().toText() : null;

        try (Connection conn = sql2o.open()) {
            int result = conn.createQuery(sql)
                    .addParameter("nombre_zona", zona.getNombre_zona())
                    .addParameter("zona", wkt)
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult();
            return result > 0 ? "Zona actualizada correctamente" : "Zona no encontrada";
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM zona_cobertura WHERE id_zona = :id";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    // Clase auxiliar para parsear el WKT
    private static class ResultadoZona {
        public Integer id_zona;
        public String nombre_zona;
        public String zona_text;

        public ZonaCobertura toZonaCobertura() {
            GeometryFactory factory = new GeometryFactory();
            Polygon polygon = null;
            if (zona_text != null) {
                try {
                    polygon = (Polygon) new WKTReader(factory).read(zona_text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new ZonaCobertura(id_zona, nombre_zona, polygon);
        }
    }
}
