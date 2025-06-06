<template>
  <v-container class="mt-8">
    <h1 class="text-h5 font-weight-bold mb-6">Estadísticas Generales</h1>

    <v-row dense>
      <!-- 1 -->
      <v-col cols="12">
        <v-card class="mb-4">
          <v-card-title>1. ¿Cuántas tareas ha hecho el usuario por sector?</v-card-title>
          <v-card-text>
            <!-- Tabla -->
            <v-simple-table>
              <thead>
                <tr>
                  <th>Sector</th>
                  <th>Tareas completadas</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="s in tareasPorSector" :key="s.sector">
                  <td>{{ s.sector }}</td>
                  <td>{{ s.cantidad }}</td>
                </tr>
              </tbody>
            </v-simple-table>

            <!-- Gráfico de barras -->
            <div style="height: 300px;" class="mt-4">
              <Bar :data="datosGrafico" />
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 2 -->
      <v-col cols="12">
        <v-card class="mb-4">
          <v-card-title>2. Tareas pendientes del usuario</v-card-title>
          <v-card-text>
            <div v-if="tareaCercana && tareaCercana.length > 0">
              <ul>
                <li v-for="t in tareaCercana" :key="t.id">
                  {{ t.nombre }} - Termina el {{ new Date(t.fechaTermino).toLocaleDateString() }}
                </li>
              </ul>
            </div>
            <div v-else>No hay tareas pendientes registradas.</div>
          </v-card-text>
        </v-card>
      </v-col>



      <!-- 3 -->
      <v-col cols="12">
        <v-card class="mb-4">
          <v-card-title>3. Sector con más tareas completadas en un radio de 2 kilómetros del Usuario</v-card-title>
          <v-card-text v-if="sectorMasEnRadio">
            El sector <strong>{{ sectorMasEnRadio.nombre }}</strong> tiene 
            <strong>{{ sectorMasEnRadio.cantidad }}</strong> tareas completadas cerca del usuario.
          </v-card-text>
          <v-card-text v-else>
            No hay sectores con tareas completadas en un radio de 2 km.
          </v-card-text>
        </v-card>
      </v-col>


      <!-- 4 -->
      <v-col cols="12" >
        <v-card class="mb-4">
          <v-card-title>4. Promedio de distancia de las tareas completadas respecto a la ubicación del Usuario</v-card-title>
          <v-card-text>
            <div class="mb-2">
              {{ promedioUsuario !== null ? promedioUsuario.toFixed(2) + ' metros' : 'No disponible' }}
            </div>

            <v-progress-linear
              :model-value="promedioUsuario"
              :color="getColor(promedioUsuario)"
              height="16"
              rounded
              background-color="grey-lighten-3"
              :max="5000"
              v-if="promedioUsuario !== null"
            >
              <template #default>
                <strong>{{ nivelDistancia(promedioUsuario) }}</strong>
              </template>
            </v-progress-linear>
          </v-card-text>
        </v-card>
      </v-col>


      <!-- 5 -->
      <v-col cols="12">
        <v-card class="mb-4">
          <v-card-title>5. Sectores con más tareas pendientes (agrupados por cercanía)</v-card-title>
          <v-card-text>
            <div v-if="sectoresMasPendientes.length > 0">
              <ul>
                <li v-for="grupo in sectoresMasPendientes" :key="grupo.grupo_id" class="mb-2">
                  <strong>Grupo {{ grupo.grupo_id }}:</strong> {{ grupo.total_tareas_pendientes }} pendientes
                  <div class="mt-1">
                    <v-chip
                      v-for="sector in grupo.sectores"
                      :key="sector"
                      class="ma-1"
                      color="indigo-lighten-3"
                      text-color="white"
                      label
                      small
                    >
                      {{ sector }}
                    </v-chip>
                  </div>
                </li>
              </ul>
            </div>
            <div v-else>No hay tareas pendientes agrupadas.</div>
          </v-card-text>
        </v-card>
      </v-col>


      <!-- 6 -->
      <v-col cols="12" >
        <v-card class="mb-4">
          <v-card-title>6. Tarea pendiente más cercana (geodistancia)</v-card-title>
          <v-card-text>
            <div v-if="tareaGeo">
              <strong>{{ tareaGeo.nombre }}</strong><br />
              Fecha de término: {{ new Date(tareaGeo.fechaTermino).toLocaleDateString() }}<br />
              Sector: {{ tareaGeo.sector?.nombre || 'No especificado' }}
            </div>
            <div v-else>
              No hay tareas pendientes cercanas encontradas.
            </div>
          </v-card-text>
        </v-card>
      </v-col>


      <!-- 7 -->
      <v-col cols="12" >
        <v-card class="mb-4">
          <v-card-title>7. Tareas completadas por usuario y sector</v-card-title>
          <v-card-text>
            <v-table density="compact">
              <thead>
                <tr>
                  <th class="text-left">Usuario</th>
                  <th class="text-left">Sector</th>
                  <th class="text-right">Tareas</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(t, index) in tareasPorUsuario" :key="index">
                  <td>{{ t.usuario }}</td>
                  <td>{{ t.sector }}</td>
                  <td class="text-right">{{ t.total_tareas }}</td>
                </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 8 -->
      <v-col cols="12">
        <v-card class="mb-4">
          <v-card-title>8. Sector con más tareas completadas dentro de un radio de 5 km</v-card-title>
          <v-card-text>
            <div v-if="sectorMasCompletadas">
              <strong>Sector:</strong> {{ sectorMasCompletadas.sector }} <br />
              <strong>Total completadas:</strong> {{ sectorMasCompletadas.total_tareas }}
            </div>
            <div v-else>
              No disponible
            </div>
          </v-card-text>
        </v-card>
      </v-col>


      <!-- 9 -->
      <v-col cols="12" >
        <v-card class="mb-4">
          <v-card-title>9. Promedio de distancia entre las tareas completadas y el punto registrado del usuario</v-card-title>
          <v-card-text>
            <div v-if="promedioGlobal !== null">
              <strong>{{ promedioGlobal.toFixed(2) }}</strong> metros
            </div>
            <div v-else>
              No disponible
            </div>
          </v-card-text>
        </v-card>
      </v-col>




    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'

