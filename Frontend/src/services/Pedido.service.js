import httpClient from "@/http-common";

// Crear un nuevo pedido
const createPedido = (pedido, token) => {
  return httpClient.post('/pedido/create', pedido, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todos los pedidos
const getAllPedidos = (token) => {
  return httpClient.get('/pedido/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener un pedido por ID
const getPedidoById = (id, token) => {
  return httpClient.get(`/pedido/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar un pedido por ID
const updatePedido = (id, pedido, token) => {
  return httpClient.put(`/pedido/update/${id}`, pedido, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar un pedido por ID
const deletePedido = (id, token) => {
  return httpClient.delete(`/pedido/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Registrar un pedido
const registrarPedido = (pedido, token) => {
  return httpClient.post('/pedido/registrar', pedido, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Confirmar un pedido y descontar el stock
const confirmarPedidoYDescontarStock = (id, token) => {
  return httpClient.put(`/pedido/confirmarPedidoYDescontarStock/${id}`, null, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Cambiar el estado de un pedido
const cambiarEstadoPedido = (id, nuevoEstado, token) => {
  return httpClient.put(`/pedido/cambiarEstadoPedido/${id}`, { nuevoEstado }, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const obtenerPedidosCliente = (idCliente, token) => {
  return httpClient.get(`/pedido/cliente/${idCliente}`, {
    headers: {
      Authorization: `Bearer ${token}`, // Usamos el token para la autenticación
    },
  });
};


//Obtener pedidos más cercanos a una empresa
const getPedidosMasCercanosEmpresa = (idEmpresa, limite = 5) => {
  return httpClient.get(`/pedido/masCercanosEmpresa/${idEmpresa}?limite=${limite}`);
};

//Obtener pedidos más lejanos por empresa
const getPedidosMasLejanosPorEmpresa = () => {
  return httpClient.get("/pedido/masLejanosPorEmpresa");
};

// Obtener pedidos que cruzan más de dos zonas de reparto
const getPedidosConMasDeDosZonas = () => {
  return httpClient.get("/pedido/pedidosConMasDeDosZonas");
};


export default {
  createPedido,
  getAllPedidos,
  getPedidoById,
  updatePedido,
  deletePedido,
  registrarPedido,
  confirmarPedidoYDescontarStock,
  cambiarEstadoPedido,
  obtenerPedidosCliente,
  getPedidosMasCercanosEmpresa,
  getPedidosMasLejanosPorEmpresa,
  getPedidosConMasDeDosZonas
};
