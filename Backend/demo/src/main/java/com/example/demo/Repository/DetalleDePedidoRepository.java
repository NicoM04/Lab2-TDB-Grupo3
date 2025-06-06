package com.example.demo.Repository;

import com.example.demo.Entity.DetalleDePedido;

import java.util.List;

public interface DetalleDePedidoRepository {
    public DetalleDePedido crear(DetalleDePedido detalle);
    public List<DetalleDePedido> getAll(int page, int size);
    public String update(DetalleDePedido detalle, Integer id);
    public void delete(Integer id);
    public DetalleDePedido getById(Integer id);
}