import tareaService from '@/services/tarea.service.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const usuarioId = localStorage.getItem("usuarioId");

const tareasPorSector = ref([]);
const tareaCercana = ref(null);
const sectorMasEnRadio = ref(null);
const promedioUsuario = ref(null);
const sectoresMasPendientes = ref([]);
const tareaGeo = ref(null);
const tareasPorUsuario = ref([]);
const sectorMasCompletadas = ref(null);
const promedioGlobal = ref(null);
const token = localStorage.getItem("jwt"); // Obtener el token del localStorage


const datosGrafico = computed(() => ({
  labels: tareasPorSector.value.map(s => s.sector),
  datasets: [
    {
      label: 'Tareas completadas',
      data: tareasPorSector.value.map(s => s.cantidad), 
      backgroundColor: '#1976D2',
    }
  ]
}));


const formatearFecha = (fecha) => {
  if (!fecha) return 'Sin fecha';
  return new Date(fecha).toLocaleDateString('es-CL', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const getColor = (valor) => {
  if (valor < 1000) return 'green';
  if (valor < 3000) return 'orange';
  return 'red';
};

const nivelDistancia = (valor) => {
  if (valor < 1000) return 'Distancia Baja';
  if (valor < 3000) return 'Distancia Media';
  return 'Distancia Alta';
};




onMounted(async () => {
  tareasPorSector.value = await tareaService.tareasPorSector(usuarioId, token).then(res => res.data);
  tareaCercana.value = await tareaService.tareasPendientesDelUsuario(usuarioId, token).then(res => res.data);
  sectorMasEnRadio.value = await tareaService.sectorMasTareasEnRadio(usuarioId, token).then(res => res.data);
  promedioUsuario.value = await tareaService.promedioDistanciaCompletadas(usuarioId, token).then(res => res.data);
  sectoresMasPendientes.value = await tareaService.sectoresMasTareasPendientes(token).then(res => res.data);
  tareaGeo.value = await tareaService.tareaMasCercana(usuarioId, token).then(res => res.data);
  tareasPorUsuario.value = await tareaService.tareasPorUsuarioYSector(token).then(res => res.data);
  sectorMasCompletadas.value = await tareaService.sectorMasCompletadas5km(usuarioId, token).then(res => res.data);
  promedioGlobal.value = await tareaService.promedioDistanciaGlobal(usuarioId, token).then(res => res.data);
});
</script>
