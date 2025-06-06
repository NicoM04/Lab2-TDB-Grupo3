<template>
  <div class="register-container">
    <h2>Crear cuenta</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">Nombre</label>
        <input
          type="text"
          id="name"
          v-model="name"
          required
          placeholder="Ingresa tu nombre"
        />
      </div>

      <div class="form-group">
        <label for="email">Correo electrónico</label>
        <input
          type="email"
          id="email"
          v-model="email"
          required
          placeholder="Ingresa tu correo"
        />
      </div>

      <div class="form-group">
        <label for="password">Contraseña</label>
        <input
          type="password"
          id="password"
          v-model="password"
          required
          placeholder="Ingresa tu contraseña"
        />
      </div>

      <div class="form-group">
        <label for="direccion">Dirección</label>
        <input
          type="text"
          id="direccion"
          v-model="direccion"
          required
          placeholder="Ingresa tu dirección"
        />
      </div>

      <div class="form-group">
        <label for="telefono">Teléfono</label>
        <input
          type="text"
          id="telefono"
          v-model="telefono"
          required
          placeholder="Ingresa tu teléfono"
        />
      </div>


      <button type="submit">Registrarse</button>
    </form>
    <p>
      ¿Ya tienes cuenta? <router-link to="/login">Inicia sesión aquí</router-link>
    </p>
  </div>
</template>

<script>
import ClienteService from '@/services/Cliente.service'; 

export default {
  data() {
    return {
      name: "",
      email: "",
      password: "",
      direccion: "",
      telefono: "",
      fecha_registro: new Date().toISOString().slice(0, 19), // Establecer la fecha actual como predeterminada
    };
  },
  methods: {
    async handleRegister() {
      try {
        // Llamar al método del service para registrar el cliente
        const response = await ClienteService.registerCliente(
          {
            nombre_cliente: this.name,
            correo_cliente: this.email,
            contrasena_cliente: this.password,
            direccion: this.direccion,
            telefono: this.telefono,
            fecha_registro: this.fecha_registro, 
          }
        );

        // Si el registro es exitoso, redirigir al login
        this.$router.push({ name: "login" });
      } catch (error) {
        console.error("Error en el registro", error);
        alert("Hubo un error al registrar la cuenta.");
      }
    },
  },
};
</script>

<style scoped>
.register-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%; /* Ocupa todo el ancho disponible */
  height: 90%;
  max-width: 400px; /* Ancho máximo del formulario */
  margin: 0 auto; /* Centrar horizontalmente */
  padding: 40px; /* Aumentar el padding */
  background-color: #007bff;
  color: rgb(255, 255, 255);
  border-radius: 12px; /* Bordes más redondeados */
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2); /* Agregar sombra */
}

.form-group {
  margin-bottom: 1.5em; /* Aumentar el espacio entre los campos */
  color: rgb(255, 255, 255);
}

label {
  display: block;
  margin-bottom: 0.5em;
}

input {
  width: 100%;
  padding: 12px; /* Aumentar el padding */
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem; /* Aumentar el tamaño de la fuente */
}

button {
  width: 100%;
  padding: 12px; /* Aumentar el padding */
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem; /* Aumentar el tamaño de la fuente */
}

button:hover {
  background-color: #218838;
}
</style>
