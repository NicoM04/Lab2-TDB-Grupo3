package com.example.demo.Repository;

import com.example.demo.Entity.MedioDePago;

import java.util.List;

public interface MediosDePagoRepository {
    public MedioDePago crear(MedioDePago medioPago);
    public List<MedioDePago> getAll(int page, int size);
    public MedioDePago findById(Integer id);
    public String update(MedioDePago medioPago, Integer id);
    public void delete(Integer id);

    public List<String> obtenerMetodoPagoMasUsadoEnPedidosUrgentes();
}
