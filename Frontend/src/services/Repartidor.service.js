import httpClient from "@/http-common";

// Crear un nuevo repartidor
const createRepartidor = (repartidor, token) => {
  return httpClient.post('/repartidor/create', repartidor, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todos los repartidores
const getAllRepartidores = (token) => {
  return httpClient.get('/repartidor/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener un repartidor por ID
const getRepartidorById = (id, token) => {
  return httpClient.get(`/repartidor/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar un repartidor por ID
const updateRepartidor = (id, repartidor, token) => {
  return httpClient.put(`/repartidor/update/${id}`, repartidor, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar un repartidor por ID
const deleteRepartidor = (id, token) => {
  return httpClient.delete(`/repartidor/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener el tiempo promedio de entrega por repartidor
const getTiempoPromedioEntrega = (token) => {
  return httpClient.get('/repartidor/tiempopromedioentrega', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener los mejores repartidores por rendimiento
const getMejoresRepartidores = (token) => {
  return httpClient.get('/repartidor/mejoresrepartidores', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  createRepartidor,
  getAllRepartidores,
  getRepartidorById,
  updateRepartidor,
  deleteRepartidor,
  getTiempoPromedioEntrega,
  getMejoresRepartidores,
};
