package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresasAsociadas {
    private Integer id_empresa;
    private String nombre_empresa;
    private String rut_empresa;
    private String correo_contacto;
    private String direccion;

    private Point ubicacion;
}
