package com.example.demo.Controller;

import com.example.demo.DTO.EmpresaAsociadaDTO;
import com.example.demo.Entity.EmpresasAsociadas;
import com.example.demo.Service.EmpresasAsociadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<EmpresaAsociadaDTO> obtenerEmpresas(@RequestParam(defaultValue = "1") int page,
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
    public List<EmpresaAsociadaDTO> empresasConMasFallos(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return empresasAsociadasService.obtenerEmpresasConMasFallos(page, size);
    }

    // Obtener empresa por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<EmpresaAsociadaDTO> obtenerEmpresaPorId(@PathVariable Integer id) {
        EmpresaAsociadaDTO dto = empresasAsociadasService.obtenerEmpresaPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // Obtener empresa por nombre
    @GetMapping("/getByName/{nombre}")
    public ResponseEntity<EmpresaAsociadaDTO> obtenerEmpresaPorNombre(@PathVariable String nombre) {
        EmpresaAsociadaDTO dto = empresasAsociadasService.obtenerEmpresaPorNombre(nombre);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

}
