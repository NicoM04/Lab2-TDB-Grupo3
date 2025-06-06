package com.example.demo.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class PedidoCompletoDTO {
    private Integer idCliente;
    private Integer idEmpresa;
    private Integer idRepartidor;
    private Date fechaPedido;
    private Date fechaEntrega;
    private String estado;
    private Boolean urgente;
    private String metodoPago;
    private Integer[] productos;
    private Integer[] cantidades;
}
