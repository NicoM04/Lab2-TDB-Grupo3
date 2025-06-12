package com.example.demo.Controller;

import com.example.demo.DTO.ClienteDTO;
import com.example.demo.DTO.ClienteLejanoDTO;
import com.example.demo.Entity.Cliente;
import com.example.demo.Entity.ZonaCobertura;
import com.example.demo.Service.ClienteService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")

@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody ClienteDTO dto) {
        GeometryFactory factory = new GeometryFactory();
        Point ubicacion = factory.createPoint(new Coordinate(dto.getLon(), dto.getLat()));

        Cliente cliente = new Cliente();
        cliente.setNombre_cliente(dto.getNombre_cliente());
        cliente.setContrasena_cliente(dto.getContrasena_cliente());
        cliente.setCorreo_cliente(dto.getCorreo_cliente());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFecha_registro(LocalDate.parse(dto.getFecha_registro()));
        cliente.setUbicacion(ubicacion);

        return clienteService.crearCliente(cliente);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody Cliente user) {
        return clienteService.loginUser(user.getCorreo_cliente(), user.getContrasena_cliente());
    }

    // Obtener todos los clientes
    @GetMapping("/getAll")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes(page, size);
        return clientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientes);
    }

    // Obtener un cliente por su ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }

    // Buscar cliente por correo
    @GetMapping("/buscar/correo")
    public ResponseEntity<Cliente> buscarPorCorreo(@RequestParam String correo) {
        return clienteService.buscarPorCorreo(correo);
    }

    // Buscar cliente por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<Cliente> buscarPorNombre(@RequestParam String nombre) {
        return clienteService.buscarPorNombre(nombre);
    }

    // Actualizar un cliente
    @PutMapping("/update/{id}")
    public ResponseEntity<String> actualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        String resultado = clienteService.actualizarCliente(id, cliente);
        return resultado.equals("Cliente actualizado exitosamente")
                ? ResponseEntity.ok(resultado)
                : ResponseEntity.badRequest().body(resultado);
    }

    // Eliminar un cliente
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mayor-gasto")
    public Cliente clienteMayorGasto() {
        return clienteService.obtenerClienteMayorGasto();
    }

    @GetMapping("/fueraDeCobertura")
    public ResponseEntity<List<Integer>> clientesFueraDeRadio(
            @RequestParam(defaultValue = "5000") Double distancia) {
        List<Integer> clientes = clienteService.getClientesFueraDeRadio(distancia);
        return clientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientes);
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //2) Determinar si un cliente se encuentra dentro de una zona de cobertura.
    @GetMapping("/zonaCobertura/{idCliente}")
    public ResponseEntity<String> verificarZonaDeCobertura(@PathVariable Integer idCliente) {
        String zona = clienteService.verificarZonaDeCliente(idCliente);
        return zona != null ? ResponseEntity.ok(zona) : ResponseEntity.notFound().build();
    }

    @GetMapping("/clientesLejanos")
    public List<ClienteLejanoDTO> getClientesLejanosDeTodasLasEmpresas() {
        return clienteService.getClientesLejanosDeTodasLasEmpresas();
    }

}
