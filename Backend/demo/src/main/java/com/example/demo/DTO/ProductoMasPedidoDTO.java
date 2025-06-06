package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMasPedidoDTO {
    private String nombre_producto;
    private String categoria;
    private Integer total_pedidos;
}
