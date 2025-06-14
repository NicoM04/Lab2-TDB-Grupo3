<template>
  <div class="desempeno-repartidor-container">
    <h2>Desempeño de los Repartidores</h2>

    <!-- Filtro de reparto -->
    <div class="filters">
      <label for="repartidor">Seleccionar Repartidor:</label>
      <select v-model="repartidorSeleccionado">
        <option value="">Todos</option>
        <option
          v-for="repartidor in repartidores"
          :key="repartidor.id_repartidor"
          :value="repartidor.id_repartidor"
        >
          {{ repartidor.nombre_repartidor }}
        </option>
      </select>
      <button @click="filtrarDesempeno">Buscar</button>
    </div>

    <!-- Tabla de desempeño -->
    <section class="performance">
      <h3>Desempeño de Repartidores</h3>
      <table>
        <thead>
          <tr>
            <th>Repartidor</th>
            <th>Entregas Completadas</th>
            <th>Puntuación Promedio</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="repartidor in repartidoresDesempeno" :key="repartidor.id_repartidor">
            <td>{{ repartidor.nombre_repartidor }}</td>
            <td>{{ repartidor.cantidad_entregas }}</td>
            <td>{{ Number(repartidor.puntuacionPromedio || 0).toFixed(2) }}</td> <!-- Mostrar puntuación promedio -->
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script>
import RepartidorService from "@/services/Repartidor.service"; // Importamos el servicio de Repartidor
import CalificacionService from "@/services/Calificacion.service"; // Importamos el servicio de Calificación

export default {
  data() {
    return {
      repartidores: [], // Lista de repartidores
      repartidoresDesempeno: [], // Desempeño filtrado de los repartidores
      repartidorSeleccionado: "", // Repartidor seleccionado para filtrar
    };
  },
  created() {
    this.obtenerRepartidores();
  },
  methods: {
    // Obtener todos los repartidores
    async obtenerRepartidores() {
      const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
      try {
        const response = await RepartidorService.getAllRepartidores(token); // Llamamos al servicio para obtener los repartidores
        this.repartidores = response.data; // Asignamos los repartidores obtenidos
        this.obtenerDesempenoRepartidores(); // Obtener el desempeño de los repartidores
      } catch (error) {
        console.error("Error al obtener repartidores", error); // En caso de error, lo mostramos en consola
      }
    },

    // Obtener desempeño de los repartidores
    async obtenerDesempenoRepartidores() {
      const token = localStorage.getItem("jwt"); // Obtener el token del localStorage
      try {
        const response = await RepartidorService.getAllRepartidores(token); // Llamamos al servicio para obtener los repartidores

        let repartidoresDesempeno = await Promise.all(response.data.map(async (repartidor) => {
          // Obtener todas las calificaciones de cada repartidor
          const calificacionResponse = await CalificacionService.getCalificacionesByRepartidorId(repartidor.id_repartidor, token);
          const calificaciones = calificacionResponse.data;


          // Sumar las puntuaciones
          let totalPuntuaciones = 0;
          let cantidadCalificaciones = 0;

          // Verificar que calificaciones sea un arreglo
          if (calificaciones && Array.isArray(calificaciones)) {
            calificaciones.forEach((calificacion) => {
              if (calificacion.puntuacion) {
                totalPuntuaciones += calificacion.puntuacion; // Sumamos las puntuaciones
                cantidadCalificaciones++; // Contamos cuántas calificaciones tiene
              }
            });
          }

          // Calcular el promedio solo si tiene calificaciones
          let puntuacionPromedio = 0;
          if (cantidadCalificaciones > 0) {
            puntuacionPromedio = totalPuntuaciones / cantidadCalificaciones; // Promedio
          }
          // Asociar los datos de desempeño
          return {
            ...repartidor,
            puntuacionPromedio: puntuacionPromedio, // Asignamos el promedio de las puntuaciones
            cantidad_entregas: repartidor.cantidad_entregas, // Mantenemos la cantidad de entregas
          };
        }));

        this.repartidoresDesempeno = repartidoresDesempeno; // Asignamos los datos de desempeño
      } catch (error) {
        console.error("Error al obtener desempeño de repartidores", error); // En caso de error, lo mostramos en consola
      }
    },



    // Filtrar desempeño según repartidor seleccionado
    filtrarDesempeno() {
      if (this.repartidorSeleccionado) {
        const idSeleccionado = parseInt(this.repartidorSeleccionado);
        this.repartidoresDesempeno = this.repartidores.filter(
          (repartidor) => repartidor.id_repartidor === idSeleccionado
        ).map((repartidor) => {
          const encontrado = this.repartidoresDesempeno.find(r => r.id_repartidor === idSeleccionado);
          return {
            ...repartidor,
            puntuacionPromedio: encontrado ? encontrado.puntuacionPromedio : 'N/A', // Se muestra la puntuación promedio
            cantidad_entregas: repartidor.cantidad_entregas,
          };
        });
      } else {
        this.obtenerDesempenoRepartidores(); // Sin filtro
      }
    },
  },
};
</script>

<style scoped>
.desempeno-repartidor-container {
  padding: 40px 20px;
  max-width: 900px;
  margin: 0 auto;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #2c3e50;
}

.filters {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 30px;
}

.filters label {
  font-weight: 600;
  color: #444;
}

.filters select {
  padding: 8px 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  min-width: 200px;
}

section {
  margin-bottom: 40px;
}

.performance h3 {
  font-size: 1.3rem;
  margin-bottom: 15px;
  color: #34495e;
  text-align: center;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fdfdfd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

thead {
  background-color: #007bff;
  color: #fff;
}

th,
td {
  padding: 14px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

tbody tr:hover {
  background-color: #f7f7f7;
}

td {
  color: #333;
  font-weight: 500;
}

@media (max-width: 600px) {
  .filters {
    flex-direction: column;
    align-items: stretch;
  }

  .filters select {
    width: 100%;
  }

  table,
  thead,
  tbody,
  th,
  td,
  tr {
    display: block;
  }

  tr {
    margin-bottom: 1rem;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
    padding: 10px;
  }

  td,
  th {
    text-align: left;
    padding: 10px;
    border: none;
    border-bottom: 1px solid #eee;
  }

  thead {
    display: none;
  }
  .filters button {
  padding: 8px 16px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
}

.filters button:hover {
  background-color: #0056b3;
}
}

</style>
