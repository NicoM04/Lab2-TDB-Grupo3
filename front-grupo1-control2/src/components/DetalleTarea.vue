<template>
  <v-container class="mt-8" max-width="700px">
    <v-card class="pa-6" elevation="3" v-if="tarea">
      <v-card-title class="text-h5 font-weight-bold">
        Detalle de la Tarea
      </v-card-title>

      <v-card-text>
        <p><strong>Nombre:</strong> {{ tarea.nombre }}</p>
        <p><strong>Descripción:</strong> {{ tarea.descripcion }}</p>
        <p><strong>Fecha término:</strong> {{ formatearFecha(tarea.fechaTermino) }}</p>
        <p><strong>Estado:</strong> {{ tarea.completada ? 'Completada' : 'Pendiente' }}</p>
        <p><strong>Sector:</strong> {{ tarea.sector?.nombre || 'Sin asignar' }}</p>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" @click="volver">Volver</v-btn>
      </v-card-actions>
    </v-card>

    <v-alert v-else type="error">
      No se pudo cargar la tarea.
    </v-alert>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import tareaService from '@/services/tarea.service.js';

const route = useRoute();
const router = useRouter();
const tarea = ref(null);

const cargarTarea = async () => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    const res = await tareaService.getTareaById(route.params.id, token);
    tarea.value = res.data;
  } catch (err) {
    console.error('Error al cargar tarea:', err);
    tarea.value = null;
  }
};

const volver = () => {
  router.back();
};

const formatearFecha = (fecha) => {
  return new Date(fecha).toLocaleDateString();
};

onMounted(() => {
  cargarTarea();
});
</script>
