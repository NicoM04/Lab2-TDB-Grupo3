<template>
  <v-container class="mt-8">
    <h1 class="text-h5 font-weight-bold mb-4">Notificaciones (tareas que vencen dentro de 1 día)</h1>

    <v-row>
      <v-col cols="12" md="6" v-for="notif in notificaciones" :key="notif.id">
        <v-card :color="notif.leida ? 'grey-lighten-4' : 'blue-lighten-5'" class="mb-4">
          <v-card-title class="text-subtitle-1">
            {{ notif.mensaje }}
          </v-card-title>
          <v-card-text>
            <p><strong>Tarea:</strong> {{ notif.tarea?.nombre || 'Desconocida' }}</p>
            <p><strong>Leída:</strong> {{ notif.leida ? 'Sí' : 'No' }}</p>
          </v-card-text>
          <v-card-actions>
            <v-btn
              v-if="!notif.leida"
              color="primary"
              @click="marcarComoLeida(notif.id)"
            >
              Marcar como leída
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <v-alert
      v-if="notificaciones.length === 0"
      type="info"
      class="mt-8"
      border="start"
      elevation="2"
      icon="mdi-bell-off-outline"
      style="max-width: 400px;"
    >
      <div>
        <span class="text-h6 font-weight-medium">No hay notificaciones disponibles</span>
        <div class="mt-2 text-body-2">
          ¡Estás al día!<br>
        </div>
      </div>
    </v-alert>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import notificacionService from '@/services/notificacion.service.js';

// Obtener el ID del usuario logueado desde localStorage
const usuarioId = localStorage.getItem('usuarioId');
const notificaciones = ref([]);

const cargarNotificaciones = async () => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    await notificacionService.generarNotificacionesProximas(token);
    const res = await notificacionService.obtenerPorUsuario(usuarioId, token);
    // Filtrar notificaciones cuya tarea NO esté completada
    notificaciones.value = res.data.filter(n => !n.tarea?.completada);
  } catch (err) {
    console.error('Error al cargar notificaciones:', err);
  }
};

const marcarComoLeida = async (id) => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    await notificacionService.marcarComoLeida(id, token);
    await cargarNotificaciones();
  } catch (err) {
    console.error('Error al marcar como leída:', err);
  }
};

onMounted(() => {
  cargarNotificaciones();
});
</script>
