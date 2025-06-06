package com.example.demo.Controller;

import com.example.demo.Entity.DetalleDePedido;
import com.example.demo.Service.DetalleDePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detallepedido")
@CrossOrigin("*")
public class DetalleDePedidoController {

    @Autowired
    private DetalleDePedidoService detalleDePedidoService;

    @PostMapping("/create")
    public DetalleDePedido crearDetalle(@RequestBody DetalleDePedido detalle) {
        return detalleDePedidoService.crearDetalle(detalle);
    }

    @GetMapping("/getAll")
    public List<DetalleDePedido> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return detalleDePedidoService.obtenerTodos(page, size);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DetalleDePedido> obtenerPorId(@PathVariable("id") Integer id) {
        DetalleDePedido detalle = detalleDePedidoService.getById(id);
        if (detalle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra el detalle
        }
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public String actualizarDetalle(@RequestBody DetalleDePedido detalle, @PathVariable Integer id) {
        return detalleDePedidoService.actualizarDetalle(detalle, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarDetalle(@PathVariable Integer id) {
        detalleDePedidoService.eliminarDetalle(id);
    }
}
