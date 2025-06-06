package com.tbd.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Polygon;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @JsonIgnore
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Polygon ubicacion;
}