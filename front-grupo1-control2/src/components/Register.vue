<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card class="pa-6" max-width="500" elevation="6">
      <v-card-title class="text-h5 text-center mb-4">Registro de Usuario</v-card-title>

      <v-form @submit.prevent="registrarUsuario">
        <v-text-field v-model="username" label="Nombre de usuario" required />
        <v-text-field v-model="correo" label="Correo electrónico" type="email" required />
        <v-text-field v-model="contrasena" label="Contraseña" type="password" required />

        <div class="my-4">
          <p class="mb-2"><strong>Selecciona tu ubicación:</strong></p>
          <div id="map" style="height: 250px;"></div>
        </div>

        <v-btn
          class="mt-4"
          color="primary"
          block
          type="submit"
          :loading="cargando"
        >
          Registrarse
        </v-btn>
      </v-form>

      <!-- Botón para volver al login -->
      <v-btn
        class="mt-2"
        variant="text"
        color="secondary"
        block
        @click="volverAlLogin"
      >
        ¿Ya tienes cuenta? Inicia sesión
      </v-btn>

      <v-alert v-if="error" type="error" class="mt-4">{{ error }}</v-alert>
      <v-alert v-if="exito" type="success" class="mt-4">¡Registro exitoso!</v-alert>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import L from 'leaflet';
import usuarioService from '@/services/usuario.service.js';

const username = ref('');
const correo = ref('');
const contrasena = ref('');
const x = ref(null); // longitud
const y = ref(null); // latitud
const error = ref(null);
const exito = ref(false);
const cargando = ref(false);
const router = useRouter();

const registrarUsuario = async () => {
  if (x.value === null || y.value === null) {
    error.value = 'Debes seleccionar una ubicación en el mapa.';
    return;
  }

  try {
    cargando.value = true;
    error.value = null;
    exito.value = false;

    const dto = {
      username: username.value,
      correo: correo.value,
      contrasena: contrasena.value,
      x: x.value,
      y: y.value
    };

    await usuarioService.register(dto);
    exito.value = true;
  } catch (err) {
    error.value = 'Error al registrar: ' + err.message;
  } finally {
    cargando.value = false;
  }
};

const volverAlLogin = () => {
  router.push('/login');
};

onMounted(() => {
  const map = L.map('map').setView([-33.45, -70.66], 13); // Santiago por defecto

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);

  let marker;

  map.on('click', function (e) {
    x.value = e.latlng.lng;
    y.value = e.latlng.lat;

    if (marker) {
      marker.setLatLng(e.latlng);
    } else {
      marker = L.marker(e.latlng).addTo(map);
    }
  });
});
</script>

<style>
#map {
  border-radius: 8px;
}
</style>