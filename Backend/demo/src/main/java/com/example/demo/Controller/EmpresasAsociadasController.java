package com.example.demo.Controller;

import com.example.demo.Entity.EmpresasAsociadas;
import com.example.demo.Service.EmpresasAsociadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@CrossOrigin("*")

public class EmpresasAsociadasController {

    @Autowired
    private EmpresasAsociadasService empresasAsociadasService;

    // Crear una nueva empresa
    @PostMapping
    public EmpresasAsociadas crearEmpresa(@RequestBody EmpresasAsociadas empresa) {
        return empresasAsociadasService.crearEmpresa(empresa);
    }

    // Obtener todas las empresas
    @GetMapping
    public List<EmpresasAsociadas> obtenerEmpresas(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return empresasAsociadasService.obtenerEmpresas(page, size);
    }

    // Actualizar empresa por ID
    @PutMapping("/{id}")
    public String actualizarEmpresa(@RequestBody EmpresasAsociadas empresa, @PathVariable Integer id) {
        return empresasAsociadasService.actualizarEmpresa(empresa, id);
    }

    // Eliminar empresa por ID
    @DeleteMapping("/{id}")
    public void eliminarEmpresa(@PathVariable Integer id) {
        empresasAsociadasService.eliminarEmpresa(id);
    }

    @GetMapping("/mas-fallos")
    public List<EmpresasAsociadas> empresasConMasFallos(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return empresasAsociadasService.obtenerEmpresasConMasFallos(page, size);
    }

}
