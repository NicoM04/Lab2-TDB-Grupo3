import { createRouter, createWebHistory } from 'vue-router';
import Dashboard from '@/components/Dashboard.vue';
import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';
import PedidosResumenCliente from '@/components/PedidosResumenCliente.vue';
import DesempenoRepartidor from '@/components/DesempenoRepartidor.vue';
import EmpresasMayorVolumen from '@/components/EmpresasMayorVolumen.vue';
import Notificaciones from '@/components/Notificaciones.vue';
import ProductoDetalle from '@/components/ProductoDetalle.vue';
import ProductosMasPedidos from '@/components/ProductosMasPedidos.vue';
import Carrito from '@/components/Carrito.vue';
import ConsultasCliente from "@/components/ConsultasCliente.vue";
import Empresas from "@/components/Empresas.vue";
import SeleccionProductos from "@/components/SeleccionProductos.vue";

//Definir las rutas de la aplicacion
const routes = [
    {
    path: '/',
    redirect: '/login',
    component: Login,
    meta: { requiresAuth: false }
  },
    {
    path: "/home",
    name: "home",
    component: Dashboard,
    meta: { requiresAuth: true }
  },
    {
    path: "/login",
    name: "login",
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: "/register",
    name: "register",
    component: Register,
    meta: { requiresAuth: false }
  },
  {
    path: '/cliente/pedidos-resumen',
    name: 'pedidos-resumen',
    component: PedidosResumenCliente,
    meta: { requiresAuth: true }
  },
    {
    path: '/repartidores/desempeno',
    name: 'desempeno-repartidor',
    component: DesempenoRepartidor,
    meta: { requiresAuth: true }
  },
    {
    path: '/empresas/mayorvolumen',
    name: 'empresas-mayorvolumen',
    component: EmpresasMayorVolumen,
    meta: { requiresAuth: true }
  },
    {
    path: '/notificaciones',
    name: 'notificaciones',
    component: Notificaciones,
    meta: { requiresAuth: true }
  },
    {
    path: '/producto/:id',
    name: 'producto-detalle',
    component: ProductoDetalle,
    meta: { requiresAuth: true }
  },
    {
    path: '/productos/mas-pedidos',
    name: 'productos-mas-pedidos',
    component: ProductosMasPedidos,
    meta: { requiresAuth: true }
  },
  {
    path: '/carrito',
    name: 'carrito',
    component: Carrito,
    meta: { requiresAuth: true }
  },
  {
    path: "/consultas-cliente",
    component: ConsultasCliente,
  },
    {
    path: '/empresas',
    name: 'empresas',
    component: Empresas,
  },
  {
    path: '/productos',
    name: 'seleccion-productos',
    component: SeleccionProductos,
    props: true,
  },

];


//Crear el router
const router = createRouter({
    history: createWebHistory(),
    routes,
  });
  
//Proteger las rutas exclusivas para usuarios admin
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    const userRole = localStorage.getItem("userRole");
    if (userRole === 'admin') {
      next();
    } else {
      next({ name: 'products' });
    }
  } else {
    next();
  }
});
  
export default router;