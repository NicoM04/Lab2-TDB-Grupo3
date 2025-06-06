package com.example.demo.Service;

import com.example.demo.Entity.Calificacion;
import com.example.demo.Entity.MedioDePago;
import com.example.demo.Repository.MediosDePagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediosDePagoService {

    @Autowired
    private MediosDePagoRepository mediosDePagoRepository;

    public MedioDePago crear(MedioDePago medioDePago) {
        return mediosDePagoRepository.crear(medioDePago);
    }

    public List<MedioDePago> getall(int page, int size) {
        return mediosDePagoRepository.getAll(page, size);
    }

    public MedioDePago findById(Integer id) {
        return mediosDePagoRepository.findById(id);
    }

    public String update(MedioDePago medioDePago, Integer id) {
        return mediosDePagoRepository.update(medioDePago, id);
    }

    public void delete(Integer id) {
        mediosDePagoRepository.delete(id);
    }

    public List<String> obtenerMetodoPagoMasUsadoEnPedidosUrgentes(){
        return mediosDePagoRepository.obtenerMetodoPagoMasUsadoEnPedidosUrgentes();
    }
}
