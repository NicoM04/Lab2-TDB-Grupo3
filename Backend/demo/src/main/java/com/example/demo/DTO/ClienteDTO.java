package com.example.demo.DTO;

import lombok.Data;

@Data
public class ClienteDTO {
    private Integer id_cliente;
    private String nombre_cliente;
    private String contrasena_cliente;
    private String correo_cliente;
    private String direccion;
    private String telefono;
    private String fecha_registro;
    private Double lat;
    private Double lon;
}
