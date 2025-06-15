<template>
  <div class="consultas-container">
    <h2>Consultas</h2>
    
    <!-- CLIENTES -->
    <!-- Clientes lejanos -->
    <section class="consulta-section">
      <h3>Clientes lejanos de todas las empresas</h3>
      <button @click="fetchClientesLejanos" class="btn-consultar">Consultar</button>
      <table v-if="clientesLejanos.length">
        <thead>
          <tr>
            <th>ID Cliente</th>
            <th>Nombre</th>
            <th>Ubicación</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(c, index) in clientesLejanos" :key="index">
            <td>{{ c.idCliente }}</td>
            <td>{{ c.nombreCliente }}</td>
            <td>{{ c.ubicacion }}</td>
          </tr>
        </tbody>
      </table>
      <l-map
        style="height: 400px; width: 100%; max-width: 800px; margin: 20px auto; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);" 
        :zoom="12" 
        :center="[-33.45, -70.65]"
        v-if="clientesLejanos.length"
      >
        <l-tile-layer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        ></l-tile-layer>
        <l-marker
          v-for="(c, index) in clientesLejanos"
          :key="index"
          :lat-lng="parseUbicacion(c.ubicacion)"
        >
          <l-popup>{{ c.nombreCliente }}</l-popup>
        </l-marker>
      </l-map>
    </section>

    <!-- Verificar zona de cobertura -->
    <section class="consulta-section">
      <h3>Verificar zona de cobertura por ID</h3>
      <div class="input-group">
        <input v-model="clienteId" placeholder="ID Cliente" class="input-consulta" />
        <button @click="verificarZona" class="btn-consultar">Verificar</button>
      </div>
      <div v-if="zonaCobertura" class="resultado">{{ zonaCobertura }}</div>
    </section>

    <!-- PEDIDOS -->
    <!-- Pedidos más cercanos a una empresa -->
    <section class="consulta-section">
      <h3>Pedidos más cercanos a una empresa</h3>
      <div class="input-group">
        <input v-model="empresaIdCercanos" placeholder="ID Empresa" class="input-consulta" />
        <button @click="consultarPedidosCercanos" class="btn-consultar">Consultar</button>
      </div>
      <table v-if="pedidosCercanos.length">
        <thead>
          <tr>
            <th>ID Pedido</th>
            <th>Distancia</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(p, i) in pedidosCercanos" :key="i">
            <td>{{ p.idPedido }}</td>
            <td>{{ p.distanciaMetros !== undefined && p.distanciaMetros !== null ? p.distanciaMetros.toFixed(2) + ' m' : 'N/A' }}</td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- Pedidos más lejanos desde cada empresa -->
    <section class="consulta-section">
      <h3>Pedidos más lejanos por empresa</h3>
      <button @click="consultarPedidosLejanos" class="btn-consultar">Consultar</button>
      <table v-if="pedidosLejanos.length">
        <thead>
          <tr>
            <th>Empresa</th>
            <th>ID Pedido</th>
            <th>Distancia</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(p, i) in pedidosLejanos" :key="i">
            <td>{{ p.nombreEmpresa }} (ID: {{ p.idEmpresa }})</td>
            <td>{{ p.idPedido }}</td>
            <td>{{ p.distanciaMetros.toFixed(2) }} m</td>
          </tr>
        </tbody>
      </table>
      <l-map
        style="height: 400px; width: 100%; max-width: 800px; margin: 20px auto; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);" 
        :zoom="10" 
        :center="[-33.45, -70.65]"
        v-if="pedidosLejanos.length"
      >
        <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        <l-marker
          v-for="(p, index) in pedidosLejanos"
          :key="'empresa-' + index"
          :lat-lng="parseUbicacion(p.ubicacionEmpresa)"
          :icon="iconEmpresa"
        >
          <l-popup>
            <strong>{{ p.nombreEmpresa }}</strong><br />
            Empresa ID: {{ p.idEmpresa }}
          </l-popup>
        </l-marker>
        <l-marker
          v-for="(p, index) in pedidosLejanos"
          :key="'pedido-' + index"
          :lat-lng="parseUbicacion(p.puntoEntrega)"
          :icon="iconPedido"
        >
          <l-popup>
            <strong>Pedido ID:</strong> {{ p.idPedido }}<br />
            Distancia: {{ p.distanciaMetros.toFixed(2) }} m
          </l-popup>
        </l-marker>
      </l-map>
    </section>

    <!-- Pedidos que cruzan más de 2 zonas -->
    <section class="consulta-section">
      <h3>Pedidos que cruzan más de 2 zonas de reparto</h3>
      <button @click="consultarPedidosCruzanZonas" class="btn-consultar">Consultar</button>
      <table v-if="pedidosCruzanZonas.length">
        <thead>
          <tr>
            <th>ID Pedido</th>
            <th>Cliente</th>
            <th>Empresa</th>
            <th>Distancia</th>
            <th>Zonas cruzadas</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(p, i) in pedidosCruzanZonas" :key="i">
            <td>{{ p.idPedido }}</td>
            <td>ID: {{ p.idCliente }}</td>
            <td>{{ p.nombreEmpresa }} (ID: {{ p.idEmpresa }})</td>
            <td>{{ p.distanciaMetros.toFixed(2) }} m</td>
            <td>{{ p.cantidadZonas }}</td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- REPARTIDORES -->
    <section class="consulta-section">
  <h3>Distancia total recorrida por un repartidor</h3>
  <div class="input-group">
    <input v-model="idRepartidorConsulta" placeholder="ID Repartidor" class="input-consulta" />
    <input v-model.number="ultimosMesesConsulta" type="number" min="1" class="input-consulta" placeholder="Meses" />
    <button @click="consultarDistanciaTotal" class="btn-consultar">Consultar</button>
  </div>
  <div v-if="distanciaTotal !== null" class="resultado" style="color: #000000;">
    Distancia total recorrida: {{ distanciaTotal.toFixed(2) }} km
  </div>
