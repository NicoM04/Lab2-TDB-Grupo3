-- 1. Crear la base de datos

CREATE DATABASE "lab2_grupo3"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Chile.1252'
    LC_CTYPE = 'Spanish_Chile.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False
    TEMPLATE = template0;

-- 2. Conectarse a la base de datos
\connect lab2_grupo3

-- Activar extensión PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;


-- 3. Crear las tablas

-- Tabla: Cliente
CREATE TABLE Cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(100),
    contrasena_cliente VARCHAR(100),
    correo_cliente VARCHAR(100),
    direccion VARCHAR(100),
    telefono VARCHAR(15),
    fecha_registro DATE
);

-- Tabla: Empresas_Asociadas
CREATE TABLE Empresas_Asociadas (
    id_empresa SERIAL PRIMARY KEY,
    nombre_empresa VARCHAR(100),
    rut_empresa VARCHAR(20),
    correo_contacto VARCHAR(100),
    direccion VARCHAR(100)
);

-- Tabla: Repartidores
CREATE TABLE Repartidores (
    id_repartidor SERIAL PRIMARY KEY,
    nombre_repartidor VARCHAR(100),
    rut VARCHAR(20),
    telefono VARCHAR(15),
    fecha_contratacion DATE,
    activo BOOLEAN,
    cantidad_entregas INT
);

-- Tabla: Medios_de_pago
CREATE TABLE Medios_de_pago (
    id_pago SERIAL PRIMARY KEY,
    metodo_pago VARCHAR(100),
    fecha_pago DATE,
    monto_total INT
);

-- Tabla: Pedido
CREATE TABLE Pedido (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INT REFERENCES Cliente(id_cliente),
    id_empresa INT REFERENCES Empresas_Asociadas(id_empresa),
    id_repartidor INT REFERENCES Repartidores(id_repartidor),
    id_pago INT REFERENCES Medios_de_pago(id_pago),
    fecha_pedido DATE,
    fecha_entrega DATE NULL,  -- Aquí se permite que fecha_entrega sea NULL
    estado VARCHAR(100),
    urgente BOOLEAN
);

-- Tabla: ProductoServicio
CREATE TABLE ProductoServicio (
    id_producto SERIAL PRIMARY KEY,
    nombre_producto VARCHAR(255),
    descripcion TEXT,
    categoria VARCHAR(50),
    precio_unitario DECIMAL(10,2),
    stock INT
);

-- Tabla: Detalle_de_pedido
CREATE TABLE Detalle_de_pedido (
    id_detalle SERIAL PRIMARY KEY,
    id_producto INT REFERENCES ProductoServicio(id_producto),
    id_pedido INT REFERENCES Pedido(id_pedido),
    cantidad INT,
    subtotal DECIMAL(10,2)
);

-- Tabla: Notificacion
CREATE TABLE Notificacion (
    id_notificacion SERIAL PRIMARY KEY,
    id_pedido INT REFERENCES Pedido(id_pedido),
    fecha_creacion DATE,
    mensaje VARCHAR(100),
    tipo VARCHAR(50),
    leida BOOLEAN,
    descripcion VARCHAR(200)
);

-- Tabla: Calificaciones
CREATE TABLE Calificaciones (
    id_calificacion SERIAL PRIMARY KEY,
    id_repartidor INT REFERENCES Repartidores(id_repartidor),
    puntuacion INT,
    comentario VARCHAR(100),
    fecha_calificacion DATE
);


-- Agregar campos de ubicación geográfica a las tablas
ALTER TABLE cliente ADD COLUMN ubicacion GEOMETRY(Point, 4326);
ALTER TABLE repartidores ADD COLUMN ubicacion_actual GEOMETRY(Point, 4326);
ALTER TABLE empresas_asociadas ADD COLUMN ubicacion GEOMETRY(Point, 4326);
ALTER TABLE pedido ADD COLUMN ruta_estimada GEOMETRY(LineString, 4326); --ruta

-- Crear tabla de zonas de cobertura
CREATE TABLE zonas_cobertura (
    zona_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    tipo VARCHAR(100),
    geom GEOMETRY(Polygon, 4326)
);


