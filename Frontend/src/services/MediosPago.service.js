import httpClient from "@/http-common";

// Crear un nuevo medio de pago
const createMedioDePago = (medioDePago, token) => {
  return httpClient.post('/mediosdepago/create', medioDePago, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener todos los medios de pago
const getAllMediosDePago = (token) => {
  return httpClient.get('/mediosdepago/getAll', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener un medio de pago por ID
const getMedioDePagoById = (id, token) => {
  return httpClient.get(`/mediosdepago/getById/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Actualizar un medio de pago por ID
const updateMedioDePago = (id, medioDePago, token) => {
  return httpClient.put(`/mediosdepago/update/${id}`, medioDePago, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Eliminar un medio de pago por ID
const deleteMedioDePago = (id, token) => {
  return httpClient.delete(`/mediosdepago/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

// Obtener el medio de pago más utilizado en pedidos urgentes
const getMedioDePagoMasUtilizadoUrgentes = (token) => {
  return httpClient.get('/mediosdepago/masutilizadourgentes', {
    headers: {  
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  createMedioDePago,
  getAllMediosDePago,
  getMedioDePagoById,
  updateMedioDePago,
  deleteMedioDePago,
  getMedioDePagoMasUtilizadoUrgentes,
};
