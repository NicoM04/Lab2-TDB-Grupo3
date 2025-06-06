package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoServicio {
    private Integer id_producto;
    private String nombre_producto;
    private String descripcion;
    private String categoria;
    private Double precio_unitario;
    private Integer stock;
}
