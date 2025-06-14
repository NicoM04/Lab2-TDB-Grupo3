package com.example.demo.Service;


import com.example.demo.DTO.RepartidorDTO;
import com.example.demo.Entity.Repartidor;
import com.example.demo.Repository.RepartidoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RepartidorService {

    @Autowired
    private RepartidoresRepository repartidoresRepository;

    public Repartidor crear(Repartidor repartidor) {
        return repartidoresRepository.crear(repartidor);
    }

    public List<RepartidorDTO> getall(int page, int size) {
        return repartidoresRepository.getAll(page, size);
    }

    public RepartidorDTO findById(Integer id) {
        return repartidoresRepository.findById(id);
    }

    public String update(Repartidor repartidor, Integer id) {
        return repartidoresRepository.update(repartidor, id);
    }

    public void delete(Integer id) {
        repartidoresRepository.delete(id);
    }

    public List<Map<String, Object>> obtenerTiempoPromedioEntregaPorRepartidor(int page, int size){
        return repartidoresRepository.obtenerTiempoPromedioEntregaPorRepartidor(page, size);
    }

    public List<Map<String, Object>> obtenerTop3RepartidoresConMejorRendimiento(){
        return repartidoresRepository.obtenerTop3RepartidoresConMejorRendimiento();
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //3) Calcular la distancia total recorrida por un repartidor en el último mes.
    public Double obtenerDistanciaTotalRecorridaEnUltimoMes(int idRepartidor, int ultimosMeses){
        return repartidoresRepository.obtenerDistanciaTotalRecorridaEnUltimoMes(idRepartidor, ultimosMeses);
    }

}