-- Procedimiento almacenado: Registrar un pedido completo con ruta estimada
CREATE OR REPLACE PROCEDURE registrar_pedido_completo(
    p_id_cliente INT,
    p_id_empresa INT,
    p_id_repartidor INT,
    p_fecha_pedido DATE,
    p_fecha_entrega DATE,
    p_estado VARCHAR,
    p_urgente BOOLEAN,
    p_metodo_pago VARCHAR,
    p_productos INT[],         -- Array de IDs de producto
    p_cantidades INT[]         -- Array de cantidades correspondientes
)
LANGUAGE plpgsql AS $$
DECLARE
    i INT;
    v_id_pago INT;
    v_id_pedido INT;
    v_id_producto INT;
    v_cantidad INT;
    v_precio DECIMAL(10,2);
    v_subtotal DECIMAL(10,2);
    v_total DECIMAL(10,2) := 0;
    v_ubicacion_empresa GEOMETRY(Point, 4326);
    v_ubicacion_cliente GEOMETRY(Point, 4326);
BEGIN
    -- Calcular total del pedido recorriendo productos
    FOR i IN 1..array_length(p_productos, 1) LOOP
        v_id_producto := p_productos[i];
        v_cantidad := p_cantidades[i];

        SELECT precio_unitario INTO v_precio
        FROM ProductoServicio
        WHERE id_producto = v_id_producto;

        v_subtotal := v_precio * v_cantidad;
        v_total := v_total + v_subtotal;
    END LOOP;

    -- Insertar en medios de pago
    INSERT INTO Medios_de_pago (metodo_pago, fecha_pago, monto_total)
    VALUES (p_metodo_pago, CURRENT_DATE, v_total)
    RETURNING id_pago INTO v_id_pago;

    -- Insertar el pedido
    INSERT INTO Pedido (
        id_cliente, id_empresa, id_repartidor, id_pago,
        fecha_pedido, fecha_entrega, estado, urgente
    )
    VALUES (
        p_id_cliente, p_id_empresa, p_id_repartidor, v_id_pago,
        p_fecha_pedido, p_fecha_entrega, p_estado, p_urgente
    )
    RETURNING id_pedido INTO v_id_pedido;

    -- Obtener ubicaciones
    SELECT ubicacion INTO v_ubicacion_empresa
    FROM Empresas_Asociadas
    WHERE id_empresa = p_id_empresa;

    SELECT ubicacion INTO v_ubicacion_cliente
    FROM Cliente
    WHERE id_cliente = p_id_cliente;

    -- Insertar ruta estimada si ambas ubicaciones son válidas
    IF v_ubicacion_empresa IS NOT NULL AND v_ubicacion_cliente IS NOT NULL THEN
        UPDATE Pedido
        SET ruta_estimada = ST_MakeLine(ARRAY[
            v_ubicacion_empresa,
            v_ubicacion_cliente
        ])
        WHERE id_pedido = v_id_pedido;
    END IF;

    -- Insertar detalles de pedido
    FOR i IN 1..array_length(p_productos, 1) LOOP
        v_id_producto := p_productos[i];
        v_cantidad := p_cantidades[i];

        SELECT precio_unitario INTO v_precio
        FROM ProductoServicio
        WHERE id_producto = v_id_producto;

        v_subtotal := v_precio * v_cantidad;

        INSERT INTO Detalle_de_pedido (
            id_producto, id_pedido, cantidad, subtotal
        )
        VALUES (
            v_id_producto, v_id_pedido, v_cantidad, v_subtotal
        );
    END LOOP;
END;
$$;







-- Procedimiento almacenado: Descontar stock al confirmar pedido (si aplica)
CREATE OR REPLACE PROCEDURE confirmar_pedido_y_descontar_stock(p_id_pedido INT)
AS $$
DECLARE
    r RECORD;
    v_stock_actual INT;
BEGIN
    -- Verificamos que el pedido esté pendiente (opcional)
    IF EXISTS (
        SELECT 1 FROM Pedido WHERE id_pedido = p_id_pedido AND estado != 'Pendiente'
    ) THEN
        RAISE EXCEPTION 'El pedido ya fue confirmado o no está en estado Pendiente.';
    END IF;

    -- Recorremos los productos del pedido
    FOR r IN
        SELECT dp.id_producto, dp.cantidad, ps.stock
        FROM Detalle_de_pedido dp
        JOIN ProductoServicio ps ON dp.id_producto = ps.id_producto
        WHERE dp.id_pedido = p_id_pedido
    LOOP
        -- Verificamos si hay suficiente stock
        IF r.stock < r.cantidad THEN
            RAISE EXCEPTION 'No hay suficiente stock para el producto ID % (stock disponible: %, requerido: %)',
                r.id_producto, r.stock, r.cantidad;
        END IF;
    END LOOP;

    -- Si todo está bien, descontamos stock
    FOR r IN
        SELECT dp.id_producto, dp.cantidad
        FROM Detalle_de_pedido dp
        WHERE dp.id_pedido = p_id_pedido
    LOOP
        UPDATE ProductoServicio
        SET stock = stock - r.cantidad
        WHERE id_producto = r.id_producto;
    END LOOP;

    -- Cambiamos el estado del pedido a Confirmado
    UPDATE Pedido
    SET estado = 'Confirmado'
    WHERE id_pedido = p_id_pedido;
