package com.example.demo.Controller;


import com.example.demo.DTO.RepartidorDTO;
import com.example.demo.Entity.Repartidor;
import com.example.demo.Service.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repartidor")
@CrossOrigin("*")

public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @PostMapping("/create")
    public Repartidor crearRepartidor(@RequestBody Repartidor repartidor) {
        return repartidorService.crear(repartidor);
    }

    @GetMapping("/getAll")
    public List<RepartidorDTO> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return repartidorService.getall(page, size);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Repartidor> obtenerPorId(@PathVariable("id") Integer id) {
        Repartidor repartidor = repartidorService.findById(id);
        if (repartidor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra el repartidor
        }
        return new ResponseEntity<>(repartidor, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public String actualizarRepartidor(@RequestBody Repartidor repartidor, @PathVariable Integer id) {
        return repartidorService.update(repartidor, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarRepartidor(@PathVariable Integer id) {
        repartidorService.delete(id);
    }

    @GetMapping("/tiempopromedioentrega")
    public List<Map<String, Object>> obtenerTiempoPromedioEntrega(@RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return repartidorService.obtenerTiempoPromedioEntregaPorRepartidor(page, size);
    }

    @GetMapping("/mejoresrepartidores")
    public List<Map<String, Object>> obtenerMejoresRepartidores() {
        return repartidorService.obtenerTop3RepartidoresConMejorRendimiento();
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //3) Calcular la distancia total recorrida por un repartidor en el último mes.
    @GetMapping("/distanciaTotalRecorrida/{idRepartidor}")
    public Double obtenerDistanciaTotalRecorrida(
            @PathVariable int idRepartidor,
            @RequestParam int ultimosMeses) {
        return repartidorService.obtenerDistanciaTotalRecorridaEnUltimoMes(idRepartidor, ultimosMeses);
    }
}
