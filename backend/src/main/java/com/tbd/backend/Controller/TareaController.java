package com.tbd.backend.Controller;

import com.tbd.backend.Entity.Tarea;
import com.tbd.backend.Service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin("*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    // Crear tarea
    @PostMapping("/create")
    public ResponseEntity<Tarea> crearTarea(@RequestBody Tarea tarea) {
        return ResponseEntity.ok(tareaService.crearTarea(tarea));
    }

    // Obtener tareas por usuario
    @GetMapping("/getTareasByUsuario/{usuarioId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaService.obtenerTareasPorUsuario(usuarioId));
    }

    @GetMapping("/getTareasByUsuarioFiltro/{usuarioId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) Boolean completada,
            @RequestParam(required = false) String palabraClave
    ) {
        return ResponseEntity.ok(
                tareaService.obtenerTareasPorUsuarioConFiltros(usuarioId, completada, palabraClave)
        );
    }

    // Obtener tareas por sector
    @GetMapping("/getTareasBySector/{sectorId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorSector(@PathVariable Long sectorId) {
        return ResponseEntity.ok(tareaService.obtenerTareasPorSector(sectorId));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Tarea> obtenerTareaPorId(@PathVariable Long id) {
        Optional<Tarea> tareaOpt = tareaService.obtenerTareaPorId(id);
        return tareaOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Editar tarea (Se le pasan todos los campos de la tarea)
    @PutMapping("/update/{tareaId}")
    public ResponseEntity<Tarea> editarTarea(@PathVariable Long tareaId, @RequestBody Tarea tareaDetalles) {
        return ResponseEntity.ok(tareaService.editarTarea(tareaId, tareaDetalles));
    }

    // Eliminar tarea
    @DeleteMapping("/{idTarea}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long idTarea) {
        tareaService.eliminarTarea(idTarea);
        return ResponseEntity.noContent().build();
    }

    // Marcar como completada
    @PatchMapping("/marcarTareaComoCompletada/{tareaId}")
    public ResponseEntity<Tarea> marcarCompletada(@PathVariable Long tareaId) {
        return ResponseEntity.ok(tareaService.marcarCompletada(tareaId));
    }

    // Listar tareas completadas
    @GetMapping("/getTareasCompletadas")
    public ResponseEntity<List<Tarea>> listarTareasCompletadas() {
        return ResponseEntity.ok(tareaService.listarTareasCompletadas());
    }

    // Listar tareas pendientes
    @GetMapping("/getTareasPendientes")
    public ResponseEntity<List<Tarea>> listarTareasPendientes() {
        return ResponseEntity.ok(tareaService.listarTareasPendientes());
    }

    // Buscar tareas por palabra clave (en nombre o descripción)
    @GetMapping("/buscarPorPalabraClave")
    public ResponseEntity<List<Tarea>> buscarPorPalabraClave(@RequestParam String palabra) {
        return ResponseEntity.ok(tareaService.buscarPorPalabraClave(palabra));
    }

    // Filtro combinado: estado y palabra clave
    @GetMapping("/filtrar")
    public ResponseEntity<List<Tarea>> filtrarPorEstadoYPalabraClave(
            @RequestParam(required = false) Boolean completada,
            @RequestParam(required = false) String palabraClave
    ) {
        return ResponseEntity.ok(tareaService.filtrarPorEstadoYPalabraClave(completada, palabraClave));
    }

    @GetMapping("/filtrarPaginado")
    public ResponseEntity<Page<Tarea>> filtrarPaginado(
            @RequestParam(required = false) Boolean completada,
            @RequestParam(required = false) String palabraClave,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tarea> resultado = tareaService.filtrarPorEstadoYPalabraClavePaginado(completada, palabraClave, pageable);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/getTareasByUsuarioPaginado/{usuarioId}")
    public ResponseEntity<Page<Tarea>> getTareasByUsuarioPaginado(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(tareaService.obtenerTareasPorUsuario(usuarioId, pageable));
    }

    @GetMapping("/getTareasBySectorPaginado/{sectorId}")
    public ResponseEntity<Page<Tarea>> getTareasBySectorPaginado(
            @PathVariable Long sectorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(tareaService.obtenerTareasPorSector(sectorId, pageable));
    }

    @GetMapping("/getTareasCompletadasPaginado")
    public ResponseEntity<Page<Tarea>> listarTareasCompletadasPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(tareaService.listarTareasCompletadas(pageable));
    }

    @GetMapping("/getTareasPendientesPaginado")
    public ResponseEntity<Page<Tarea>> listarTareasPendientesPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(tareaService.listarTareasPendientes(pageable));
    }

    @GetMapping("/buscarPorPalabraClavePaginado")
    public ResponseEntity<Page<Tarea>> buscarPorPalabraClavePaginado(
            @RequestParam String palabra,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(tareaService.buscarPorPalabraClavePaginado(palabra, pageable));
    }

    // ------------------------------ PREGUNTAS -------------------------

    @GetMapping("/tareasPorSector/{usuarioId}")
    public ResponseEntity<List<Map<String, Object>>> tareasPorSector(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaService.contarTareasPorSectorUsuario(usuarioId));
    }

    @GetMapping("/tareaPendientesDelUsuario/{usuarioId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPendientesPorUsuario(@PathVariable Long usuarioId) {
        List<Tarea> tareasPendientes = tareaService.tareaPendienteDelUsuario(usuarioId);
        if (tareasPendientes.isEmpty()) {
            return ResponseEntity.noContent().build(); // o ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(tareasPendientes);
    }


    @GetMapping("/sectorMasTareasEnRadio/{usuarioId}")
    public ResponseEntity<Map<String, Object>> sectorMasTareasEnRadio(@PathVariable Long usuarioId) {
        Map<String, Object> result = tareaService.sectorConMasTareasEnRadio(usuarioId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/promedioDistanciaCompletadas/{usuarioId}")
    public ResponseEntity<Double> promedioDistancia(@PathVariable Long usuarioId) {
        Double result = tareaService.promedioDistanciaTareasCompletadas(usuarioId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/sectoresMasTareasPendientes")
    public ResponseEntity<List<Map<String, Object>>> sectoresMasPendientes() {
        return ResponseEntity.ok(tareaService.sectoresConMasTareasPendientes());
    }

    @GetMapping("/tareaMasCercana/{usuarioId}")
    public ResponseEntity<Tarea> tareaMasCercana(@PathVariable Long usuarioId) {
        Tarea tarea = tareaService.tareaPendienteMasCercana(usuarioId);
        return tarea != null ? ResponseEntity.ok(tarea) : ResponseEntity.notFound().build();
    }

    @GetMapping("/tareasPorUsuarioYSector")
    public ResponseEntity<List<Map<String, Object>>> tareasPorUsuarioYSector() {
        return ResponseEntity.ok(tareaService.contarTareasPorUsuarioYSector());
    }

    @GetMapping("/sectorMasTareasCompletadas5km/{usuarioId}")
    public ResponseEntity<Map<String, Object>> sectorMasCompletadas5km(@PathVariable Long usuarioId) {
        Map<String, Object> result = tareaService.sectorConMasCompletadasEnRadio5km(usuarioId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/promedioDistanciaGlobalCompletadas/{usuarioId}")
    public ResponseEntity<Double> promedioDistanciaGlobal(@PathVariable Long usuarioId) {
        Double result = tareaService.promedioDistanciaTodasCompletadasAUsuario(usuarioId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }


}
