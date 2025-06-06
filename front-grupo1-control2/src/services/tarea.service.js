import http from "../http-common";

const baseUrl = "/api/tareas";

// ----------- CRUD -----------

const crearTarea = (tarea, token) =>
  http.post(`${baseUrl}/create`, tarea, {
    headers: { Authorization: `Bearer ${token}` },
  });

const editarTarea = (id, tareaActualizada, token) =>
  http.put(`${baseUrl}/update/${id}`, tareaActualizada, {
    headers: { Authorization: `Bearer ${token}` },
  });

const eliminarTarea = (id, token) =>
  http.delete(`${baseUrl}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const marcarTareaComoCompletada = (id, token) =>
  http.patch(`${baseUrl}/marcarTareaComoCompletada/${id}`, null, {
    headers: { Authorization: `Bearer ${token}` },
  });

// ----------- Obtener tareas por relaciones -----------

const getTareasByUsuario = (usuarioId, token) =>
  http.get(`${baseUrl}/getTareasByUsuario/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareasBySector = (sectorId, token) =>
  http.get(`${baseUrl}/getTareasBySector/${sectorId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareaById = (id, token) =>
  http.get(`/api/tareas/getById/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

// ----------- Filtros simples -----------

const getTareasCompletadas = (token) =>
  http.get(`${baseUrl}/getTareasCompletadas`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareasPendientes = (token) =>
  http.get(`${baseUrl}/getTareasPendientes`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const buscarPorPalabraClave = (palabra, token) =>
  http.get(`${baseUrl}/buscarPorPalabraClave`, {
    params: { palabra },
    headers: { Authorization: `Bearer ${token}` },
  });

const filtrarTareas = (completada, palabraClave, token) =>
  http.get(`${baseUrl}/filtrar`, {
    params: { completada, palabraClave },
    headers: { Authorization: `Bearer ${token}` },
  });

const filtrarTareasPorUsuario = (usuarioId, completada, palabraClave, token) =>
  http.get(`${baseUrl}/getTareasByUsuarioFiltro/${usuarioId}`, {
    params: { completada, palabraClave },
    headers: { Authorization: `Bearer ${token}` },
  });

// ----------- Filtros paginados -----------

const filtrarTareasPaginado = (completada, palabraClave, page = 0, size = 10, token) =>
  http.get(`${baseUrl}/filtrarPaginado`, {
    params: { completada, palabraClave, page, size },
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareasByUsuarioPaginado = (usuarioId, page = 0, size = 10, token) =>
  http.get(`${baseUrl}/getTareasByUsuarioPaginado/${usuarioId}`, {
    params: { page, size },
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareasBySectorPaginado = (sectorId, page = 0, size = 10, token) =>
  http.get(`${baseUrl}/getTareasBySectorPaginado/${sectorId}`, {
    params: { page, size },
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareasCompletadasPaginado = (page = 0, size = 10, token) =>
  http.get(`${baseUrl}/getTareasCompletadasPaginado`, {
    params: { page, size },
    headers: { Authorization: `Bearer ${token}` },
  });

const getTareasPendientesPaginado = (page = 0, size = 10, token) =>
  http.get(`${baseUrl}/getTareasPendientesPaginado`, {
    params: { page, size },
    headers: { Authorization: `Bearer ${token}` },
  });

const buscarPorPalabraClavePaginado = (palabra, page = 0, size = 10, token) =>
  http.get(`${baseUrl}/buscarPorPalabraClavePaginado`, {
    params: { palabra, page, size },
    headers: { Authorization: `Bearer ${token}` },
  });

// ----------- PREGUNTAS estadísticas -----------

const tareasPorSector = (usuarioId, token) =>
  http.get(`${baseUrl}/tareasPorSector/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });


const tareasPendientesDelUsuario = (usuarioId, token) =>
  http.get(`${baseUrl}/tareaPendientesDelUsuario/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });


const sectorMasTareasEnRadio = (usuarioId, token) =>
  http.get(`${baseUrl}/sectorMasTareasEnRadio/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const promedioDistanciaCompletadas = (usuarioId, token) =>
  http.get(`${baseUrl}/promedioDistanciaCompletadas/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const sectoresMasTareasPendientes = (token) =>
  http.get(`${baseUrl}/sectoresMasTareasPendientes`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const tareaMasCercana = (usuarioId, token) =>
  http.get(`${baseUrl}/tareaMasCercana/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const tareaPendienteCercanaUbicacion = (usuarioId, token) =>
  http.get(`${baseUrl}/tareaPendienteCercanaUbicacion/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const tareasPorUsuarioYSector = (token) =>
  http.get(`${baseUrl}/tareasPorUsuarioYSector`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const sectorMasCompletadas5km = (usuarioId, token) =>
  http.get(`${baseUrl}/sectorMasTareasCompletadas5km/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

const promedioDistanciaGlobal = (usuarioId, token) =>
  http.get(`${baseUrl}/promedioDistanciaGlobalCompletadas/${usuarioId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

// ----------- Exportar -----------

export default {
  crearTarea,
  editarTarea,
  eliminarTarea,
  marcarTareaComoCompletada,
  getTareasByUsuario,
  getTareasBySector,
  getTareasCompletadas,
  getTareasPendientes,
  getTareaById,
  buscarPorPalabraClave,
  filtrarTareas,
  filtrarTareasPorUsuario,
  filtrarTareasPaginado,
  getTareasByUsuarioPaginado,
  getTareasBySectorPaginado,
  getTareasCompletadasPaginado,
  getTareasPendientesPaginado,
  buscarPorPalabraClavePaginado,
  tareasPorSector,
  tareaMasCercana,
  sectorMasTareasEnRadio,
  promedioDistanciaCompletadas,
  sectoresMasTareasPendientes,
  tareaPendienteCercanaUbicacion,
  tareasPorUsuarioYSector,
  sectorMasCompletadas5km,
  promedioDistanciaGlobal,
  tareasPendientesDelUsuario
};