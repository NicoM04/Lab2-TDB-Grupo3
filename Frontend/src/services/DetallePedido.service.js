import httpClient from "@/http-common";

// Crear un nuevo detalle de pedido
const createDetalle = (detalle, token) => {
  return httpClient.post('/detallepedido/create', detalle, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todos los detalles de pedido
const getAllDetalles = (token) => {
  return httpClient.get('/detallepedido/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener un detalle de pedido por ID
const getDetalleById = (id, token) => {
  return httpClient.get(`/detallepedido/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar un detalle de pedido
const updateDetalle = (id, detalle, token) => {
  return httpClient.put(`/detallepedido/update/${id}`, detalle, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar un detalle de pedido
const deleteDetalle = (id, token) => {
  return httpClient.delete(`/detallepedido/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  createDetalle,
  getAllDetalles,
  getDetalleById,
  updateDetalle,
  deleteDetalle,
};
