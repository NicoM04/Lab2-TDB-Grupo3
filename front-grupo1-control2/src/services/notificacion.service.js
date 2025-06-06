import http from "../http-common";

const baseUrl = "/api/notificaciones";

const crearNotificacion = (notificacion, token) => {
  return http.post(baseUrl, notificacion, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const obtenerTodas = (token) => {
  return http.get(baseUrl, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const obtenerPorId = (id, token) => {
  return http.get(`${baseUrl}/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const eliminarNotificacion = (id, token) => {
  return http.delete(`${baseUrl}/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const marcarComoLeida = (id, token) => {
  return http.put(`${baseUrl}/${id}/leer`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const obtenerPorUsuario = (usuarioId, token) => {
  return http.get(`${baseUrl}/getNotificacionByUsuario/${usuarioId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const generarNotificacionesProximas = (token) => {
  return http.post(`${baseUrl}/generarNotificaciones`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  crearNotificacion,
  obtenerTodas,
  obtenerPorId,
  eliminarNotificacion,
  marcarComoLeida,
  obtenerPorUsuario,
  generarNotificacionesProximas
};
