package com.example.demo.Service;

import com.example.demo.DTO.EmpresaAsociadaDTO;
import com.example.demo.Entity.EmpresasAsociadas;
import com.example.demo.Repository.EmpresasAsociadasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresasAsociadasService {

    @Autowired
    private EmpresasAsociadasRepository empresasAsociadasRepository;

    // Crear una nueva empresa
    public EmpresasAsociadas crearEmpresa(EmpresasAsociadas empresa) {
        return empresasAsociadasRepository.crear(empresa);
    }

    // Obtener todas las empresas
    public List<EmpresaAsociadaDTO> obtenerEmpresas(int page, int size) {
        return empresasAsociadasRepository.getAll(page, size);
    }

    // Actualizar empresa
    public String actualizarEmpresa(EmpresasAsociadas empresa, Integer id) {
        return empresasAsociadasRepository.update(empresa, id);
    }

    // Eliminar empresa
    public void eliminarEmpresa(Integer id) {
        empresasAsociadasRepository.delete(id);
    }

    public List<EmpresaAsociadaDTO> obtenerEmpresasConMasFallos(int page, int size) {
        return empresasAsociadasRepository.getEmpresasConMasFallos(page, size);
    }

}
