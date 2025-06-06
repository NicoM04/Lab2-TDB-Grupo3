package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {
    private Integer id_notificacion;
    private Integer id_pedido;
    private LocalDate fecha_creacion;
    private String mensaje;
    private String tipo;
    private Boolean leida;
    private String descripcion;
}
