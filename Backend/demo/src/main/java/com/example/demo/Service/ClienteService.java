package com.example.demo.Service;

import com.example.demo.DTO.ClienteDTO;
import com.example.demo.DTO.ClienteLejanoDTO;
import com.example.demo.Entity.Cliente;
import com.example.demo.Entity.ZonaCobertura;
import com.example.demo.Repository.ClienteRepository;
import com.example.demo.config.InputVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.config.JwtMiddlewareService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private JwtMiddlewareService jwtMiddlewareService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear un cliente
    public ResponseEntity<Object> crearCliente(Cliente cliente) {
        // Verificación de caracteres
        if (!InputVerificationService.validateInput(cliente.getNombre_cliente()) ||
                !InputVerificationService.validateInput(cliente.getCorreo_cliente()) ||
                !InputVerificationService.validateInput(cliente.getContrasena_cliente())) {
            return ResponseEntity.badRequest().body("Error: caracteres no permitidos.");
        }

        // Verificación de existencia de correo
        if (clienteRepository.existeCorreo(cliente.getCorreo_cliente())) {
            return ResponseEntity.status(409).body("Ya existe un usuario con ese correo.");
        }

        // Encriptar la contraseña
        cliente.setContrasena_cliente(passwordEncoder.encode(cliente.getContrasena_cliente()));

        try {
            // Guardar el cliente
            clienteRepository.crear(cliente);

            // Generar el token JWT
            String token = jwtMiddlewareService.generateToken(cliente);

            // Retornar el token generado
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el cliente: " + e.getMessage());
        }
    }

    // Obtener todos los clientes
    public List<ClienteDTO> obtenerTodosLosClientes(int page, int size) {
        return clienteRepository.getAll(page, size);
    }

    // Obtener un cliente por su ID
    public ClienteDTO obtenerClientePorId(Integer id) {
        return clienteRepository.findById(id);
    }

    // Actualizar un cliente
    public String actualizarCliente(Integer id, Cliente cliente) {
        return clienteRepository.update(cliente, id);
    }

    // Eliminar un cliente
    public void eliminarCliente(Integer id) {
        clienteRepository.delete(id);
    }

    // Buscar cliente por correo
    public ResponseEntity<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    // Buscar cliente por nombre
    public ResponseEntity<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByName(nombre);
    }

    public ResponseEntity<Object> loginUser(String correo_cliente, String contrasena_cliente) {

        if (!InputVerificationService.validateInput(correo_cliente) || !InputVerificationService.validateInput(contrasena_cliente)) {
            return ResponseEntity.badRequest().body("Error al iniciar sesión: caracteres no permitidos.");
        }

        try {
            ResponseEntity<Cliente> response = buscarPorCorreo(correo_cliente);
            Cliente cliente = response.getBody();

            if (cliente == null) {
                return ResponseEntity.status(401).body("Usuario no encontrado.");
            }

            String storedPassword = cliente.getContrasena_cliente();

            if (correo_cliente.endsWith("@example.com")) {
                if (contrasena_cliente.equals(storedPassword)) {
                    String token = jwtMiddlewareService.generateToken(cliente);
                    return ResponseEntity.ok(token);
                } else {
                    return ResponseEntity.status(401).body("Contraseña incorrecta.");
                }
            }

            if (passwordEncoder.matches(contrasena_cliente, storedPassword)) {
                String token = jwtMiddlewareService.generateToken(cliente);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Contraseña incorrecta.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al iniciar sesión: " + e.getMessage());
        }
    }

    public Cliente obtenerClienteMayorGasto() {
        return clienteRepository.getClienteMayorGasto();
    }

    public List<Integer> getClientesFueraDeRadio(Double distanciaMetros) {
        return clienteRepository.getClientesFueraDeRadio(distanciaMetros);
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------
    //2) Determinar si un cliente se encuentra dentro de una zona de cobertura.
    public String verificarZonaDeCliente(Integer idCliente) {
        return clienteRepository.verificarZonaDeCliente(idCliente);
    }

    //6)Determinar los clientes que están a más de 5km de cualquier empresa
    public List<ClienteLejanoDTO> getClientesLejanosDeTodasLasEmpresas(){
        return clienteRepository.getClientesLejanosDeTodasLasEmpresas();
    }


}
