package com.example.demo.Service;

import com.example.demo.Entity.ZonaCobertura;
import com.example.demo.Repository.ZonaCoberturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZonaCoberturaService {

    @Autowired
    private ZonaCoberturaRepository zonaCoberturaRepository;

    public ZonaCobertura crear(ZonaCobertura zona) {
        return zonaCoberturaRepository.crear(zona);
    }

    public List<ZonaCobertura> getAll(int page, int size) {
        return zonaCoberturaRepository.getAll(page, size);
    }

    public ZonaCobertura getById(Integer id) {
        return zonaCoberturaRepository.findById(id);
    }

    public String update(ZonaCobertura zona, Integer id) {
        return zonaCoberturaRepository.update(zona, id);
    }

    public void delete(Integer id) {
        zonaCoberturaRepository.delete(id);
    }
}
