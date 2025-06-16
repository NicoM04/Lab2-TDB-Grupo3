<template>
  <div class="carrito">
    <h1>Carrito de Compras</h1>

    <div v-if="carrito.length === 0">No hay productos seleccionados.</div>

    <div v-for="producto in carrito" :key="producto.id_producto" class="producto">
      <h3>{{ producto.nombre_producto }}</h3>
      <p>Cantidad: {{ producto.cantidadSeleccionada }}</p>
      <p>Precio unitario: ${{ producto.precio_unitario }}</p>
    </div>

    <h2>Seleccionar repartidor</h2>
    <select v-model="idRepartidorSeleccionado">
      <option disabled value="">-- Selecciona un repartidor --</option>
      <option v-for="r in repartidores" :key="r.id_repartidor" :value="r.id_repartidor">
        {{ r.nombre_repartidor }}
      </option>
    </select>

    <h2>Opciones adicionales</h2>

    <label>
      Método de pago:
      <select v-model="metodoPago">
        <option value="efectivo">Efectivo</option>
        <option value="tarjeta de crédito">Tarjeta de crédito</option>
        <option value="transferencia bancaria">Transferencia bancaria</option>
      </select>
    </label>

    <button @click="confirmarPedido" class="confirmar-btn">
      Confirmar pedido
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import PedidoService from '@/services/Pedido.service';
import RepartidorService from '@/services/Repartidor.service';
import ProductoServicio from '@/services/ProductoServicio.service';

const router = useRouter();
const carrito = ref([]);
const repartidores = ref([]);
const idRepartidorSeleccionado = ref(null);
const urgente = ref(false);
const metodoPago = ref('efectivo');

const idCliente = parseInt(localStorage.getItem('id_cliente'));
const idEmpresa = parseInt(localStorage.getItem('id_empresa'));

const cargarCarrito = async () => {
  const data = JSON.parse(localStorage.getItem('carrito'));
  if (!data || !data.productos || !data.cantidades) return;

  const token = localStorage.getItem("jwt");
  try {
    const response = await ProductoServicio.getAllProductos(token);
    const productosAPI = response.data;

    // Combinar info de carrito con detalles de productos
    carrito.value = data.productos.map((id, index) => {
      const producto = productosAPI.find(p => p.id_producto === id);
      return {
        id_producto: producto.id_producto,
        nombre_producto: producto.nombre_producto,
        precio_unitario: producto.precio_unitario,
        cantidadSeleccionada: data.cantidades[index]
      };
    });
  } catch (error) {
    console.error('Error al reconstruir carrito:', error);
  }
};

const cargarRepartidores = async () => {
  const token = localStorage.getItem("jwt");
  try {
    const response = await RepartidorService.getAllRepartidores(token);
    repartidores.value = response.data;
  } catch (error) {
    console.error('Error al cargar repartidores:', error);
  }
};

const toLocalISOString = (date) => {
  const tzOffset = date.getTimezoneOffset() * 60000;
  return new Date(date - tzOffset).toISOString().slice(0, 19);
};

const confirmarPedido = async () => {
  if (!idRepartidorSeleccionado.value) {
    alert('Selecciona un repartidor.');
    return;
  }

  const ahora = new Date();
  const entrega = new Date();
  entrega.setDate(ahora.getDate() + 3);

  const pedidoDTO = {
    idCliente,
    idEmpresa,
    idRepartidor: idRepartidorSeleccionado.value,
    fechaPedido: toLocalISOString(ahora),
    fechaEntrega: toLocalISOString(entrega),
    estado: 'Pendiente',
    urgente: false,
    metodoPago: metodoPago.value,
    productos: carrito.value.map(p => p.id_producto),
    cantidades: carrito.value.map(p => p.cantidadSeleccionada)
  };

  try {
    const token = localStorage.getItem("jwt");

    // 1. Registrar el pedido
    await PedidoService.registrarPedido(pedidoDTO, token);

    // 2. Obtener el último pedido de todos (mayor id_pedido)
    const todosLosPedidos = await PedidoService.getAllPedidos(token);
    const ultimoPedido = todosLosPedidos.data.reduce(
      (max, p) => (p.id_pedido > max.id_pedido ? p : max),
      todosLosPedidos.data[0]
    );
    const idPedido = ultimoPedido.id_pedido;

    // 3. Confirmar y descontar stock
    await PedidoService.confirmarPedidoYDescontarStock(idPedido, token);

    alert('Pedido confirmado con éxito.');
    localStorage.removeItem('carrito');
    router.push({ name: 'home' });
  } catch (error) {
    console.error('Error al confirmar pedido:', error);
    alert('Hubo un error al procesar el pedido.');
  }
};

onMounted(() => {
  cargarCarrito();
  cargarRepartidores();
});

</script>

<style scoped>
.carrito {
  display: flex;
  flex-direction: column;

  padding: 20px;
  max-width: 600px;
  margin: auto;
  margin-top: 90px; /* Ajusta este valor según la altura real de tu navbar */
}
.producto {
  border: 1px solid #ccc;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 6px;
}
.confirmar-btn {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #0077cc;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
</style>
