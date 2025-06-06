package com.example.demo.Controller;

import com.example.demo.DTO.EstadoPedidoDTO;
import com.example.demo.DTO.PedidoCompletoDTO;
import com.example.demo.DTO.ResumenPedidoDTO;
import com.example.demo.Entity.Pedido;
import com.example.demo.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/create")
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        return pedidoService.crearPedido(pedido);
    }

    @GetMapping("/getAll")
    public List<Pedido> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return pedidoService.obtenerTodos(page, size);
    }

    //Aún no se usa el frontend
    @GetMapping("/resumenCliente/{id}")
    public List<ResumenPedidoDTO> getResumenPorCliente(@PathVariable Integer id) {
        return pedidoService.obtenerResumenPorCliente(id);
    }


    // Obtener un pedido por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable("id") Integer id) {
        Pedido pedido = pedidoService.getById(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si no se encuentra el pedido
        }
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public String actualizarPedido(@RequestBody Pedido pedido, @PathVariable Integer id) {
        return pedidoService.actualizarPedido(pedido, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarPedido(@PathVariable Integer id) {
        pedidoService.eliminarPedido(id);
    }

    //---------------- PROCEDIMIENTOS---------------------

    //1) Registrar pedido completo
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarPedido(@RequestBody PedidoCompletoDTO pedido) {
        try {
            pedidoService.registrarPedido(pedido);
            return ResponseEntity.ok("Pedido registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar pedido: " + e.getMessage());
        }
    }

    //2) Confirmar pedido y descontar stock
    @PutMapping("/confirmarPedidoYDescontarStock/{id}")
    public ResponseEntity<String> confirmarPedido(@PathVariable("id") int id) {
        try {
            // Llamamos al servicio para confirmar el pedido y descontar el stock
            pedidoService.confirmarPedidoYDescontarStock(id);
            return ResponseEntity.ok("Pedido confirmado y stock descontado correctamente.");
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un mensaje de error
            return ResponseEntity.status(500).body("Error al confirmar el pedido: " + e.getMessage());
        }
    }

    //3) Cambiar estado del pedido con validación
    @PutMapping("/cambiarEstadoPedido/{id}")
    public void cambiarEstadoPedido(@PathVariable int id, @RequestBody EstadoPedidoDTO dto) {
        pedidoService.cambiarEstadoPedido(id, dto.getNuevoEstado());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorCliente(@PathVariable Integer idCliente,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorCliente(idCliente, page, size);
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(404).body(null); // No hay pedidos para este cliente
        }
        return ResponseEntity.ok(pedidos); // Devolver los pedidos encontrados
    }


}
