package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Integer id_pedido;
    private Integer id_cliente;
    private Integer id_empresa;
    private Integer id_repartidor;
    private Integer id_pago;
    private LocalDate fecha_pedido;
    private LocalDate fecha_entrega;
    private String estado;
    private Boolean urgente;
}
