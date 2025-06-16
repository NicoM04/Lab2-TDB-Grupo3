<template>
  <div class="empresas-container">
    <h1>Empresas Asociadas</h1>
    <div class="empresa-grid">
      <div
        v-for="empresa in empresas"
        :key="empresa.id_empresa"
        @click="seleccionarEmpresa(empresa.id_empresa)"
        class="empresa-card"
      >
        <h2>{{ empresa.nombre_empresa }}</h2>
        <p><strong>RUT:</strong> {{ empresa.rut_empresa }}</p>
        <p><strong>Correo:</strong> {{ empresa.correo_contacto }}</p>
        <p><strong>Dirección:</strong> {{ empresa.direccion }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import EmpresasService from '@/services/EmpresasAsociadas.service';

const empresas = ref([]);
const router = useRouter();

const cargarEmpresas = async () => {
  try {
    const token = localStorage.getItem("jwt");
    const response = await EmpresasService.getEmpresas(token);
    empresas.value = response.data;
  } catch (error) {
    console.error('Error al cargar empresas:', error);
  }
};

const seleccionarEmpresa = (id) => {
  localStorage.setItem('id_empresa', id);
  router.push({ name: 'seleccion-productos' });
};

onMounted(() => {
  cargarEmpresas();
});
</script>

<style scoped>
.empresas-container {
  padding: 20px;
}
.empresa-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.empresa-card {
  background-color: #f5f5f5;
  padding: 18px 22px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  cursor: pointer;
  min-width: 260px;
  max-width: 320px;
  flex: 1 1 260px;
  transition: background 0.2s, box-shadow 0.2s;
  border: 1px solid #e0e0e0;
}
.empresa-card:hover {
  background-color: #d6eaff;
  box-shadow: 0 4px 16px rgba(40, 53, 167, 0.12);
}
.empresa-card h2 {
  margin: 0 0 10px 0;
  font-size: 1.2rem;
  color: #2835a7;
}
.empresa-card p {
  margin: 4px 0;
  color: #222;
}
</style>