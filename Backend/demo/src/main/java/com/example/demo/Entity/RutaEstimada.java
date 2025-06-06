package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaEstimada {
    private Integer id_ruta;
    private Integer id_pedido;
    private LineString ruta; // NUEVO: ruta estimada como línea entre empresa y cliente
}
