<template>
  <v-card class="mb-4" elevation="4">
    <v-card-title class="text-h6 d-flex justify-space-between">
      {{ tarea.nombre }}
      <v-chip :color="tarea.completada ? 'green' : 'orange'" text-color="white" small>
        {{ tarea.completada ? 'Completada' : 'Pendiente' }}
      </v-chip>
    </v-card-title>

    <v-card-subtitle class="text-grey">
      {{ tarea.descripcion }}
    </v-card-subtitle>

    <v-card-text>
      <div><strong>Fecha término:</strong> {{ formatearFecha(tarea.fechaTermino) }}</div>
      <div v-if="tarea.sector">
        <strong>Sector:</strong> {{ tarea.sector.nombre }}
      </div>
    </v-card-text>

    <v-card-actions>

      <v-spacer></v-spacer>
      <v-btn
        v-if="!tarea.completada"
        color="success"
        @click="$emit('onCompletar', tarea.id)"
      >
        Marcar completada
      </v-btn>
      <v-btn color="error" @click="$emit('onEliminar', tarea.id)">Eliminar</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
defineProps({
  tarea: {
    type: Object,
    required: true
  }
});

const formatearFecha = (fecha) => {
  return new Date(fecha).toLocaleDateString();
};
</script>
