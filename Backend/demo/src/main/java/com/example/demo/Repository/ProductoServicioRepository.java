package com.example.demo.Repository;

import com.example.demo.Entity.ProductoServicio;
import com.example.demo.DTO.ProductoMasPedidoDTO;

import java.util.List;

public interface ProductoServicioRepository {
    ProductoServicio crear(ProductoServicio producto);
    List<ProductoServicio> getAll(int page, int size);
    String update(ProductoServicio producto, Integer id);
    void delete(Integer id);
    ProductoServicio getById(Integer id);

    //Metodo para obtener los productos mas pedidos por categoria el ultimo mes
    List<ProductoMasPedidoDTO> obtenerMasPedidosPorCategoriaUltimoMes(int page, int size);
}
