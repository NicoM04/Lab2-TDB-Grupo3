<template>
  <v-container class="mt-8" max-width="700px">
    <v-card class="pa-6" elevation="4">
      <v-card-title class="text-h5 font-weight-bold mb-4">Editar Tarea</v-card-title>

      <v-form @submit.prevent="editarTarea" v-if="cargado">
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
          required
        />

        <v-btn
          type="submit"
          color="primary"
          block
          class="mt-4"
          :loading="cargando"
        >
          Guardar cambios
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
import { useRoute, useRouter } from 'vue-router';
import tareaService from '@/services/tarea.service.js';
import sectorService from '@/services/sector.service.js';

const route = useRoute();
const router = useRouter();
const tareaId = route.params.id;

const nombre = ref('');
const descripcion = ref('');
const fechaTermino = ref('');
const sectorId = ref(null);
const sectores = ref([]);
const cargado = ref(false);
const cargando = ref(false);

const mensaje = ref('');
const mensajeTipo = ref('success');

const cargarTarea = async () => {
  const token = localStorage.getItem("jwt");
  try {
    // Usar getTareaById para obtener la tarea específica
    const res = await tareaService.getTareaById(tareaId, token);
    const tarea = res.data;
    nombre.value = tarea.nombre;
    descripcion.value = tarea.descripcion;
    fechaTermino.value = tarea.fechaTermino.slice(0, 10);
    sectorId.value = tarea.sector.id;
    cargado.value = true;
  } catch (err) {
    mensaje.value = 'Error al cargar la tarea.';
    mensajeTipo.value = 'error';
  }
};

const editarTarea = async () => {
  const token = localStorage.getItem("jwt");
  try {
    cargando.value = true;
    const tareaActualizada = {
      nombre: nombre.value,
      descripcion: descripcion.value,
      fechaTermino: fechaTermino.value,
      sector: { id: sectorId.value },
    };
    await tareaService.editarTarea(tareaId, tareaActualizada, token);
    mensaje.value = 'Tarea actualizada correctamente.';
    mensajeTipo.value = 'success';
  } catch (err) {
    mensaje.value = 'Error al actualizar la tarea.';
    mensajeTipo.value = 'error';
  } finally {
    cargando.value = false;
  }
};

const cargarSectores = async () => {
  const token = localStorage.getItem("jwt");
  try {
    const res = await sectorService.obtenerTodos(token);
    sectores.value = res.data;
  } catch (err) {
    mensaje.value = 'Error al cargar sectores.';
    mensajeTipo.value = 'error';
  }
};

onMounted(() => {
  cargarSectores();
  cargarTarea();
});
</script>