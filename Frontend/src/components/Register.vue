<template>
  <div class="register-container">
    <h2>Crear cuenta</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">Nombre</label>
        <input
          type="text"
          id="name"
          v-model="name"
          required
          placeholder="Ingresa tu nombre"
        />
      </div>

      <div class="form-group">
        <label for="email">Correo electrónico</label>
        <input
          type="email"
          id="email"
          v-model="email"
          required
          placeholder="Ingresa tu correo"
        />
      </div>

      <div class="form-group">
        <label for="password">Contraseña</label>
        <input
          type="password"
          id="password"
          v-model="password"
          required
          placeholder="Ingresa tu contraseña"
        />
      </div>

      <div class="form-group">
        <label for="telefono">Teléfono</label>
        <input
          type="text"
          id="telefono"
          v-model="telefono"
          required
          placeholder="Ingresa tu teléfono"
        />
      </div>

      <div class="form-group">
        <label><strong>Selecciona tu ubicación:</strong></label>
        <div id="map" style="height: 250px; border-radius: 8px; margin-bottom: 10px;"></div>
        <div v-if="lat && lon" style="font-size: 0.9em;">
          Latitud: {{ lat }}<br>
          Longitud: {{ lon }}
        </div>
        <div v-else style="font-size: 0.9em; color: #ffd;">
          Haz clic en el mapa para seleccionar tu ubicación.
        </div>
      </div>

      <button type="submit">Registrarse</button>
    </form>
    <p>
      ¿Ya tienes cuenta? <router-link to="/login">Inicia sesión aquí</router-link>
    </p>
    <div v-if="registroExitoso" class="success-message">
      ¡Registro exitoso! Ahora puedes iniciar sesión.
    </div>
  </div>
</template>

<script>
import ClienteService from '@/services/Cliente.service';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

export default {
  data() {
    return {
      name: "",
      email: "",
      password: "",
      direccion: "a",
      telefono: "",
      fecha_registro: new Date().toISOString().slice(0, 10),
      lat: null,
      lon: null,
      map: null,
      marker: null,
      registroExitoso: false, // Nuevo estado
    };
  },
  mounted() {
    this.map = L.map('map', {
      zoomControl: true,
      attributionControl: false,
      scrollWheelZoom: true,
      doubleClickZoom: true,
      dragging: true,
    }).setView([-33.45, -70.66], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
      maxZoom: 19,
    }).addTo(this.map);

    // Animación y custom icon
    const customIcon = L.icon({
      iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
      iconSize: [38, 38],
      iconAnchor: [19, 38],
      popupAnchor: [0, -38],
    });

    this.map.on('click', (e) => {
      this.lat = e.latlng.lat.toFixed(6);
      this.lon = e.latlng.lng.toFixed(6);

      if (this.marker) {
        this.marker.setLatLng(e.latlng);
      } else {
        this.marker = L.marker(e.latlng, { icon: customIcon, bounceOnAdd: true }).addTo(this.map);
      }
      this.marker.setIcon(customIcon);
    });
  },
  methods: {
    async handleRegister() {
      if (this.lat === null || this.lon === null) {
        alert("Debes seleccionar una ubicación en el mapa.");
        return;
      }
      try {
        await ClienteService.registerCliente({
          nombre_cliente: this.name,
          correo_cliente: this.email,
          contrasena_cliente: this.password,
          direccion: this.direccion,
          telefono: this.telefono,
          fecha_registro: this.fecha_registro,
          lat: this.lat,
          lon: this.lon,
        });
        this.registroExitoso = true; // Mostrar mensaje de éxito
        setTimeout(() => {
          this.$router.push({ name: "login" });
        }, 3000);
      } catch (error) {
        console.error("Error en el registro", error);
        alert("Hubo un error al registrar la cuenta.");
      }
    },
  },
};
</script>

<style scoped>
@import "leaflet/dist/leaflet.css";

.register-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 95%;
  max-width: 500px;
  margin: 0 auto;
  padding: 40px;
  background-color: #007bff;
  color: rgb(255, 255, 255);
  border-radius: 12px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
}

.form-group {
  margin-bottom: 1em;
  color: rgb(255, 255, 255);
}

label {
  display: block;
  margin-bottom: 0.5em;
}

input {
  width: 100%;
  padding: 12px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}

button {
  width: 100%;
  padding: 12px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #218838;
}

.success-message {
  margin-top: 16px;
  padding: 12px;
  background: #28a745;
  color: #fff;
  border-radius: 8px;
  text-align: center;
  font-weight: bold;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08);
}

#map {
  border-radius: 8px;
}
</style>