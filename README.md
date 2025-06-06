# Control-2-TBD

Este repositorio contiene todos los archivos relacionados con el Control N°2 de Base de Datos Avanzadas desarrollado por el grupo 3.

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
* Postgres SQL última versión + PostGIS habilitado
* PgAdmin versión 4.
* IntelliJ IDEA.
* VUE versión 3.
* Postman.
* Visual Studio Code.
* Docker (para despliegue)
* Jenkins (opcional para CI/CD)

## Instrucciones de uso
1. Clona el repositorio en tu máquina local usando el siguiente comando:
```sh
git clone https://github.com/Sembit26/Control-2-TBD.git
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
3. Ejecutar el backend
* Abre la carpeta Backend en IntelliJ IDEA.
* Ejecuta la aplicación haciendo clic en la opción "Run".

Lo anterior creara la estructura base del proyecto (tablas).

4. Crear cliente:
   Utiliza Postman para crear un primer usuario enviando una solicitud POST a:
```sh
http://localhost:8090/api/usuario/register
```
Con el siguiente cuerpo JSON:
```sh
{
  "username": "cliente",
  "correo": "cliente1@gmail.com",
  "contrasena": "admin",
  "ubicacion": {
    "type": "Point",
    "coordinates": [-70.6506, -33.4372]
  }
}
```

Este cliente es para efectos practicos de la carga de datos, básicamente para que se pueda ver los resumenes asociados a este cliente
Otra alternativa para esto es a través del "register" del frontend, la configuración y ejeccución de este se encuentra en el apartado 6 (recomendado)
5. Cargar datos en la base de datos:
   Una vez registrado el ususario inicial en la base de datos, 
    desde la consola de PostgreSQL, ejecuta los siguientes comandos:
```sh
psql -U postgres
```
Ingresa la contraseña del usuario postgres cuando se solicite.
* Carga los datos para la base de datos:
```sh
\i C:/ruta/loadData.sql  
```
* Puedes iniciar sesión con los datos de los clientes cargados mediante el archivo loadData, ya que si bien al momento de registrar un usuario por Postman (o resgiter del Frontend) su contraseña queda encriptada, al momento de cargar los datos "manualmente" no lo hace, por lo que se permitió que solo los clientes que se carguen a mano y tengan en su correo @example.com puedan iniciar sesión con una contraseña sin encriptación, esto en caso de desear corroborar su tareas asociadas.
* Para verificar lo anterior, si lo desea, pruebe ingresando el correo "user3@example.cl" de "usuario3" con su contraseña "pass3", con lo cual no se permitirá su ingreso pues su correo no termina en "@example.com". Por otro lado, "user12@example.com" con contraseña "pass12" si le permitirá ingresar.

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



### Despliegue con Docker 

Puedes levantar todo el sistema con Docker. Asegúrate de tener instalado Docker y Docker Compose.

1. Crea un archivo `Dockerfile` en el backend con este contenido:

```Dockerfile
# Dockerfile
FROM openjdk:17
VOLUME /tmp
COPY target/backend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

2. En la raíz del proyecto, crea un archivo `docker-compose.yml` como este:

```yaml
version: '3.8'

services:
  db:
    image: postgis/postgis
    container_name: postgres_tdb
    restart: always
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
      POSTGRES_DB: tdb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: backend_tdb
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tdb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: password
    ports:
      - "8090:8090"

  frontend:
    container_name: frontend_tdb
    build: ./frontend
    ports:
      - "5173:5173"
    depends_on:
      - backend

volumes:
  pgdata:
```

3. Ejecuta el sistema con:

```sh
docker-compose up --build
```

---

###  CI/CD con Jenkins (opcional) 

Si deseas automatizar el despliegue, puedes usar el siguiente `Jenkinsfile` para construir y desplegar tanto backend como frontend:

```groovy
pipeline {
  agent any

  environment {
    DOCKER_IMAGE_BACKEND = 'backend-tdb'
    DOCKER_IMAGE_FRONTEND = 'frontend-tdb'
  }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/tuusuario/tu-repo.git'
      }
    }

    stage('Build Backend') {
      steps {
        dir('backend') {
          sh './mvnw clean package -DskipTests'
        }
      }
    }

    stage('Build Docker Images') {
      steps {
        sh 'docker-compose build'
      }
    }

    stage('Deploy Containers') {
      steps {
        sh 'docker-compose down'
        sh 'docker-compose up -d'
      }
    }
  }

  post {
    success {
      echo 'Despliegue exitoso ' 
    }
    failure {
      echo 'Error en el pipeline ' 
    }
  }
}
```

>  Este pipeline: 
>
> * Clona el repositorio
> * Compila el backend
> * Construye las imágenes Docker
> * Despliega automáticamente los contenedores


