<script setup>
import Navbar from './components/Navbar.vue';
</script>

<template>
  <div id="app">
    <!-- Mostrar el navbar solo si no estás en las rutas de login o register -->
    <Navbar v-if="$route.name !== 'login' && $route.name !== 'register'" />
    <div class="wrapper"></div>
    <router-view />
  </div>
</template>

<script>
import Navbar from "./components/Navbar.vue";

export default {
  components: {
    Navbar,
  },
};
</script>

<style scoped>
/* Estilos para pantallas grandes */
@media (min-width: 1024px) {
  .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
    width: 100vw;
  }
}
</style>

