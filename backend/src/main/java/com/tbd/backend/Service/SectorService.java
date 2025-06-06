package com.tbd.backend.Service;

import com.tbd.backend.DTO.SectorDTO;
import com.tbd.backend.Entity.Sector;
import com.tbd.backend.Repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    // Crear
    public Sector crearSector(Sector sector) {
        return sectorRepository.save(sector);
    }

    public List<SectorDTO> obtenerTodos() {
        return sectorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SectorDTO> obtenerPorId(Long id) {
        return sectorRepository.findById(id)
                .map(this::convertToDTO);
    }

    private SectorDTO convertToDTO(Sector sector) {
        SectorDTO dto = new SectorDTO();
        dto.setId(sector.getId());
        dto.setNombre(sector.getNombre());
        dto.setDescripcion(sector.getDescripcion());

        if (sector.getUbicacion() != null) {
            // Obtener el anillo exterior del polígono (primer anillo)
            var coords = sector.getUbicacion().getExteriorRing().getCoordinates();

            List<double[]> coordenadas = new java.util.ArrayList<>();
            for (var coord : coords) {
                coordenadas.add(new double[]{coord.getX(), coord.getY()});
            }
            dto.setCoordenadas(coordenadas);
        }

        return dto;
    }



    // Actualizar (recibe id y sector con datos nuevos)
    public Sector actualizarSector(Long id, Sector sectorActualizado) {
        Sector sector = sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sector no encontrado con id " + id));

        sector.setNombre(sectorActualizado.getNombre());
        sector.setDescripcion(sectorActualizado.getDescripcion());
        sector.setUbicacion(sectorActualizado.getUbicacion());

        return sectorRepository.save(sector);
    }

    // Eliminar por id
    public void eliminarSector(Long id) {
        if (!sectorRepository.existsById(id)) {
            throw new RuntimeException("Sector no encontrado con id " + id);
        }
        sectorRepository.deleteById(id);
    }
}
