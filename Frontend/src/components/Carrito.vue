<template>
  <div class="carrito-container">
    <h2>Mi Carrito</h2>

    <div v-if="productosEnCarrito.length === 0">
      <p>Tu carrito está vacío.</p>
    </div>

    <div v-else>
      <table>
        <thead>
          <tr>
            <th>Producto</th>
            <th>Cantidad</th>
            <th>Precio</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(producto, index) in productosEnCarrito" :key="index">
            <td>{{ producto.nombre_producto }}</td>
            <td>{{ producto.cantidad }}</td>
            <td>${{ producto.precio_unitario.toFixed(2) }}</td>
            <td>
              <button @click="eliminarDelCarrito(index)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="total">
        <p>Total: ${{ calcularTotal().toFixed(2) }}</p>
        <button @click="finalizarCompra">Finalizar Compra</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      productosEnCarrito: JSON.parse(localStorage.getItem("carrito")) || [], // Cargar productos desde el localStorage si existen
    };
  },
  methods: {
    eliminarDelCarrito(index) {
      this.productosEnCarrito.splice(index, 1); // Eliminar producto del carrito
      this.actualizarCarrito();
    },
    calcularTotal() {
      return this.productosEnCarrito.reduce(
        (total, producto) => total + producto.precio_unitario * producto.cantidad,
        0
      );
    },
    actualizarCarrito() {
      localStorage.setItem("carrito", JSON.stringify(this.productosEnCarrito)); // Guardar el carrito actualizado en localStorage
    },
    finalizarCompra() {
      alert("¡Gracias por tu compra!"); // Aquí puedes redirigir a una página de pago
    },
  },
};
</script>

<style scoped>
.carrito-container {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
  color:#333
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 4px;
}

button:hover {
  background-color: #0056b3;
}

.total {
  font-size: 1.2em;
  font-weight: bold;
  color: #ffffff;
  margin-top: 20px;
}
</style>
