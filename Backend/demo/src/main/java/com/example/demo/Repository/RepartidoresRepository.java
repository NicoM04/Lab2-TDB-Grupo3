package com.example.demo.Repository;
import com.example.demo.DTO.RepartidorDTO;
import com.example.demo.Entity.Repartidor;

import java.util.List;
import java.util.Map;

public interface RepartidoresRepository {
    public Repartidor crear(Repartidor repartidor);
    public List<RepartidorDTO> getAll(int page, int size);
    public Repartidor findById(Integer id);
    public String update(Repartidor repartidor, Integer id);
    public void delete(Integer id);

    public List<Map<String, Object>> obtenerTiempoPromedioEntregaPorRepartidor(int page, int size);
    public List<Map<String, Object>> obtenerTop3RepartidoresConMejorRendimiento();

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //3) Calcular la distancia total recorrida por un repartidor en el último mes.
    Double obtenerDistanciaTotalRecorridaEnUltimoMes(int idRepartidor, int ultimosMeses);

}
