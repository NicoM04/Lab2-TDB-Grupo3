-- Crear la base de datos (si aún no existe)
CREATE DATABASE control2_grupo3
    WITH OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Chile.1252'
    LC_CTYPE = 'Spanish_Chile.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False
    TEMPLATE template0;

-- Conectarse a la base
\connect control2_grupo3;

-- Activar la extensión PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;