END;
$$ LANGUAGE plpgsql;


-- Procedimiento almacenado: Cambiar el estado de un pedido con validación.

CREATE OR REPLACE PROCEDURE cambiar_estado_pedido(
    p_id_pedido INT,
    p_nuevo_estado VARCHAR
)
AS $$
DECLARE
    v_estado_actual VARCHAR;
    v_id_cliente INT;
    v_id_empresa INT;
    v_mensaje VARCHAR(100);
BEGIN
    -- Obtener estado actual y cliente asociado
    SELECT estado, id_cliente, id_empresa
    INTO v_estado_actual, v_id_cliente, v_id_empresa
    FROM Pedido
    WHERE id_pedido = p_id_pedido;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'No existe un pedido con el ID %.', p_id_pedido;
    END IF;

    -- Validar que el nuevo estado no sea el mismo
    IF v_estado_actual = p_nuevo_estado THEN
        RAISE EXCEPTION 'El pedido ya está en estado "%".', p_nuevo_estado;
    END IF;

    -- Validar transición permitida
    IF v_estado_actual = 'Pendiente' AND p_nuevo_estado IN ('Confirmado', 'Cancelado') THEN
        NULL;
    ELSIF v_estado_actual = 'Confirmado' AND p_nuevo_estado = 'En reparto' THEN
        NULL;
    ELSIF v_estado_actual = 'En reparto' AND p_nuevo_estado = 'Entregado' THEN
        NULL;
    ELSIF v_estado_actual = 'Entregado' AND p_nuevo_estado = 'Finalizado' THEN
        NULL;
    ELSE
        RAISE EXCEPTION 'Transicion invalida de estado: "%" → "%".', v_estado_actual, p_nuevo_estado;
    END IF;

    -- Actualizar estado del pedido
    UPDATE Pedido
    SET estado = p_nuevo_estado
    WHERE id_pedido = p_id_pedido;

    -- Crear mensaje de notificación
    v_mensaje := FORMAT('El estado de tu pedido #%s ha cambiado a "%s".', p_id_pedido, p_nuevo_estado);

    -- Insertar notificación
    INSERT INTO Notificacion (
        id_pedido, fecha_creacion, mensaje, tipo, leida, descripcion
    ) VALUES (
        p_id_pedido,
        CURRENT_DATE,
        v_mensaje,
        'Estado pedido',
        FALSE,
        FORMAT('Tu pedido ha pasado de "%s" a "%s".', v_estado_actual, p_nuevo_estado)
    );
END;
$$ LANGUAGE plpgsql;




--TRIGGERS:
--Trigger: Insertar automáticamente la fecha de entrega al marcar como entregado.

CREATE OR REPLACE FUNCTION actualizar_fecha_entrega()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el nuevo estado es 'Entregado' y antes no lo era
    IF NEW.estado = 'Entregado' AND (OLD.estado IS DISTINCT FROM 'Entregado') THEN
        NEW.fecha_entrega := CURRENT_DATE;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;




--Trigger: Insertar una calificación automática si no se recibe en 48 horas.
CREATE OR REPLACE FUNCTION insertar_calificacion_automatica()
RETURNS TRIGGER AS $$
BEGIN
    -- Verificar que el estado es "Entregado"
    IF NEW.estado = 'Entregado' AND (OLD.estado IS DISTINCT FROM 'Entregado') THEN
        RAISE NOTICE 'Estado es "Entregado".';

        -- Verificar si han pasado 48 horas desde la fecha de pedido
        IF CURRENT_DATE - NEW.fecha_pedido >= 2 THEN
            RAISE NOTICE 'Han pasado mas de 48 horas desde el pedido.';

            -- Verificar si no existe una calificación para este repartidor y fecha de entrega
            IF NOT EXISTS (
                SELECT 1 FROM Calificaciones
                WHERE id_repartidor = NEW.id_repartidor
                AND fecha_calificacion = NEW.fecha_entrega
            ) THEN
                -- Insertar la calificación
                INSERT INTO Calificaciones (
                    id_repartidor, puntuacion, comentario, fecha_calificacion
                ) VALUES (
                    NEW.id_repartidor, 3,
                    'Calificacion automatica por no responder en 48 horas',
                    NEW.fecha_entrega
                );
                RAISE NOTICE 'Calificacion insertada.';
            ELSE
                RAISE NOTICE 'Ya existia una calificacion para ese repartidor y fecha.';
            END IF;
        ELSE
            RAISE NOTICE 'Aun no han pasado 48 horas desde el pedido.';
        END IF;
    ELSE
        RAISE NOTICE 'Estado no es "Entregado" o no ha cambiado.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;




