package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repartidor {
    private Integer id_repartidor;
    private String nombre_repartidor;
    private String rut;
    private String telefono;
    private LocalDate fecha_contratacion;
    private Boolean activo;
    private Integer cantidad_entregas;

    private Integer puntuacion; // Aunque hay que quitarlo, lo dejo para que me funcione por como estan las tablas...

    private Point ubicacion;
}
