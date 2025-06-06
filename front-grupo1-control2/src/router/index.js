import { createRouter, createWebHistory } from 'vue-router';

import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';
import ListaTareas from '@/components/ListaTareas.vue';
import CrearTarea from '@/components/CrearTarea.vue';
import EditarTarea from '@/components/EditarTarea.vue';
import DetalleTarea from '@/components/DetalleTarea.vue';
import Notificaciones from '@/components/Notificaciones.vue';
import Estadisticas from '@/components/Estadisticas.vue';
import MapaSectores from '@/components/MapaSectores.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login, meta: { hideNavBar: true } },
  { path: '/register', name: 'Register', component: Register, meta: { hideNavBar: true } },
  { path: '/tareas', name: 'ListaTareas', component: ListaTareas },
  { path: '/tareas/crear', name: 'CrearTarea', component: CrearTarea },
  { path: '/tareas/editar/:id', name: 'EditarTarea', component: EditarTarea, props: true },
  { path: '/tareas/detalle/:id', name: 'DetalleTarea', component: DetalleTarea, props: true },
  { path: '/notificaciones', name: 'Notificaciones', component: Notificaciones },
  { path: '/estadisticas', name: 'Estadisticas', component: Estadisticas },
  { path: '/mapa', name: 'MapaSectores', component: MapaSectores },
];
const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
