import axios from 'axios';

//Cargar las variables de entorno
const BackendServer = import.meta.env.VITE_BACKEND_SERVER;
const BackendPort = import.meta.env.VITE_BACKEND_PORT;

//Imprimir para verificar que las variables se cargan correctamente
console.log("Backend Server:", BackendServer);
console.log("Backend Port:", BackendPort);

//Crear instancia de Axios
export default axios.create({
    baseURL: `http://${BackendServer}:${BackendPort}`, // URL base del backend
    headers: {
        'Content-Type': 'application/json',
    },
});
