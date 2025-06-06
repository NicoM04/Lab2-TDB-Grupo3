<template>
  <v-row dense class="mb-4">
    <v-col cols="12" md="4">
      <v-select
        v-model="estadoLocal"
        :items="estados"
        label="Filtrar por estado"
        clearable
      />
    </v-col>

    <v-col cols="12" md="8">
      <v-text-field
        v-model="palabraClaveLocal"
        label="Buscar por palabra clave"
        clearable
      />
    </v-col>

    <v-col cols="12" class="d-flex justify-end">
      <v-btn color="primary" @click="emitirFiltros">
        Confirmar filtro
      </v-btn>
    </v-col>
  </v-row>
</template>

<script setup>
import { ref } from 'vue';

const emit = defineEmits(['filtrar']);

const estadoLocal = ref(null);
const palabraClaveLocal = ref('');

const estados = [
  { title: 'Todas', value: null },
  { title: 'Pendiente', value: false },
  { title: 'Completada', value: true },
];

const emitirFiltros = () => {
  emit('filtrar', { estado: estadoLocal.value, palabra: palabraClaveLocal.value });
};
</script>