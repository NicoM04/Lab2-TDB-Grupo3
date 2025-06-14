package com.example.demo.Repository;

import com.example.demo.DTO.EmpresaAsociadaDTO;
import com.example.demo.Entity.EmpresasAsociadas;

import java.util.List;

public interface EmpresasAsociadasRepository {
    public EmpresasAsociadas crear(EmpresasAsociadas empresa);
    public List<EmpresaAsociadaDTO> getAll(int page, int size);
    public String update(EmpresasAsociadas empresa, Integer id);
    public void delete(Integer id);

    List<EmpresaAsociadaDTO> getEmpresasConMasFallos(int page, int size);
}
