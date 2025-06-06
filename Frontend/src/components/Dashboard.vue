<template>
  <div class="dashboard-container">
    <h2>Bienvenido al Dashboard</h2>

    <!-- Productos/Servicios -->
    <section class="products">
      <h3>Productos y Servicios</h3>
      <div class="product-list">
        <div
          class="product-item"
          v-for="producto in productos"
          :key="producto.id_producto"
        >
          <img src="@/components/images/producto.png" alt="Logo" />
          <h4 class="product-name">{{ producto.nombre_producto }}</h4>
          <p class="product-description">{{ producto.descripcion }}</p>
          <p class="product-description"><strong>Precio:</strong> ${{ producto.precio_unitario }}</p>
          <button @click="verDetalles(producto.id_producto)">Ver más detalles</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import ProductoServicioService from '@/services/ProductoServicio.service';

export default {
  data() {
    return {
      productos: [], // Lista de productos/servicios
    };
  },
  created() {
    this.obtenerProductos();
  },
  methods: {
    // Obtener productos y servicios desde la API
    async obtenerProductos() {
      const token = localStorage.getItem("jwt"); // Obtener el token de localStorage
      try {
        const response = await ProductoServicioService.getAllProductos(token);
        // Asignar una imagen predeterminada si no existe la imagen en el producto
        this.productos = response.data.map(producto => {
          return {
            ...producto,
            imagen: producto.imagen || 'https://via.placeholder.com/200x200?text=Producto+Sin+Imagen', // Asignar imagen predeterminada
          };
        });
      } catch (error) {
        console.error("Error al obtener productos", error);
      }
    },

    // Ver más detalles de un producto
    verDetalles(id) {
      this.$router.push({ name: 'producto-detalle', params: { id: id } });
    },
  },
};
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column; 
  padding: 20px;
}

/* Título principal centrado y grande */
.dashboard-container h2 {
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
  color: #cfcfcf;
  margin-bottom: 40px;
}

section {
  margin-bottom: 30px;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.product-item {
  width: 250px; /* Cambié el ancho para que se vea más grande */
  text-align: center;
  border: 1px solid #ddd;
  padding: 15px; /* Aumenté el padding */
  border-radius: 12px; /* Redondeé más los bordes */
  transition: box-shadow 0.3s ease;
  background-color: #f7f7f7; /* Agregué un color de fondo claro */
}

.product-name {
  font-size: 1.2em;
  font-weight: bold;
  color: #007bff; /* Color destacado para el nombre */
  margin-bottom: 10px;
}

.product-description {
  font-size: 1em;
  color: #555; /* Color gris para la descripción */
  margin-bottom: 10px;
}

.product-item:hover {
  box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.2); /* Sombra más fuerte */
}


.product-item img {
  max-width: 100%;
  height: auto;
  margin-bottom: 10px;
}

button {
  margin-top: 10px;
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
