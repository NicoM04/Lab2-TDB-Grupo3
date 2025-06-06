<template>
  <v-container>
    <h1 class="text-h4 font-weight-bold mb-4">Lista de Tareas</h1>

    <!-- Filtro -->
    <FiltroBusqueda @filtrar="recibirFiltros" />

    <!-- Lista de tareas -->
    <v-row>
      <v-col cols="12" md="6" v-for="tarea in tareas" :key="tarea.id">
        <TareaCard
          :tarea="tarea"
          @onCompletar="marcarCompletada"
          @onEliminar="eliminarTarea"
        />
        <v-btn
          color="warning"
          class="mt-2"
          @click="editarTarea(tarea.id)"
          block
        >
          Editar
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import tareaService from '@/services/tarea.service.js';
import FiltroBusqueda from './FiltroBusqueda.vue';
import TareaCard from './TareaCard.vue';

const tareas = ref([]);
const estado = ref(null);
const palabraClave = ref('');
const router = useRouter();

// Obtener el id del usuario logeado desde localStorage
const usuarioId = localStorage.getItem('usuarioId');

// Obtener tareas según filtros (solo del usuario)
const buscarTareas = async () => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    const response = await tareaService.filtrarTareasPorUsuario(
      usuarioId,
      estado.value,
      palabraClave.value,
      token
    );
    tareas.value = response.data;
  } catch (error) {
    console.error('Error al filtrar tareas:', error);
  }
};

// Recibir filtros desde el componente hijo
const recibirFiltros = ({ estado: nuevoEstado, palabra }) => {
  estado.value = nuevoEstado;
  palabraClave.value = palabra;
  buscarTareas();
};

// Acciones
const marcarCompletada = async (id) => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    await tareaService.marcarTareaComoCompletada(id, token);
    buscarTareas();
  } catch (err) {
    console.error('Error al marcar tarea:', err);
  }
};

const eliminarTarea = async (id) => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    await tareaService.eliminarTarea(id, token);
    buscarTareas();
  } catch (err) {
    console.error('Error al eliminar tarea:', err);
  }
};

const editarTarea = (id) => {
  router.push(`/tareas/editar/${id}`);
};

onMounted(() => {
  buscarTareas();
});
</script>
