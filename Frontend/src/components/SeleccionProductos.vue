<template>
  <div class="seleccion-productos">
    <h1>Selecciona tus productos</h1>
    <div v-if="productos.length === 0">Cargando productos...</div>

    <div class="productos-grid">
      <div v-for="producto in productos" :key="producto.id_producto" class="producto-card">
        <h3>{{ producto.nombre_producto }}</h3>
        <p><strong>Categoría:</strong> {{ producto.categoria }}</p>
        <p>{{ producto.descripcion }}</p>
        <p><strong>Precio:</strong> ${{ producto.precio_unitario }}</p>
        <p><strong>Stock disponible:</strong> {{ producto.stock }}</p>

        <div class="cantidad-selector">
          <button @click="disminuirCantidad(producto.id_producto)">-</button>
          <span>{{ cantidades[producto.id_producto] }}</span>
          <button @click="aumentarCantidad(producto.id_producto, producto.stock)">+</button>
        </div>
      </div>
    </div>

    <button @click="confirmarSeleccion" class="confirmar-btn">
      Confirmar selección y continuar al carrito
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ProductoServicio from '@/services/ProductoServicio.service';

const productos = ref([]);
const cantidades = ref({});
const router = useRouter();

// Cargar todos los productos
const cargarProductos = async () => {
  const token = localStorage.getItem("jwt");
  try {
    const response = await ProductoServicio.getAllProductos(token);
    productos.value = response.data;

    // Inicializar cantidades en 0 para cada producto
    response.data.forEach(producto => {
      cantidades.value[producto.id_producto] = 0;
    });
  } catch (error) {
    console.error('Error al cargar productos:', error);
  }
};

const aumentarCantidad = (id_producto, stock) => {
  if (cantidades.value[id_producto] < stock) {
    cantidades.value[id_producto]++;
  }
};

const disminuirCantidad = (id_producto) => {
  if (cantidades.value[id_producto] > 0) {
    cantidades.value[id_producto]--;
  }
};

const confirmarSeleccion = () => {
  const productosSeleccionados = productos.value
    .filter(p => cantidades.value[p.id_producto] > 0);

  if (productosSeleccionados.length === 0) {
    alert('Debes seleccionar al menos un producto antes de continuar.');
    return;
  }

  const productosIds = productosSeleccionados.map(p => p.id_producto);
  const cantidadesSeleccionadas = productosSeleccionados.map(p => cantidades.value[p.id_producto]);

  const pedidoParcial = {
    idCliente: parseInt(localStorage.getItem('idCliente')), // asegúrate de guardar esto antes
    idEmpresa: parseInt(localStorage.getItem('idEmpresa')), // lo mismo
    productos: productosIds,
    cantidades: cantidadesSeleccionadas
    // el resto se completará en Carrito.vue
  };

  localStorage.setItem('carrito', JSON.stringify(pedidoParcial));
  router.push({ name: 'carrito' });
};

onMounted(() => {
  cargarProductos();
});
</script>

<style scoped>
.seleccion-productos {
  padding: 20px;
  margin-top: 100px;
}
.productos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}
.producto-card {
  border: 1px solid #ccc;
  padding: 16px;
  border-radius: 10px;
  background-color: #f9f9f9;
  color: black; /* 👈 Fuerza texto negro */
  box-shadow: 2px 2px 10px rgba(0,0,0,0.05);
}
.cantidad-selector {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
  gap: 10px;
}
.cantidad-selector button {
  padding: 6px 12px;
  font-size: 16px;
  cursor: pointer;
}
.confirmar-btn {
  margin-top: 30px;
  padding: 12px 24px;
  background-color: #0077cc;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
</style>
