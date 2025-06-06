package com.example.demo.Controller;

import com.example.demo.DTO.ProductoMasPedidoDTO;
import com.example.demo.Entity.ProductoServicio;
import com.example.demo.Service.ProductoServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/producto")
public class ProductoServicioController {

    @Autowired
    private ProductoServicioService productoServicioService;

    @PostMapping("/create")
    public ProductoServicio crearProducto(@RequestBody ProductoServicio producto) {
        return productoServicioService.crearProducto(producto);
    }

    @GetMapping("/getAll")
    public List<ProductoServicio> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return productoServicioService.obtenerTodos(page, size);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductoServicio> getProductoById(@PathVariable Integer id) {
        ProductoServicio producto = productoServicioService.getById(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);  // Si se encuentra el producto, se devuelve con HTTP 200
        } else {
            return ResponseEntity.notFound().build();  // Si no se encuentra el producto, se devuelve HTTP 404
        }
    }

    @PutMapping("/update/{id}")
    public String actualizarProducto(@RequestBody ProductoServicio producto, @PathVariable Integer id) {
        return productoServicioService.actualizarProducto(producto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarProducto(@PathVariable Integer id) {
        productoServicioService.eliminarProducto(id);
    }

    @GetMapping("/masPedidosUltimoMes")
    public List<ProductoMasPedidoDTO> obtenerMasPedidosUltimoMes(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return productoServicioService.obtenerMasPedidosPorCategoriaUltimoMes(page, size);
    }

}
