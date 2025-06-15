<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <router-link to="/home" class="logo">
        <h2>DocumentosYa</h2>
      </router-link>
    </div>
    <div class="navbar-links">
      <router-link to="/home" class="navbar-item">Inicio</router-link>
      <router-link to="/cliente/pedidos-resumen" class="navbar-item">Resumen Pedidos</router-link>
      <router-link to="/repartidores/desempeno" class="navbar-item">Desempeño Repartidor</router-link>
      <router-link to="/empresas/mayorvolumen" class="navbar-item">Empresas Mayor Volumen</router-link>
      <router-link to="/carrito" class="navbar-item">Carrito</router-link>
      <router-link to="/consultas-cliente" class="navbar-item">Consultas</router-link>
      <button @click="logout" class="navbar-item logout-button">Cerrar sesión</button>
    </div>
  </nav>
</template>

<script>
export default {
  methods: {
    logout() {
      // Eliminar token de localStorage y redirigir a la página de login
      localStorage.removeItem("jwt");
      this.$router.push({ name: "login" });
    },
  },
};
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000; /* para que quede encima de todo */
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1px 20px;
  background-color: #007bff;
  color: white;
}

.navbar .navbar-brand h2 {
  margin: 0;
  color : white;
  font-size: 30px;
  font-weight: bold;
}

.navbar-links {
  display: flex;
  gap: 15px;
}

.navbar-item {
  color: white;
  text-decoration: none;
  font-size: 20px;
}

.navbar-item:hover {
  text-decoration: underline;
}

.logout-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 15px;
  cursor: pointer;
  border-radius: 4px;
}

.logout-button:hover {
  background-color: #c82333;
}
</style>
