package com.tbd.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<double[]> coordenadas; // Cada elemento es [lon, lat]
}