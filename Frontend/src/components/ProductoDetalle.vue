<template>
  <div class="product-detail-container">
    <h2>Detalle del Producto</h2>

    <div v-if="producto" class="product-detail-card">
      <img src="@/components/images/producto.png" alt="Imagen del producto" class="product-image" />
      <div class="product-info">
        <h3 class="product-title">{{ producto.nombre_producto }}</h3>
        <p class="product-description">{{ producto.descripcion }}</p>
        <p class="product-price"><strong>Precio:</strong> ${{ producto.precio_unitario }}</p>
        <button @click="agregarAlCarrito(producto)">Agregar al Carrito</button>
      </div>
    </div>
    <div v-else>
      <p>Cargando información del producto...</p>
    </div>
  </div>
</template>

<script>
import ProductoServicioService from '@/services/ProductoServicio.service';

export default {
  data() {
    return {
      producto: null, // Objeto que contendrá los detalles del producto
    };
  },
  created() {
    this.obtenerProductoDetalle();
  },
  methods: {
    // Obtener el detalle del producto por ID
    async obtenerProductoDetalle() {
      const token = localStorage.getItem("jwt");
      const id = this.$route.params.id; // Obtener el ID del producto desde la ruta
      try {
        const response = await ProductoServicioService.getProductoById(id, token);
        this.producto = response.data;
      } catch (error) {
        console.error("Error al obtener el detalle del producto", error);
      }
    },
    // Método para agregar el producto al carrito
    agregarAlCarrito(producto) {
      let carrito = JSON.parse(localStorage.getItem("carrito")) || []; // Obtener carrito del localStorage, si no existe, usar uno vacío

      // Verificar si el producto ya está en el carrito
      const productoExistente = carrito.find(
        (item) => item.id_producto === producto.id_producto
      );

      if (productoExistente) {
        productoExistente.cantidad += 1; // Si el producto ya está en el carrito, aumentamos la cantidad
      } else {
        producto.cantidad = 1; // Si el producto no está en el carrito, lo agregamos con cantidad 1
        carrito.push(producto); // Agregar producto al carrito
      }

      // Guardar el carrito actualizado en el localStorage
      localStorage.setItem("carrito", JSON.stringify(carrito));

      // Mostrar un alert para notificar que el producto ha sido agregado al carrito
      alert(`${producto.nombre_producto} ha sido agregado al carrito!`);

      console.log("Producto agregado al carrito:", producto); // Solo para verificar
    },
  },
};
</script>

<style scoped>
.product-detail-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.product-detail-card {
  display: flex;
  background-color: #f7f7f7;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 900px;
  width: 100%;
  gap: 20px;
  align-items: center;
}

.product-image {
  width: 300px;
  height: auto;
  border-radius: 8px;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex-grow: 1;
}

.product-title {
  font-size: 1.8em;
  font-weight: bold;
  color: #007bff;
}

.product-description {
  font-size: 1.2em;
  color: #555;
}

.product-price {
  font-size: 1.4em;
  font-weight: bold;
  color: #333;
}

button {
  padding: 12px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1.1em;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #218838;
}
</style>
