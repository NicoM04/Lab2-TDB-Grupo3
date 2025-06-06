<template>
  <div class="notificaciones-container">
    <h2>Notificaciones</h2>

    <!-- Lista de Notificaciones -->
    <section v-if="notificaciones.length > 0" class="notifications-list">
      <div
        class="notification-item"
        v-for="(notificacion, index) in notificaciones"
        :key="index"
      >
        <div class="notification-header">
          <strong>{{ notificacion.titulo }}</strong>
          <span>{{ formatDate(notificacion.fecha) }}</span>
        </div>
        <p>{{ notificacion.mensaje }}</p>
        <button @click="marcarComoLeida(index)">Marcar como leída</button>
      </div>
    </section>

    <!-- Mensaje cuando no hay notificaciones -->
    <p v-else>No tienes notificaciones por el momento.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      notificaciones: [], // Lista de notificaciones
    };
  },
  created() {
    this.obtenerNotificaciones();
  },
  methods: {
    // Obtener las notificaciones del backend
    async obtenerNotificaciones() {
      try {
        const response = await axios.get("/api/notificaciones");
        this.notificaciones = response.data;
      } catch (error) {
        console.error("Error al obtener las notificaciones", error);
      }
    },

    // Marcar una notificación como leída
    marcarComoLeida(index) {
      // Actualiza el estado de la notificación en el frontend (y también se puede actualizar en el backend)
      this.notificaciones[index].leida = true;

      // Aquí se podría hacer un PUT para marcar la notificación como leída en el backend
      axios.put(`/api/notificaciones/${this.notificaciones[index].id}/marcar-leida`);
    },

    // Formatear la fecha de la notificación
    formatDate(fecha) {
      const date = new Date(fecha);
      return date.toLocaleDateString();
    },
  },
};
</script>

<style scoped>
.notificaciones-container {
  padding: 20px;
}

.notifications-list {
  margin-bottom: 20px;
}

.notification-item {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
}

.notification-item p {
  margin: 5px 0;
}

button {
  padding: 6px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
