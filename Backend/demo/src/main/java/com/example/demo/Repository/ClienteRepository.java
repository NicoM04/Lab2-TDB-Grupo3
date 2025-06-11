package com.example.demo.Repository;

import com.example.demo.DTO.ClienteLejanoDTO;
import com.example.demo.Entity.Cliente;
import com.example.demo.Entity.ZonaCobertura;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClienteRepository {
    public void crear(Cliente cliente);
    public List<Cliente> getAll(int page, int size);
    public String update(Cliente cliente, Integer id);
    public void delete(Integer id);
    public Cliente findById(Integer id);
    public ResponseEntity<Cliente> findByCorreo(String correo);
    public boolean existeCorreo(String correo);
    public ResponseEntity<Cliente> findByName(String name);
    Cliente getClienteMayorGasto();

    List<Integer> getClientesFueraDeRadio(Double distanciaMetros);

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //2) Determinar si un cliente se encuentra dentro de una zona de cobertura.
    String verificarZonaDeCliente(Integer idCliente);

    //6)Determinar los clientes que están a más de 5km de cualquier empresa
    List<ClienteLejanoDTO> getClientesLejanosDeTodasLasEmpresas();


}
