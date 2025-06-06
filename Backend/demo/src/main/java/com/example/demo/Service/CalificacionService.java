package com.example.demo.Service;

import com.example.demo.Entity.Calificacion;
import com.example.demo.Entity.DetalleDePedido;
import com.example.demo.Repository.CalificacionRepository;
import com.example.demo.Repository.DetalleDePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    public Calificacion crear(Calificacion calificacion) {
        return calificacionRepository.crear(calificacion);
    }

    public List<Calificacion> getAll(int page, int size) {
        return calificacionRepository.getAll(page, size);
    }

    public Calificacion findById(Integer id) {
        return calificacionRepository.findById(id);
    }

    public String update(Calificacion calificacion, Integer id){
        return calificacionRepository.update(calificacion, id);
    }

    public void delete(Integer id) {
        calificacionRepository.delete(id);
    }

    // Método que obtiene todas las calificaciones de un repartidor por su id
    public List<Calificacion> getCalificacionesByRepartidorId(Integer idRepartidor) {
        return calificacionRepository.findByIdRepartidor(idRepartidor);
    }
}