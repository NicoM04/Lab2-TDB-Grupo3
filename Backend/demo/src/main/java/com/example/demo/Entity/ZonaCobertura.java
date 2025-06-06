package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZonaCobertura {
    private Integer id_zona;
    private String nombre_zona;
    private Polygon zona; // NUEVO: polígono que representa la zona de cobertura
}
