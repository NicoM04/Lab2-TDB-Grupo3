import httpClient from "@/http-common";

// Crear una nueva notificación
const createNotificacion = (notificacion, token) => {
  return httpClient.post('/notificaciones', notificacion, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todas las notificaciones
const getAllNotificaciones = (token) => {
  return httpClient.get('/notificaciones', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar una notificación por ID
const updateNotificacion = (id, notificacion, token) => {
  return httpClient.put(`/notificaciones/${id}`, notificacion, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar una notificación por ID
const deleteNotificacion = (id, token) => {
  return httpClient.delete(`/notificaciones/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  createNotificacion,
  getAllNotificaciones,
  updateNotificacion,
  deleteNotificacion,
};
