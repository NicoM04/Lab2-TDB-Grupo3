package com.example.demo.Repository;

import com.example.demo.DTO.PedidoCompletoDTO;
import com.example.demo.DTO.ResumenPedidoDTO;
import com.example.demo.Entity.Pedido;

import java.util.List;

public interface PedidoRepository {
    public Pedido crear(Pedido pedido);
    public List<Pedido> getAll(int page, int size);
    public String update(Pedido pedido, Integer id);
    public void delete(Integer id);
    public Pedido getById(Integer id);

    public List<ResumenPedidoDTO> obtenerResumenPorCliente(Integer idCliente);

    //PROCEDIMIENTOS ALMACENADOS
    public void registrarPedidoCompleto(PedidoCompletoDTO pedidoCompletoDTO);
    void confirmarPedidoYDescontarStock(int id);
    void cambiarEstadoPedido(int idPedido, String nuevoEstado);

    List<Pedido> getPedidosByCliente(Integer idCliente, int page, int size);

    List<Pedido> getPedidosMasCercanos(Integer idEmpresa, int limite);

    List<Pedido> getPedidosMasLejanosPorEmpresa();



}
