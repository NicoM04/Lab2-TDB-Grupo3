package com.example.demo.Repository;

import com.example.demo.DTO.EmpresaAsociadaDTO;
import com.example.demo.Entity.EmpresasAsociadas;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class EmpresasAsociadasRepositoryImp implements EmpresasAsociadasRepository {

    @Autowired
    private Sql2o sql2o;

    private EmpresaAsociadaDTO mapToDTO(EmpresasAsociadas empresa) {
        EmpresaAsociadaDTO dto = new EmpresaAsociadaDTO();
        dto.setId_empresa(empresa.getId_empresa());
        dto.setNombre_empresa(empresa.getNombre_empresa());
        dto.setRut_empresa(empresa.getRut_empresa());
        dto.setCorreo_contacto(empresa.getCorreo_contacto());
        dto.setDireccion(empresa.getDireccion());
        return dto;
    }

    @Override
    public EmpresasAsociadas crear(EmpresasAsociadas empresa) {
        try (Connection conn = sql2o.open()) {
            String sql = "INSERT INTO empresas_asociadas (nombre_empresa, rut_empresa, correo_contacto, direccion) " +
                    "VALUES (:nombre_empresa, :rut_empresa, :correo_contacto, :direccion)";
            String wkt = empresa.getUbicacion() != null ? empresa.getUbicacion().toText() : null;
            conn.createQuery(sql)
                    .addParameter("nombre_empresa", empresa.getNombre_empresa())
                    .addParameter("rut_empresa", empresa.getRut_empresa())
                    .addParameter("correo_contacto", empresa.getCorreo_contacto())
                    .addParameter("direccion", empresa.getDireccion())
                    .addParameter("ubicacion", wkt)
                    .executeUpdate();
            return empresa;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<EmpresaAsociadaDTO> getAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = """
        SELECT id_empresa, nombre_empresa, rut_empresa, correo_contacto, direccion
        FROM empresas_asociadas
        LIMIT :size OFFSET :offset
        """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(EmpresaAsociadaDTO.class);

        } catch (Exception e) {
            System.out.println("Error en getAll: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String update(EmpresasAsociadas empresa, Integer id) {
        try (Connection conn = sql2o.open()) {
            String sql = "UPDATE empresas_asociadas SET nombre_empresa = :nombre_empresa, rut_empresa = :rut_empresa, " +
                    "correo_contacto = :correo_contacto, direccion = :direccion WHERE id_empresa = :id";
            conn.createQuery(sql)
                    .addParameter("nombre_empresa", empresa.getNombre_empresa())
                    .addParameter("rut_empresa", empresa.getRut_empresa())
                    .addParameter("correo_contacto", empresa.getCorreo_contacto())
                    .addParameter("direccion", empresa.getDireccion())
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
            String sql = "DELETE FROM empresas_asociadas WHERE id_empresa = :id";
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<EmpresaAsociadaDTO> getEmpresasConMasFallos(int page, int size) {
        int offset = (page - 1) * size;
        String sql = """
        SELECT e.id_empresa, e.nombre_empresa, e.rut_empresa, e.correo_contacto, e.direccion
        FROM empresas_asociadas e
        JOIN pedido p ON e.id_empresa = p.id_empresa
        WHERE p.estado = 'Cancelado'
        GROUP BY e.id_empresa, e.nombre_empresa, e.rut_empresa, e.correo_contacto, e.direccion
        ORDER BY COUNT(p.id_pedido) DESC
        LIMIT :size OFFSET :offset
        """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("size", size)
                    .addParameter("offset", offset)
                    .executeAndFetch(EmpresaAsociadaDTO.class);
        } catch (Exception e) {
            System.out.println("Error al obtener empresas con más fallos: " + e.getMessage());
            return null;
        }
    }

    @Override
    public EmpresaAsociadaDTO getById(Integer id) {
        String sql = """
    SELECT id_empresa, nombre_empresa, rut_empresa, correo_contacto, direccion
    FROM empresas_asociadas
    WHERE id_empresa = :id
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(EmpresaAsociadaDTO.class);
        } catch (Exception e) {
            System.out.println("Error en getById: " + e.getMessage());
            return null;
        }
    }

    @Override
    public EmpresaAsociadaDTO getByName(String nombre) {
        String sql = """
    SELECT id_empresa, nombre_empresa, rut_empresa, correo_contacto, direccion
    FROM empresas_asociadas
    WHERE LOWER(nombre_empresa) = LOWER(:nombre)
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("nombre", nombre)
                    .executeAndFetchFirst(EmpresaAsociadaDTO.class);
        } catch (Exception e) {
            System.out.println("Error en getByName: " + e.getMessage());
            return null;
        }
    }



}
