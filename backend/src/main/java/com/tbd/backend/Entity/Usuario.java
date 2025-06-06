package com.tbd.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String correo;

    private String contrasena;

    @JsonIgnore
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point ubicacion;  // ubicación geoespacial (lat, lon)
}
