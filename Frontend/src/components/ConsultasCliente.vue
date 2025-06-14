<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Consultas de Clientes y Pedidos</h1>
    <div style="height: 400px;">
</div>


    <!-- CLIENTES -->

    <!-- Clientes lejanos -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Clientes lejanos de todas las empresas</h2>
      <ul>
        <li v-for="(c, index) in clientesLejanos" :key="index">
          ID: {{ c.idCliente }} - {{ c.nombreCliente }}<br />
          Ubicación: {{ c.ubicacion }}
        </li>
      </ul>
      <button @click="fetchClientesLejanos" class="btn">Consultar</button>
    </section>
      <l-map
    style="height: 400px; width: 100%;"
    :zoom="12"
    :center="[ -33.45, -70.65 ]"
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



    <!-- Verificar zona de cobertura -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Verificar zona de cobertura por ID</h2>
      <input v-model="clienteId" placeholder="ID Cliente" class="input" />
      <button @click="verificarZona" class="btn">Verificar</button>
      <div v-if="zonaCobertura">{{ zonaCobertura }}</div>
    </section>

    <!-- PEDIDOS -->

    <!-- Pedidos más cercanos a una empresa -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Pedidos más cercanos a una empresa</h2>
      <input v-model="empresaIdCercanos" placeholder="ID Empresa" class="input" />
      <button @click="consultarPedidosCercanos" class="btn">Consultar</button>
      <ul>
        <li v-for="(p, i) in pedidosCercanos" :key="i">
  Pedido ID: {{ p.idPedido }},
  Distancia: {{ p.distanciaMetros !== undefined && p.distanciaMetros !== null ? p.distanciaMetros.toFixed(2) + ' m' : 'N/A' }}
</li>

      </ul>
    </section>

    <!-- Pedidos más lejanos desde cada empresa -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Pedidos más lejanos por empresa</h2>
      <button @click="consultarPedidosLejanos" class="btn">Consultar</button>
      <ul>
        <li v-for="(p, i) in pedidosLejanos" :key="i">
          Empresa ID: {{ p.id_empresa }}, Pedido ID: {{ p.id_pedido }}, Distancia: {{ p.distanciaMetros.toFixed(2) }}m
        </li>
      </ul>
    </section>

    <!-- Pedidos que cruzan más de 2 zonas -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Pedidos que cruzan más de 2 zonas de reparto</h2>
      <button @click="consultarPedidosCruzanZonas" class="btn">Consultar</button>
      <ul>
        <li v-for="(p, i) in pedidosCruzanZonas" :key="i">
          Pedido ID: {{ p.id }}, Zonas cruzadas: {{ p.zonas_cruzadas }}
        </li>
      </ul>
    </section>

  </div>
</template>

<script>
import ClienteService from "@/services/cliente.service";
import PedidoService from "@/services/pedido.service";

// Leaflet para Vue 3
import { LMap, LTileLayer, LMarker, LPopup } from "@vue-leaflet/vue-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

// Corrige íconos rotos
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
    return {
      clienteId: "",
      zonaCobertura: null,
      clientesLejanos: [],
      empresaIdCercanos: "",
      pedidosCercanos: [],
      pedidosLejanos: [],
      pedidosCruzanZonas: [],
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
    parseUbicacion(ubicacion) {
      const match = ubicacion.match(/POINT\((-?\d+\.\d+) (-?\d+\.\d+)\)/);
      if (match) {
        const [, lon, lat] = match;
        return [parseFloat(lat), parseFloat(lon)];
      }
      return [0, 0];
    }
  }
};
</script>



<style scoped>
.input {
  border: 1px solid #d1d5db;
  padding: 0.25rem 0.5rem;
  margin-right: 0.5rem;
  border-radius: 0.25rem;
}
.btn {
  background-color: #2563eb;
  color: #fff;
  padding: 0.25rem 1rem;
  border-radius: 0.25rem;
  transition: background-color 0.2s;
  cursor: pointer;
}
.btn:hover {
  background-color: #1d4ed8;
}
</style>
