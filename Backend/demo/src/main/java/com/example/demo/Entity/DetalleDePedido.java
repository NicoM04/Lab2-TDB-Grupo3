package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleDePedido {
    private Integer id_detalle;
    private Integer id_producto;
    private Integer id_pedido;
    private Integer cantidad;
    private Double subtotal;
}
