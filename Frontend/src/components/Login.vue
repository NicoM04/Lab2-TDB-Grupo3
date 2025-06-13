<template>
  <div class="login-container">
    <h2>Iniciar sesión</h2>
    <form @submit.prevent="handleLogin">
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

      <button type="submit">Iniciar sesión</button>
    </form>
    <p>
      ¿No tienes cuenta? <router-link to="/register">Regístrate aquí</router-link>
    </p>
  </div>
</template>

<script>
import ClienteService from "@/services/Cliente.service"; // Importar el servicio de cliente
import { useRouter } from "vue-router"; // Importar Vue Router para redirigir al dashboard

export default {
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    async handleLogin() {
      try {
        // Llamada al método loginCliente del servicio ClienteService
        const response = await ClienteService.loginCliente(this.email, this.password);
        console.log("Login exitoso", response.data);

        // Si el login es exitoso, guarda el token en el localStorage
        localStorage.setItem("jwt", response.data);
        
        // Ahora, obtenemos el id_cliente utilizando el correo electrónico y el token
        const clienteResponse = await ClienteService.buscarPorCorreo(this.email, response.data); // Llamamos al servicio con el correo y el token

        // Guardamos el id_cliente en localStorage
        localStorage.setItem("id_cliente", clienteResponse.data.id_cliente);


        // Redirige al dashboard o página principal
        this.$router.push({ name: "home" });
      } catch (error) {
        console.error("Error en el login", error);
        alert("Credenciales incorrectas.");
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* Centrar horizontalmente */
  justify-content: center; /* Centrar verticalmente */
  width: 100%; /* Ocupa todo el ancho disponible */
  height: 90%;
  max-width: 400px; /* Ancho máximo del formulario */
  margin: 0 auto; /* Centrar horizontalmente */
  padding: 40px; /* Aumentar el padding */
  background-color: #007bff;
  color: rgb(255, 255, 255);
  border-radius: 12px; /* Bordes más redondeados */
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2); /* Agregar sombra */
  text-align: center; /* Centrar el texto */
}

.form-group {
  margin-bottom: 1.5em; /* Aumentar el espacio entre los campos */
  color: rgb(255, 255, 255);
  width: 100%; /* Asegurar que los campos ocupen todo el ancho */
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
