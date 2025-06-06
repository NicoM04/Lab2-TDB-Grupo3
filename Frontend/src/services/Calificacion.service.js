import httpClient from "@/http-common";

// Crear una calificación
const createCalificacion = (calificacion, token) => {
  return httpClient.post('/calificacion/create', calificacion, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todas las calificaciones
const getAllCalificaciones = (token) => {
  return httpClient.get('/calificacion/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener calificación por ID
const getCalificacionById = (id, token) => {
  return httpClient.get(`/calificacion/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar calificación
const updateCalificacion = (id, calificacion, token) => {
  return httpClient.put(`/calificacion/update/${id}`, calificacion, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar calificación
const deleteCalificacion = (id, token) => {
  return httpClient.delete(`/calificacion/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener calificaciones por id_repartidor
const getCalificacionesByRepartidorId = (idRepartidor, token) => {
  return httpClient.get(`/calificacion/getAllByRepartidor/${idRepartidor}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};


export default {
  createCalificacion,
  getAllCalificaciones,
  getCalificacionById,
  updateCalificacion,
  deleteCalificacion,
  getCalificacionesByRepartidorId,
};
