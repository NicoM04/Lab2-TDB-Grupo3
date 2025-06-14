package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepartidorDTO {
    private Integer id_repartidor;
    private String nombre_repartidor;
    private String rut;
    private String telefono;
    private String fecha_contratacion; // en formato ISO
    private Boolean activo;
    private Integer cantidad_entregas;

    private Integer puntuacion;
}
