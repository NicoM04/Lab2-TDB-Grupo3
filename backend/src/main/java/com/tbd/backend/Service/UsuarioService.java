package com.tbd.backend.Service;

import com.tbd.backend.Config.InputVerificationService;
import com.tbd.backend.Config.JwtMiddlewareService;
import com.tbd.backend.DTO.UsuarioDTO;
import com.tbd.backend.Entity.Usuario;
import com.tbd.backend.Repository.UsuarioRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private JwtMiddlewareService jwtMiddlewareService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> getById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Buscar usuario por correo
    public Long buscarIdPorCorreo(String correo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        return usuarioOpt.map(Usuario::getId).orElse(null);
    }

    // Método privado para convertir entidad a DTO
    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setCorreo(usuario.getCorreo());
        dto.setContrasena(usuario.getContrasena()); // En prod, quizás no mandar la contraseña
        dto.setX(usuario.getUbicacion().getX());
        dto.setY(usuario.getUbicacion().getY());
        return dto;
    }

    // Actualizar usuario
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setUsername(usuarioDetails.getUsername());
        usuario.setCorreo(usuarioDetails.getCorreo());
        usuario.setContrasena(usuarioDetails.getContrasena());
        usuario.setUbicacion(usuarioDetails.getUbicacion());
        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario register(UsuarioDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Correo ya registrado");
        }
            Usuario usuario = new Usuario();
            usuario.setUsername(dto.getUsername());
            usuario.setCorreo(dto.getCorreo());

            // Encriptar contraseña
            String encryptedPassword = passwordEncoder.encode(dto.getContrasena());
            usuario.setContrasena(encryptedPassword);

            // Crear ubicación geográfica
            Point ubicacion = geometryFactory.createPoint(new Coordinate(dto.getX(), dto.getY()));
            ubicacion.setSRID(4326);
            usuario.setUbicacion(ubicacion);

        return usuarioRepository.save(usuario);
    }


    // Login - validar correo y contraseña
    public ResponseEntity<Object> login(String correo, String contrasena) {

        if (!InputVerificationService.validateInput(correo) || !InputVerificationService.validateInput(contrasena)) {
            return ResponseEntity.badRequest().body("Error al iniciar sesión: caracteres no permitidos.");
        }

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Usuario no encontrado.");
            }

            Usuario usuario = usuarioOpt.get();
            String storedPassword = usuario.getContrasena();

            if (correo.endsWith("@example.com")) {
                if (contrasena.equals(storedPassword)) {
                    String token = jwtMiddlewareService.generateToken(usuario);
                    return ResponseEntity.ok(token);
                } else {
                    return ResponseEntity.status(401).body("Contraseña incorrecta.");
                }
            }

            if (!passwordEncoder.matches(contrasena, storedPassword)) {
                return ResponseEntity.status(401).body("Contraseña incorrecta.");
            }

            String token = jwtMiddlewareService.generateToken(usuario);
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al iniciar sesión: " + e.getMessage());
        }
    }

}
