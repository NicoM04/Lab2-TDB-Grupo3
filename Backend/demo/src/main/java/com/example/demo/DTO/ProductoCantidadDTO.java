package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCantidadDTO {
    private String nombreProducto;
    private Integer cantidad;
    private Double subtotal;
}
