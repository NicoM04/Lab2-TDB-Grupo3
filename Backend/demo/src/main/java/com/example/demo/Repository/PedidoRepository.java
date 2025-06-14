package com.example.demo.Repository;

import com.example.demo.DTO.*;
import com.example.demo.Entity.Pedido;

import java.util.List;

public interface PedidoRepository {
    public Pedido crear(Pedido pedido);
    public List<PedidoDTO> getAll(int page, int size);
    public String update(Pedido pedido, Integer id);
    public void delete(Integer id);
    public PedidoDTO getById(Integer id);
    public List<ResumenPedidoDTO> obtenerResumenPorCliente(Integer idCliente);

    //PROCEDIMIENTOS ALMACENADOS
    public void registrarPedidoCompleto(PedidoCompletoDTO pedidoCompletoDTO);
    void confirmarPedidoYDescontarStock(int id);
    void cambiarEstadoPedido(int idPedido, String nuevoEstado);

    List<PedidoDTO> getPedidosByCliente(Integer idCliente, int page, int size);

    List<Pedido> getPedidosMasCercanos(Integer idEmpresa, int limite);

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //1) Encontrar los 5 puntos de entrega más cercanos a una empresa asociada.
    List<PedidoCercanoDTO> getPedidosMasCercanosEmpresa(Integer idEmpresa, int limite);

    //4) Identificar el punto de entrega más lejano desde cada empresa asociada.
    List<PedidoCercanoDTO> getPedidosMasLejanosPorEmpresa();

    //5) Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
    List<PedidoRutaDTO> getPedidosConMasDeDosZonas();

}
