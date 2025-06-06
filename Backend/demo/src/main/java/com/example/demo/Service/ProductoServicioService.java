package com.example.demo.Service;

import com.example.demo.Entity.ProductoServicio;
import com.example.demo.Repository.ProductoServicioRepository;
import com.example.demo.DTO.ProductoMasPedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServicioService {

    @Autowired
    private ProductoServicioRepository productoServicioRepository;

    public ProductoServicio crearProducto(ProductoServicio producto) {
        return productoServicioRepository.crear(producto);
    }

    public List<ProductoServicio> obtenerTodos(int page, int size) {
        return productoServicioRepository.getAll(page, size);
    }

    public ProductoServicio getById(Integer id) {
        return productoServicioRepository.getById(id);
    }

    public String actualizarProducto(ProductoServicio producto, Integer id) {
        return productoServicioRepository.update(producto, id);
    }

    public void eliminarProducto(Integer id) {
        productoServicioRepository.delete(id);
    }

    //Obtiene productos mas pedidos por categoría en el mes
    public List<ProductoMasPedidoDTO> obtenerMasPedidosPorCategoriaUltimoMes(int page, int size) {
        return productoServicioRepository.obtenerMasPedidosPorCategoriaUltimoMes(page, size);
    }
}
