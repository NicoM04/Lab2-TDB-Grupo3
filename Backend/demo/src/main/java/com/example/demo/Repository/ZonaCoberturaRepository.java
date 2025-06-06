package com.example.demo.Repository;

import com.example.demo.Entity.ZonaCobertura;

import java.util.List;

public interface ZonaCoberturaRepository {
    ZonaCobertura crear(ZonaCobertura zona);
    List<ZonaCobertura> getAll(int page, int size);
    ZonaCobertura findById(Integer id);
    String update(ZonaCobertura zona, Integer id);
    void delete(Integer id);
}
