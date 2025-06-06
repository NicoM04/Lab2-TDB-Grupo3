<template>
  <v-container class="mt-6">
    <h1 class="text-h5 font-weight-bold mb-4">Mapa de Sectores</h1>
    <div id="map" style="height: 500px; border-radius: 8px;"></div>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import L from 'leaflet';
import sectorService from '@/services/sector.service.js';

const sectores = ref([]);

const cargarSectores = async () => {
  const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
  try {
    const res = await sectorService.obtenerTodos(token);
    sectores.value = res.data;
  } catch (err) {
    console.error('Error al cargar sectores:', err);
  }
};

onMounted(async () => {
  await cargarSectores();

  const map = L.map('map').setView([-33.45, -70.66], 13); // Santiago como centro inicial

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);

  sectores.value.forEach(sector => {
    if (sector.coordenadaY && sector.coordenadaX) {
      const marker = L.marker([sector.coordenadaY, sector.coordenadaX]).addTo(map);
      marker.bindPopup(`<strong>${sector.nombre}</strong><br>${sector.descripcion || ''}`);
    }
  });
});
</script>

<style>
#map {
  width: 100%;
}
</style>
