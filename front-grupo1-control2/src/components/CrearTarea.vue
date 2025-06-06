<template>
  <v-container class="mt-8" max-width="700px">
    <v-card class="pa-6" elevation="4">
      <v-card-title class="text-h5 font-weight-bold mb-4">Crear Nueva Tarea</v-card-title>

      <v-form @submit.prevent="crearTarea">
        <v-text-field
          v-model="nombre"
          label="Título de la tarea"
          required
        />

        <v-textarea
          v-model="descripcion"
          label="Descripción"
          rows="3"
          required
        />

        <v-select
          v-model="sectorId"
          :items="sectores"
          item-title="nombre"
          item-value="id"
          label="Sector asociado"
          required
        />

        <v-text-field
          v-model="fechaTermino"
          label="Fecha de término"
          type="date"
          :min="hoy"
          required
        />

        <v-btn
          type="submit"
          color="primary"
          block
          class="mt-4"
          :loading="cargando"
        >
          Crear tarea
        </v-btn>
      </v-form>

      <v-alert v-if="mensaje" :type="mensajeTipo" class="mt-4">
        {{ mensaje }}
      </v-alert>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import tareaService from '@/services/tarea.service.js';
import sectorService from '@/services/sector.service.js';

const nombre = ref('');
const descripcion = ref('');
const fechaTermino = ref('');
const sectorId = ref(null);
const sectores = ref([]);
const cargando = ref(false);

const mensaje = ref('');
const mensajeTipo = ref('success');

// Obtener la fecha de hoy en formato YYYY-MM-DD
const hoy = new Date().toISOString().split('T')[0];

const crearTarea = async () => {
  if (!nombre.value || !descripcion.value || !fechaTermino.value || !sectorId.value) {
    mensaje.value = 'Por favor, completa todos los campos.';
    mensajeTipo.value = 'error';
    return;
  }

  cargando.value = true;
  mensaje.value = '';

  // Obtener el id del usuario logeado desde localStorage
  const usuarioId = localStorage.getItem('usuarioId');

  const tarea = {
    nombre: nombre.value,
    descripcion: descripcion.value,
    fechaTermino: fechaTermino.value,
    completada: false,
    sector: { id: sectorId.value },
    usuario: { id: usuarioId }
  };

  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    await tareaService.crearTarea(tarea, token);
    mensaje.value = 'Tarea creada correctamente.';
    mensajeTipo.value = 'success';
    // FALTA AASIGNARLE EL USUARIO ID
    // Reiniciar formulario
    nombre.value = '';
    descripcion.value = '';
    fechaTermino.value = '';
    sectorId.value = null;
  } catch (err) {
    mensaje.value = 'Error al crear tarea.';
    mensajeTipo.value = 'error';
  } finally {
    cargando.value = false;
  }
};

const obtenerSectores = async () => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    const res = await sectorService.obtenerTodos(token);
    sectores.value = res.data;
  } catch (err) {
    mensaje.value = 'Error al cargar sectores.';
    mensajeTipo.value = 'error';
  }
};

onMounted(() => {
  obtenerSectores();
});
</script>
