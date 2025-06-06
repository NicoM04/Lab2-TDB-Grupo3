<template> 
  <div class="empresas-fallidas-container">
    <!-- Empresas con mayor volumen de pedidos entregados -->
    <section class="mayor-volumen">
      <h3>Empresas con Mayor Volumen de Pedidos Entregados</h3>
      <table>
        <thead>
          <tr>
            <th>Empresa</th>
            <th>Pedidos Entregados</th>
            <th>Volumen Total (Monto)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="empresa in empresasMayorVolumen" :key="empresa.id_empresa">
            <td class="td">{{ empresa.nombre_empresa }}</td>
            <td class="td">{{ empresa.pedidosEntregados }}</td>
            <td class="td">${{ empresa.volumenTotal.toFixed(2) }}</td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script>
import EmpresaService from "@/services/EmpresasAsociadas.service"; // Importamos el servicio de Empresas
import PedidoService from "@/services/Pedido.service"; // Importamos el servicio de Pedidos
import MediosPagoService from "@/services/MediosPago.service"; // Importamos el servicio de Medios de Pago

export default {
  data() {
    return {
      empresasMayorVolumen: [], // Empresas con mayor volumen de pedidos entregados
    };
  },
  created() {
    this.obtenerEmpresasMayorVolumen();
  },
  methods: {

    // Obtener empresas con mayor volumen de pedidos entregados
    async obtenerEmpresasMayorVolumen() {
      const token = localStorage.getItem("jwt"); // Obtener el token de localStorage
      try {
        // Primero obtenemos todas las empresas
        const empresasResponse = await EmpresaService.getEmpresas(token);

        // Luego obtenemos los pedidos asociados a cada empresa
        let empresasMayorVolumen = await Promise.all(empresasResponse.data.map(async (empresa) => {
          // Obtener los pedidos de la empresa usando el id_empresa
          const pedidosResponse = await PedidoService.getAllPedidos(token);
          const pedidos = pedidosResponse.data.filter(pedido => pedido.id_empresa === empresa.id_empresa); // Filtramos los pedidos por empresa

          // Contamos los pedidos entregados
          let pedidosEntregados = pedidos.filter(pedido => pedido.estado.toLowerCase() === 'entregado').length;

          // Sumamos el monto total de los pedidos de la empresa
          let volumenTotal = 0;
          for (let pedido of pedidos) {
            const medioDePago = await MediosPagoService.getMedioDePagoById(pedido.id_pago, token); // Obtenemos el medio de pago de cada pedido
            volumenTotal += medioDePago.data.monto_total || 0; // Sumar el monto total del medio de pago
          }

          return {
            ...empresa,
            pedidosEntregados,
            volumenTotal
          };
        }));

        // Ordenamos las empresas de mayor a menor según la cantidad de pedidos entregados
        this.empresasMayorVolumen = empresasMayorVolumen.sort((a, b) => b.pedidosEntregados - a.pedidosEntregados);
      } catch (error) {
        console.error("Error al obtener empresas con mayor volumen de pedidos", error);
      }
    },
  },
};
</script>

<style scoped>
.empresas-fallidas-container {
  padding: 40px 20px;
  max-width: 1000px;
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
  color: #2c3e50;
}

td {
  color: #000000;
}

section {
  margin-bottom: 40px;
}

h3 {
  margin-bottom: 15px;
  font-size: 1.4rem;
  color: #343a40;
  border-left: 4px solid #2835a7;
  padding-left: 10px;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fdfdfd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

thead {
  background-color: #28a745;
  color: #fff;
}

th,
td {
  padding: 14px;
  text-align: left;
  border-bottom: 1px solid #eaeaea;
}

tbody tr:hover {
  background-color: #f9f9f9;
}
</style>

