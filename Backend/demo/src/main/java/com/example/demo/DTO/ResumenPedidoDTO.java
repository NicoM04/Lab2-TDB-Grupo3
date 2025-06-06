package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenPedidoDTO {
    private Integer idPedido;
    private String fechaPedido;
    private String estado;
    private Double montoTotal;
    private List<ProductoCantidadDTO> productos;
}
