package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private Integer id_pedido;
    private Integer id_cliente;
    private Integer id_empresa;
    private Integer id_repartidor;
    private Integer id_pago;
    private LocalDate fecha_pedido;
    private LocalDate fecha_entrega; //LE COLOQUE OPTIONAL PARA MANEJAR QUE EL PEDIDO SEA
    private String estado;
    private Boolean urgente;
}
