package com.example.demo.Service;

import com.example.demo.DTO.PedidoCompletoDTO;
import com.example.demo.DTO.ResumenPedidoDTO;
import com.example.demo.Entity.Pedido;
import com.example.demo.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.crear(pedido);
    }

    //servicio getAll pedidos
    public List<Pedido> obtenerTodos(int page, int size) {
        return pedidoRepository.getAll(page, size);
    }

    public Pedido getById(Integer id) {
        return pedidoRepository.getById(id);
    }

    public String actualizarPedido(Pedido pedido, Integer id) {
        return pedidoRepository.update(pedido, id);
    }

    public void eliminarPedido(Integer id) {
        pedidoRepository.delete(id);
    }

    public List<ResumenPedidoDTO> obtenerResumenPorCliente(Integer idCliente){
        return pedidoRepository.obtenerResumenPorCliente(idCliente);
    }


    //--------------- PROCEDIMIENTOS---------------
    public void registrarPedido(PedidoCompletoDTO pedido) {
        pedidoRepository.registrarPedidoCompleto(pedido);
    }

    public void confirmarPedidoYDescontarStock(int id){
        pedidoRepository.confirmarPedidoYDescontarStock(id);
    }

    public void cambiarEstadoPedido(int id, String nuevoEstado){
        pedidoRepository.cambiarEstadoPedido(id, nuevoEstado);
    }

    // Método para obtener los pedidos de un cliente
    public List<Pedido> obtenerPedidosPorCliente(Integer idCliente, int page, int size) {
        return pedidoRepository.getPedidosByCliente(idCliente, page, size);
    }
}
