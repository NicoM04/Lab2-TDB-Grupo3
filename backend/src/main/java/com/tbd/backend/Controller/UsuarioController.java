package com.tbd.backend.Controller;

import com.tbd.backend.DTO.UsuarioDTO;
import com.tbd.backend.Entity.Usuario;
import com.tbd.backend.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioOpt = usuarioService.getById(id);
        return usuarioOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.getAll();
    }

    // Buscar cliente por correo
    @GetMapping("/buscar/correo")
    public ResponseEntity<Long> buscarIdPorCorreo(@RequestParam String correo) {
        Long id = usuarioService.buscarIdPorCorreo(correo);
        if (id != null) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Actualizar usuario
    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        try {
            Usuario actualizado = usuarioService.update(id, usuarioDetails);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar usuario
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Registro
    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@RequestBody UsuarioDTO dto) {
        try {
            Usuario registrado = usuarioService.register(dto);
            return ResponseEntity.ok(registrado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login (devuelve el usuario completo)
    @PostMapping("/login")
    public ResponseEntity<Object> loginUsuario(@RequestBody Usuario usuarioLogin) {
        return usuarioService.login(usuarioLogin.getCorreo(), usuarioLogin.getContrasena());
    }

}
