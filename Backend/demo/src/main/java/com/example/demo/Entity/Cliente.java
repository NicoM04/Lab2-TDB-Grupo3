package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
        private Integer id_cliente;
        private String nombre_cliente;
        private String contrasena_cliente;
        private String correo_cliente;
        private String direccion;
        private String telefono;
        private LocalDate fecha_registro;
}