</section>
  </div>
</template>

<script>
import ClienteService from "@/services/Cliente.service";
import PedidoService from "@/services/Pedido.service";
import RepartidorService from "@/services/Repartidor.service";

import { LMap, LTileLayer, LMarker, LPopup } from "@vue-leaflet/vue-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: new URL("leaflet/dist/images/marker-icon-2x.png", import.meta.url).href,
  iconUrl: new URL("leaflet/dist/images/marker-icon.png", import.meta.url).href,
  shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).href,
});

export default {
  name: "ConsultasCliente",
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LPopup,
  },
  data() {
    const iconEmpresa = L.icon({
      iconUrl: new URL("leaflet/dist/images/marker-icon.png", import.meta.url).href,
      shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).href,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    const iconPedido = L.icon({
      iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png",
      shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).href,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    return {
      clienteId: "",
      zonaCobertura: null,
      clientesLejanos: [],
      empresaIdCercanos: "",
      pedidosCercanos: [],
      pedidosLejanos: [],
      pedidosCruzanZonas: [],
      iconEmpresa,
      iconPedido,
      idRepartidorConsulta: "",
      ultimosMesesConsulta: "",
      distanciaTotal: null,
    };
  },
  methods: {
    async fetchClientesLejanos() {
      const res = await ClienteService.getClientesLejanos();
      this.clientesLejanos = res.data;
    },
    async verificarZona() {
      if (!this.clienteId) return;
      const res = await ClienteService.verificarZonaCobertura(this.clienteId);
      this.zonaCobertura = res.data;
    },
    async consultarPedidosCercanos() {
      if (!this.empresaIdCercanos) return;
      const res = await PedidoService.getPedidosMasCercanosEmpresa(this.empresaIdCercanos);
      this.pedidosCercanos = res.data;
    },
    async consultarPedidosLejanos() {
      const res = await PedidoService.getPedidosMasLejanosPorEmpresa();
      this.pedidosLejanos = res.data;
    },
    async consultarPedidosCruzanZonas() {
      const res = await PedidoService.getPedidosConMasDeDosZonas();
      this.pedidosCruzanZonas = res.data;
    },
    async consultarDistanciaTotal() {
      if (!this.idRepartidorConsulta) return;
      const res = await RepartidorService.getDistanciaTotalRecorrida(
        this.idRepartidorConsulta,
        this.ultimosMesesConsulta
      );
      this.distanciaTotal = res.data;
    },
    parseUbicacion(ubicacion) {
      const match = ubicacion.match(/POINT\((-?\d+\.\d+) (-?\d+\.\d+)\)/);
      if (match) {
        const [, lon, lat] = match;
        return [parseFloat(lat), parseFloat(lon)];
      }
      return [0, 0];
    },
  },
};
</script>

<style scoped>
.consultas-container {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

h2 {
  margin-bottom: 30px;
  font-size: 1.8rem;
  text-align: center;
  color: #000000;
}

.consulta-section {
  margin-bottom: 40px;
  padding: 20px;
  background-color: #fdfdfd;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

h3 {
  margin-bottom: 15px;
  font-size: 1.4rem;
  color: #000000;
  border-left: 4px solid #2835a7;
  padding-left: 10px;
}

.input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.input-consulta {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex-grow: 1;
  max-width: 300px;
}

.btn-consultar {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-consultar:hover {
  background-color: #218838;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

thead {
  background-color: #28a745;
  color: #fff;
}

th{
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eaeaea;
} 
td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eaeaea;
  color: #000000;
}

tbody tr:hover {
  background-color: #f9f9f9;
  color: #000000;
}

.resultado {
  margin-top: 15px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #17a2b8;
  color: #000000;
}
</style>