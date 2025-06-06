import http from "../http-common";

const baseUrl = "/api/sector";

const crearSector = (sector, token) => {
  return http.post(`${baseUrl}/create`, sector, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const obtenerTodos = (token) => {
  return http.get(`${baseUrl}/getAll`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const obtenerPorId = (id, token) => {
  return http.get(`${baseUrl}/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const actualizarSector = (id, sectorActualizado, token) => {
  return http.put(`${baseUrl}/${id}`, sectorActualizado, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const eliminarSector = (id, token) => {
  return http.delete(`${baseUrl}/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  crearSector,
  obtenerTodos,
  obtenerPorId,
  actualizarSector,
  eliminarSector,
};
