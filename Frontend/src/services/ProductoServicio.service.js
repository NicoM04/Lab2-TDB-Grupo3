import httpClient from "@/http-common";

// Crear un nuevo producto o servicio
const createProducto = (producto, token) => {
  return httpClient.post('/producto/create', producto, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todos los productos o servicios
const getAllProductos = (token) => {
  return httpClient.get('/producto/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener un producto o servicio por ID
const getProductoById = (id, token) => {
  return httpClient.get(`/producto/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar un producto o servicio por ID
const updateProducto = (id, producto, token) => {
  return httpClient.put(`/producto/update/${id}`, producto, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar un producto o servicio por ID
const deleteProducto = (id, token) => {
  return httpClient.delete(`/producto/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener los productos más pedidos del último mes
const getMasPedidosUltimoMes = (token) => {
  return httpClient.get('/producto/masPedidosUltimoMes', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  createProducto,
  getAllProductos,
  getProductoById,
  updateProducto,
  deleteProducto,
  getMasPedidosUltimoMes,
};
