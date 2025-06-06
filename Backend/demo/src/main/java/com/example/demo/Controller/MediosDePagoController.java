package com.example.demo.Controller;


import com.example.demo.Entity.MedioDePago;
import com.example.demo.Service.MediosDePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")

@RequestMapping("/mediosdepago")
public class MediosDePagoController {

    @Autowired
    private MediosDePagoService mediosDePagoService;

    @PostMapping("/create")
    public MedioDePago crearMedioDePago(@RequestBody MedioDePago medioDePago) {
        return mediosDePagoService.crear(medioDePago);
    }

    @GetMapping("/getAll")
    public List<MedioDePago> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return mediosDePagoService.getall(page, size);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<MedioDePago> obtenerPorId(@PathVariable("id") Integer id) {
        MedioDePago medioDePago = mediosDePagoService.findById(id);
        if (medioDePago == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra el MedioDePago
        }
        return new ResponseEntity<>(medioDePago, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public String actualizarMedioDePago(@RequestBody MedioDePago medioDePago, @PathVariable Integer id) {
        return mediosDePagoService.update(medioDePago, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarMedioDePago(@PathVariable Integer id) {
        mediosDePagoService.delete(id);
    }

    // Obtener medio de pago más utilizado en pedidos urgentes
    @GetMapping("/masutilizadourgentes")
    public List<String> obtenerMedioDePagoMasUtilizadoEnPedidosUrgentes() {
        return mediosDePagoService.obtenerMetodoPagoMasUsadoEnPedidosUrgentes();
    }
}
