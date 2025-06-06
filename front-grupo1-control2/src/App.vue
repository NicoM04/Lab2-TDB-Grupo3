<template>
  <div id="app">
    <!-- Barra de navegación fija en la parte superior -->
    <nav class="navbar" v-if="!$route.meta.hideNavBar">
      <ul>
        <li><router-link to="/tareas">Tareas</router-link></li>
        <li><router-link to="/tareas/crear">Crear Tarea</router-link></li>
        <li><router-link to="/notificaciones">Notificaciones <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span></router-link></li>
        <li><router-link to="/estadisticas">Estadisticas</router-link></li>
        <li>
          <button @click="cerrarSesion" class="logout-btn">
            Cerrar sesión
          </button>
        </li>
      </ul>
    </nav>
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import notificacionService from './services/notificacion.service';
export default {
  name: "App",
  data() {
    return {
      unreadCount: 0,
    };
  },
  created() {
    this.loadUnreadNotifications(); // Cargar las notificaciones no leídas al montar el componente
  },
  methods: {
    async loadUnreadNotifications() {
      if (!localStorage.getItem("id")) {
        return;
      }
      try {
        this.unreadCount = (await notificacionService.checkCantidadNoLeidas(localStorage.getItem("id"))).data;
        console.log("Notificaciones no leídas:", this.unreadCount);
      } catch (error) {
        console.error("Error al cargar las notificaciones:", error);
      }
    },
    cerrarSesion() {
      localStorage.removeItem("jwt");
      localStorage.removeItem("usuarioId");
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #2c3e50;
  color: white;
  padding: 10px 0;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.navbar ul {
  list-style: none;
  display: flex;
  justify-content: center;
  margin: 0;
  padding: 0;
}

.navbar ul li {
  margin: 0 20px;
}

.navbar ul li a {
  color: white;
  text-decoration: none;
  font-size: 1.1rem;
}

.navbar ul li a:hover {
  text-decoration: underline;
}

.badge {
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 0.3em 0.6em;
  font-size: 0.9rem;
  font-weight: bold;
  margin-left: 5px;
  text-align: center;
  min-width: 1.5em;
}

.badge::after {
  content: "";
  display: inline-block;
  vertical-align: middle;
}

.content {
  margin-top: 60px;
  padding: 20px;
}

.logout-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.1rem;
  cursor: pointer;
  padding: 0;
}
.logout-btn:hover {
  text-decoration: underline;
}
</style>
