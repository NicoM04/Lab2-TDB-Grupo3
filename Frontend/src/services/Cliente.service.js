import httpClient from "@/http-common";

// Registrar un nuevo cliente
const registerCliente = (cliente) => {
  return httpClient.post('/cliente/register', cliente);
};

// Iniciar sesión de un cliente
const loginCliente = (correo, contrasena) => {
  return httpClient.post('/cliente/login', { correo_cliente: correo, contrasena_cliente: contrasena });
};

// Obtener todos los clientes
const getAllClientes = (token) => {
  return httpClient.get('/cliente/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener un cliente por ID
const getClienteById = (id, token) => {
  return httpClient.get(`/cliente/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Buscar cliente por correo
const buscarPorCorreo = (correo, token) => {
  return httpClient.get(`/cliente/buscar/correo?correo=${correo}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Buscar cliente por nombre
const buscarPorNombre = (nombre, token) => {
  return httpClient.get(`/cliente/buscar/nombre?nombre=${nombre}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar un cliente
const updateCliente = (id, cliente, token) => {
  return httpClient.put(`/cliente/update/${id}`, cliente, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar un cliente
const deleteCliente = (id, token) => {
  return httpClient.delete(`/cliente/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};


// Verificar zona de cobertura
const verificarZonaCobertura = (id) => {
  return httpClient.get(`/cliente/zonaCobertura/${id}`);
};

// Clientes lejanos de todas las empresas
const getClientesLejanos = () => {
  return httpClient.get('/cliente/clientesLejanos');
};

// Clientes fuera de zona de cobertura
const getClientesFueraDeCobertura = (distancia = 5000) => {
  return httpClient.get(`/cliente/fueraDeCobertura?distancia=${distancia}`);
};


// Cliente con mayor gasto
const getClienteMayorGasto = () => {
  return httpClient.get(`/cliente/mayor-gasto`);
};


export default {
  registerCliente,
  loginCliente,
  getAllClientes,
  getClienteById,
  buscarPorCorreo,
  buscarPorNombre,
  updateCliente,
  deleteCliente,
  verificarZonaCobertura,
  getClientesLejanos,
  getClienteMayorGasto,
  getClientesFueraDeCobertura,
};

