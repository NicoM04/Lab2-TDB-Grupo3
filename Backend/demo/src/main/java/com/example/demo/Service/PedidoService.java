package com.example.demo.Service;

import com.example.demo.DTO.*;
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
    public List<PedidoDTO> obtenerTodos(int page, int size) {
        return pedidoRepository.getAll(page, size);
    }

    public PedidoDTO getById(Integer id) {
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
    public List<PedidoDTO> obtenerPedidosPorCliente(Integer idCliente, int page, int size) {
        return pedidoRepository.getPedidosByCliente(idCliente, page, size);
    }

    public List<Pedido> getPedidosMasCercanos(Integer idEmpresa, int limite) {
        return pedidoRepository.getPedidosMasCercanos(idEmpresa, limite);
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //1) Encontrar los 5 puntos de entrega más cercanos a una empresa asociada.
    public List<PedidoCercanoDTO> getPedidosMasCercanosEmpresa(Integer idEmpresa, int limite){
        return pedidoRepository.getPedidosMasCercanosEmpresa(idEmpresa, limite);
    }

    //4) Identificar el punto de entrega más lejano desde cada empresa asociada.
    public List<PedidoCercanoDTO> getPedidosMasLejanosPorEmpresa() {
        return pedidoRepository.getPedidosMasLejanosPorEmpresa();
    }

    //5) Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
    public List<PedidoRutaDTO> getPedidosConMasDeDosZonas(){
        return pedidoRepository.getPedidosConMasDeDosZonas();
    }


}
