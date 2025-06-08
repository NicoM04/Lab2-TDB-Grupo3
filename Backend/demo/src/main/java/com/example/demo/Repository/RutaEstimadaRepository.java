package com.example.demo.Repository;

import com.example.demo.Entity.RutaEstimada;

import java.util.List;

public interface RutaEstimadaRepository {
    RutaEstimada crear(RutaEstimada ruta);
    RutaEstimada findById(Integer id_ruta);
    RutaEstimada findByPedido(Integer id_pedido);
    List<RutaEstimada> getAll(int page, int size);
    String update(RutaEstimada ruta, Integer id_ruta);
    void delete(Integer id_ruta);

    Double calcularDistanciaTotalPorRepartidorEnMes(Integer idRepartidor, String fechaInicio, String fechaFin);

    List<Integer> getPedidosConRutaEnMasDeDosZonas();

}
