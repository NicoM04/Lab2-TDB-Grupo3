package com.example.demo.Service;

import com.example.demo.Entity.RutaEstimada;
import com.example.demo.Repository.RutaEstimadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutaEstimadaService {

    @Autowired
    private RutaEstimadaRepository rutaEstimadaRepository;

    public RutaEstimada crear(RutaEstimada ruta) {
        return rutaEstimadaRepository.crear(ruta);
    }

    public RutaEstimada getById(Integer id_ruta) {
        return rutaEstimadaRepository.findById(id_ruta);
    }

    public RutaEstimada getByPedido(Integer id_pedido) {
        return rutaEstimadaRepository.findByPedido(id_pedido);
    }

    public List<RutaEstimada> getAll(int page, int size) {
        return rutaEstimadaRepository.getAll(page, size);
    }

    public String update(RutaEstimada ruta, Integer id_ruta) {
        return rutaEstimadaRepository.update(ruta, id_ruta);
    }

    public void delete(Integer id_ruta) {
        rutaEstimadaRepository.delete(id_ruta);
    }

    public Double calcularDistanciaTotalPorRepartidorEnMes(Integer idRepartidor, String fechaInicio, String fechaFin) {
        return rutaEstimadaRepository.calcularDistanciaTotalPorRepartidorEnMes(idRepartidor, fechaInicio, fechaFin);
    }

    public List<Integer> getPedidosConRutaEnMasDeDosZonas() {
        return rutaEstimadaRepository.getPedidosConRutaEnMasDeDosZonas();
    }


}
