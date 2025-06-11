package com.example.demo.DTO;

import lombok.Data;

@Data
public class ClienteLejanoDTO {
    private Integer idCliente;
    private String nombreCliente;
    private String ubicacion; // Lo tomaremos como texto con ST_AsText en la consulta
}
