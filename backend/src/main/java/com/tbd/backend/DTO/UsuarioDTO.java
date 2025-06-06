package com.tbd.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;       // <-- agregamos el id
    private String username;
    private String correo;
    private String contrasena;
    private double x; // longitud
    private double y; // latitud
}

