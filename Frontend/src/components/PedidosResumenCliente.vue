<template>
  <div class="pedidos-resumen-container">
    <h2>Resumen de Pedidos por Cliente</h2>

    <!-- Filtros -->
    <div class="filters">
      <label for="estado">Estado del Pedido:</label>
      <select v-model="estadoFiltro" @change="filtrarPedidos">
        <option value="">Todos</option>
        <option value="pendiente">Pendiente</option>
        <option value="en reparto">En Reparto</option>
        <option value="finalizado">Finalizado</option>
        <option value="cancelado">Cancelado</option>
      </select>

      <label for="fechaInicio">Fecha de Inicio:</label>
      <input type="date" v-model="fechaInicioFiltro" @change="filtrarPedidos" />

      <label for="fechaFin">Fecha de Fin:</label>
      <input type="date" v-model="fechaFinFiltro" @change="filtrarPedidos" />
    </div>

    <!-- Resumen de pedidos -->
    <section class="summary">
      <h3>Pedidos Realizados</h3>
      <table>
        <thead>
          <tr>
            <th>ID del Pedido</th>
            <th>Fecha</th>
            <th>Estado</th>
            <th>Monto Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pedido in pedidosFiltrados" :key="pedido.id_pedido">
            <td class="td">{{ pedido.id_pedido }}</td>
            <td class="td">{{ new Date(pedido.fecha_pedido).toLocaleDateString() }}</td>
            <td class="td">{{ pedido.estado }}</td>
            <td class="td">${{ pedido.montoTotal ? pedido.montoTotal.toFixed(2) : '0.00' }}</td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- Resumen de total -->
    <section class="total">
      <h3>Total de Pedidos</h3>
      <p>Número de pedidos: {{ numeroPedidos }}</p>
      <p>Monto Total: ${{ montoTotal ? montoTotal.toFixed(2) : '0.00' }}</p>
    </section>
  </div>
</template>

<script>
import PedidoServicio from "@/services/Pedido.service"; // Importando PedidoService
import MediosPagoServicio from "@/services/MediosPago.service"; // Importando MediosPagoService

export default {
  data() {
    return {
      pedidos: [], // Lista de todos los pedidos del cliente
      pedidosFiltrados: [], // Lista de pedidos filtrados
      estadoFiltro: "", // Filtro por estado
      fechaInicioFiltro: "", // Filtro por fecha de inicio
      fechaFinFiltro: "", // Filtro por fecha de fin
      numeroPedidos: 0, // Número total de pedidos
      montoTotal: 0, // Monto total de los pedidos
    };
  },
  created() {
    const idCliente = localStorage.getItem("id_cliente"); // Obtener el id_cliente de localStorage
    this.obtenerPedidos(idCliente); // Pasar el id_cliente al método obtenerPedidos
  },
  methods: {
    // Obtener todos los pedidos del cliente
    async obtenerPedidos(idCliente) {
      const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
      try {
        const response = await PedidoServicio.obtenerPedidosCliente(idCliente, token); // Llamar al servicio con el id_cliente y token
        this.pedidos = response.data; // Asignar los datos obtenidos
        this.pedidosFiltrados = [...this.pedidos]; // Hacemos una copia para filtrarlos
        await this.obtenerMontoTotal(); // Llamamos a la función para obtener los montos de los pedidos
        this.calcularResumen(); // Llamamos la función para calcular el resumen de los pedidos
      } catch (error) {
        console.error("Error al obtener los pedidos", error); // En caso de error, lo imprimimos
      }
    },

    // Obtener el monto total de cada pedido desde su id_pago relacionado
    async obtenerMontoTotal() {
    for (let pedido of this.pedidosFiltrados) {
        try {
        const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
        const response = await MediosPagoServicio.getMedioDePagoById(pedido.id_pago, token); // Llamar al servicio con el token y el id_pago
        console.log(response);
        pedido.montoTotal = response.data.monto_total; // Asignamos el monto total del pago al pedido
        } catch (error) {
        console.error(`Error al obtener el monto total para el pedido ${pedido.id_pedido}`, error);
        }
    }
    this.calcularResumen(); // Después de obtener los montos, recalculamos el resumen
    },


    // Filtrar pedidos por estado y fecha
    filtrarPedidos() {
      this.pedidosFiltrados = this.pedidos.filter((pedido) => {
        const fechaValida =
          (!this.fechaInicioFiltro ||
            new Date(pedido.fecha_pedido) >= new Date(this.fechaInicioFiltro)) &&
          (!this.fechaFinFiltro || new Date(pedido.fecha_pedido) <= new Date(this.fechaFinFiltro));

        const estadoValido =
          !this.estadoFiltro || pedido.estado.toLowerCase() === this.estadoFiltro.toLowerCase();

        return fechaValida && estadoValido;
      });
      this.calcularResumen(); // Volvemos a calcular el resumen después de filtrar
    },

    // Calcular resumen de pedidos (número total y monto total)
    calcularResumen() {
      this.numeroPedidos = this.pedidosFiltrados.length; // Contamos los pedidos filtrados

      // Asegurarse de que el montoTotal no sea undefined o null
      this.montoTotal = this.pedidosFiltrados.reduce((total, pedido) => {
        return total + (pedido.montoTotal || 0); // Asegurarse de que montoTotal sea un número
      }, 0); // Si es undefined o null, se asigna 0 por defecto
    },

    // Ver detalles del pedido
    verDetalles(id) {
      this.$router.push({ name: "pedido-detalle", params: { id } }); // Redirigimos a la vista de detalles del pedido
    },
  },
};
</script>

<style scoped>
.pedidos-resumen-container {
  padding: 40px 20px;
  max-width: 1000px;
  margin: 0 auto;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

td {
  color: #000000;
}


.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  margin-bottom: 30px;
}

.filters label {
  font-weight: 600;
  margin-right: 5px;
  color: #444;
}

.filters select,
.filters input {
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
}

section {
  margin-bottom: 40px;
}

.summary h3,
.total h3 {
  font-size: 1.3rem;
  color: #2c3e50;
  margin-bottom: 15px;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fafafa;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

thead {
  background-color: #007bff;
  color: white;
}

th,
td {
  padding: 14px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

tbody tr:hover {
  background-color: #f1f1f1;
}

button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #218838;
}

.total p {
  font-size: 1.1rem;
  color: #555;
  margin: 6px 0;
  text-align: center;
}

</style>
