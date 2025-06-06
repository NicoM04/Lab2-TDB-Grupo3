# Laboratorio 1 - TDB - Grupo 3
Este repositorio contiene todos los archivos relacionados con el Laboratorio N°1 del Laboratorio de Base de Datos Avanzadas desarrollado por el grupo 1.

**IMPORTANTE:**
- Para poder conectarse correctamente con la base de datos y el backend, asegurate de lo siguientes:
    1. Configura el archivo application.properties en la carpeta resources del backend:
        * Actualiza el usuario y contraseña de PostgreSQL. (usuario: postgres, contraseña: password por defecto)
        * Ajusta el puerto de conexión de la base de datos (5432 por defecto).
        * Configura el puerto donde estará disponible la aplicación Spring Boot (8090 por defecto).

    2. En caso de modificar la dirección del servidor o el puerto en application.properties, actualiza también el archivo .env en el frontend para asegurar que ambos coincidan.
    3. Por defecto la aplicación se ejecuta en el puerto 5173.

## Requisitos previos
Para ejecutar correctamente todo el proyecto, además de los archivos del repositorio se requieren las siguientes tecnologías:
* Postgres SQL última versión
* PgAdmin versión 4.
* IntelliJ IDEA.
* VUE versión 3.
* Postman.
* Visual Studio Code.

## Instrucciones de uso
1. Clona el repositorio en tu máquina local usando el siguiente comando:
```sh
git clone https://github.com/NicoM04/Lab1-TDB-Grupo3.git
```

2. Configurar la base de datos
* Abre pgAdmin o utiliza la consola de PostgreSQL.
* Ejecuta los siguientes comandos en la consola:
```sh
psql -U postgres
```
Ingresa la contraseña del usuario postgres cuando se solicite.
* Carga el archivo de creación de la base de datos:
```sh
\i C:/ruta/DBCreate.sql  
```

Esto creará la estructura de la base de datos necesaria para la aplicación.

3. Ejecutar el backend
* Abre la carpeta Backend en IntelliJ IDEA.
* Ejecuta la aplicación haciendo clic en la opción "Run".

4. Crear cliente:
   Utiliza Postman para crear un primer usuario enviando una solicitud POST a:
```sh
http://localhost:8090/cliente/register
```
Con el siguiente cuerpo JSON:
```sh
{
  "nombre_cliente": "Cliente 1",
  "correo_cliente": "cliente1@gmail.com",
  "contrasena_cliente": "admin",
  "direccion": "Calle Ficticia 123, Ciudad, País",
  "telefono": "+1234567890",
  "fecha_registro": "2025-04-30T12:00:00"
}

```

Este cliente es para efectos practicos de la carga de datos, básicamente para que se pueda ver los resumenes asociados a este cliente

5. Cargar datos en la base de datos:
   Desde la consola de PostgreSQL, ejecuta los siguientes comandos:
```sh
psql -U postgres
```
Ingresa la contraseña del usuario postgres cuando se solicite.
* Carga los datos para la base de datos:
```sh
\i C:/ruta/loadDB.sql  
```
* Puedes iniciar sesión con los datos de los clientes cargados mediante el archivo loadDB, ya que si bien al momento de registrar un usuario por Postman
* su contraseña queda encriptada, al momento de cargar los datos "manualmente" no lo hace, por lo que se permitio que solo los clientes que se carguen a mano y tengan
* en su correo @example.com puedan iniciar sesión con una contraseña sin encriptacion, esto para efectos de que si se desea corroborar su resumen de pedidos. Para
* verificar lo anterior, si lo desea, pruebe ingresando el correo "ana.torres@prueba.com" de "Ana Torre" con su contraseña "contraseña", con lo cual no se permitira su
* ingreso pues su correo no termina en @example.com. Por otro lado, "luis.rojas@example.com" con contraseña "contraseña" si le dejera ingresar.

6. Configurar y ejecutar el frontend:
   Dentro de la carpeta Frontend, abre la consola y ejecuta los siguientes comandos para instalar las dependencias y levantar el frontend:
```sh
npm install
npm install axios
npm install vue-cookies --save
npm install jwt-decode
npm run dev
```
7. Uso de la aplicación
* Accede a la aplicación usando las credenciales del cliente creado en el paso 4.
