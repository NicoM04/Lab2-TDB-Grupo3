import httpClient from "@/http-common";

// Crear una nueva empresa
const createEmpresa = (empresa, token) => {
  return httpClient.post('/empresas', empresa, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todas las empresas
const getEmpresas = (token) => {
  return httpClient.get('/empresas', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar una empresa por ID
const updateEmpresa = (id, empresa, token) => {
  return httpClient.put(`/empresas/${id}`, empresa, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar una empresa por ID
const deleteEmpresa = (id, token) => {
  return httpClient.delete(`/empresas/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener las empresas con más fallos
const getEmpresasConMasFallos = (token) => {
  return httpClient.get('/empresas/mas-fallos', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  createEmpresa,
  getEmpresas,
  updateEmpresa,
  deleteEmpresa,
  getEmpresasConMasFallos,
};
