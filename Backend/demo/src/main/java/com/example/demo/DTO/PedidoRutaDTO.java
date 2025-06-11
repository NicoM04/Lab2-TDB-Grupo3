package com.example.demo.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class PedidoRutaDTO {
    private Integer idPedido;
    private Integer idCliente;
    private Integer idEmpresa;
    private String nombreEmpresa;
    private String puntoEntrega;
    private String ubicacionEmpresa;
    private Double distanciaMetros;
    private int cantidadZonas;
}

