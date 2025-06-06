<template>
  <div class="productos-mas-pedidos-container">
    <h2>Productos Más Solicitados</h2>

    <!-- Tabla de productos más pedidos -->
    <section v-if="productos.length > 0" class="productos-list">
      <table>
        <thead>
          <tr>
            <th>Producto</th>
            <th>Cantidad Vendida</th>
            <th>Volumen Total</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="producto in productos" :key="producto.id">
            <td>{{ producto.nombre }}</td>
            <td>{{ producto.cantidadVendida }}</td>
            <td>${{ producto.volumenTotal }}</td>
            <td>
              <router-link :to="'/producto/' + producto.id">
                <button>Ver Detalles</button>
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- Mensaje cuando no hay productos -->
    <p v-else>No hay productos más solicitados por el momento.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      productos: [], // Lista de los productos más vendidos
    };
  },
  created() {
    this.obtenerProductosMasSolicitados();
  },
  methods: {
    // Obtener los productos más solicitados desde la API
    async obtenerProductosMasSolicitados() {
      try {
        const response = await axios.get("/api/productos/mas-pedidos");
        this.productos = response.data;
      } catch (error) {
        console.error("Error al obtener los productos más solicitados", error);
      }
    },
  },
};
</script>

<style scoped>
.productos-mas-pedidos-container {
  padding: 20px;
}

.productos-list {
  margin-bottom: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 8px;
  text-align: left;
  border: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

button {
  padding: 8px 12px;
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
