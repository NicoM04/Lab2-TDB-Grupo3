package com.example.demo.Repository;

import com.example.demo.Entity.Notificacion;

import java.util.List;

public interface NotificacionRepository {
    public Notificacion crear(Notificacion notificacion);
    public List<Notificacion> getAll(int page, int size);
    public String update(Notificacion notificacion, Integer id);
    public void delete(Integer id);
}