--Trigger: Registrar una notificación si se detecta un problema crítico en el pedido.
CREATE OR REPLACE FUNCTION registrar_problema_critico()
RETURNS TRIGGER AS $$
DECLARE
    v_descripcion TEXT;
BEGIN
    -- Problema: Pedido cancelado
    IF NEW.estado = 'Cancelado' AND (OLD.estado IS DISTINCT FROM 'Cancelado') THEN
        v_descripcion := 'El pedido ha sido cancelado por un motivo no especificado.';

        INSERT INTO Notificacion (
            id_pedido, fecha_creacion, mensaje, tipo, leida, descripcion
        ) VALUES (
            NEW.id_pedido,
            CURRENT_DATE,
            FORMAT('El pedido #%s ha sido cancelado.', NEW.id_pedido),
            'Problema critico',
            FALSE,
            v_descripcion
        );
    END IF;

    -- Problema: Pedido confirmado sin repartidor
    IF NEW.estado = 'Confirmado' AND (OLD.estado IS DISTINCT FROM 'Confirmado') THEN
        IF NEW.id_repartidor IS NULL THEN
            v_descripcion := 'El pedido fue confirmado pero no tiene repartidor asignado.';

            INSERT INTO Notificacion (
                id_pedido, fecha_creacion, mensaje, tipo, leida, descripcion
            ) VALUES (
                NEW.id_pedido,
                CURRENT_DATE,
                FORMAT('Pedido #%s confirmado sin repartidor asignado.', NEW.id_pedido),
                'Problema critico',
                FALSE,
                v_descripcion
            );
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



--Trigger extra: Agregar validaciones especiales en triggers según el tipo de producto/servicio.
CREATE OR REPLACE FUNCTION validar_detalle_pedido()
RETURNS TRIGGER AS $$
DECLARE
    v_categoria VARCHAR(50);
    v_urgente BOOLEAN;
    v_id_repartidor INT;
    v_entregas INT;
BEGIN
    -- Obtener categoría del producto
    SELECT categoria INTO v_categoria
    FROM ProductoServicio
    WHERE id_producto = NEW.id_producto;

    -- Obtener si el pedido es urgente y el id_repartidor
    SELECT urgente, id_repartidor
    INTO v_urgente, v_id_repartidor
    FROM Pedido
    WHERE id_pedido = NEW.id_pedido;

    -- Obtener cantidad de entregas del repartidor
    SELECT cantidad_entregas INTO v_entregas
    FROM Repartidores
    WHERE id_repartidor = v_id_repartidor;

    -- Validaciones específicas por categoría
    IF v_categoria = 'Documento Legal' THEN
        IF NOT v_urgente THEN
            RAISE EXCEPTION 'Los documentos legales solo pueden enviarse con pedidos urgentes.';
        END IF;
        IF NEW.cantidad > 1 THEN
            RAISE EXCEPTION 'Solo se permite una unidad por pedido para documentos legales.';
        END IF;

    ELSIF v_categoria = 'Certificado Oficial' THEN
        IF v_entregas < 5 THEN
            RAISE EXCEPTION 'El repartidor debe tener al menos 5 entregas para transportar certificados oficiales.';
        END IF;
        IF NEW.cantidad > 1 THEN
            RAISE EXCEPTION 'Solo se permite una unidad por pedido para certificados oficiales.';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;




--Crear los triggers


CREATE TRIGGER trigger_actualizar_fecha_entrega
BEFORE UPDATE ON Pedido
FOR EACH ROW
EXECUTE FUNCTION actualizar_fecha_entrega();


CREATE TRIGGER trigger_insertar_calificacion_automatica
AFTER UPDATE ON Pedido
FOR EACH ROW
EXECUTE FUNCTION insertar_calificacion_automatica();


CREATE TRIGGER trigger_problema_critico_pedido
AFTER UPDATE ON Pedido
FOR EACH ROW
EXECUTE FUNCTION registrar_problema_critico();


CREATE TRIGGER trigger_validar_detalle
BEFORE INSERT OR UPDATE ON detalle_de_pedido
FOR EACH ROW
EXECUTE FUNCTION validar_detalle_pedido();



