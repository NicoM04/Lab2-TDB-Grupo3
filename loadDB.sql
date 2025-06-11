SET client_encoding = 'UTF8';

INSERT INTO Cliente (nombre_cliente, contrasena_cliente, correo_cliente, direccion, telefono, fecha_registro, ubicacion) VALUES
('Ana Torres', 'contraseña', 'ana.torres@prueba.com', 'Av. Siempre Viva 123', '912345678', '2019-05-12', ST_GeomFromText('POINT(-70.6506 -33.4372)', 4326)), -- Santiago Centro
('Luis Rojas', 'contraseña', 'luis.rojas@example.com', 'Calle Falsa 456', '912345679', '2020-03-10', ST_GeomFromText('POINT(-70.6693 -33.4523)', 4326)), -- Providencia
('Carla Vega', 'contraseña', 'carla.vega@example.com', 'Jr. Los Álamos 22', '912345680', '2021-07-25', ST_GeomFromText('POINT(-70.6132 -33.5010)', 4326)), -- La Florida
('Pedro Sáez', 'contraseña', 'pedro.saez@example.com', 'Av. Las Flores 555', '912345681', '2022-01-13', ST_GeomFromText('POINT(-70.7852 -33.5950)', 4326)), -- Maipú
('Lucía Díaz', 'contraseña', 'lucia.diaz@example.com', 'Calle Lima 332', '912345682', '2023-02-01', ST_GeomFromText('POINT(-70.5455 -33.4160)', 4326)), -- Peñalolén
('Manuel Salas', 'contraseña', 'manuel.salas@example.com', 'Jr. Central 99', '912345683', '2020-09-20', ST_GeomFromText('POINT(-70.6692 -33.3811)', 4326)), -- Recoleta
('Sofía Herrera', 'contraseña', 'sofia.herrera@example.com', 'Av. Norte 118', '912345684', '2021-11-10', ST_GeomFromText('POINT(-70.7431 -33.4366)', 4326)), -- Pudahuel
('Rodrigo Ruiz', 'contraseña', 'rodrigo.ruiz@example.com', 'Pasaje Sol 11', '912345685', '2019-08-30', ST_GeomFromText('POINT(-70.6565 -33.4569)', 4326)), -- Ñuñoa
('Fernanda Pino', 'contraseña', 'fernanda.pino@example.com', 'Calle Sur 80', '912345686', '2023-06-18', ST_GeomFromText('POINT(-70.7082 -33.5202)', 4326)), -- San Bernardo
('Javier Soto', 'contraseña', 'javier.soto@example.com', 'Av. del Mar 300', '912345687', '2024-01-09', ST_GeomFromText('POINT(-70.5812 -33.6002)', 4326)); -- Puente Alto


INSERT INTO Empresas_Asociadas (nombre_empresa, rut_empresa, correo_contacto, direccion, ubicacion) VALUES
('Estudio Jurídico LegalPro', '76451234-5', 'contacto@legalpro.com', 'Av. Libertad 101', ST_GeomFromText('POINT(-70.6420 -33.4206)', 4326)), -- Independencia
('Notaría San Martín', '76321233-8', 'info@notariasanmartin.com', 'Jr. Constitución 201', ST_GeomFromText('POINT(-70.7200 -33.4500)', 4326)), -- Cerrillos
('Corporación Educativa EducaTec', '76578921-2', 'admin@educatec.com', 'Calle Sabiduría 350', ST_GeomFromText('POINT(-70.6167 -33.4010)', 4326)), -- Macul
('Contadores Asociados LimaSur', '76987453-9', 'info@limasurcontadores.com', 'Av. Contable 459', ST_GeomFromText('POINT(-70.7371 -33.5922)', 4326)), -- El Bosque
('Ministerio de Trámites Rápidos', '76452345-6', 'tramites@ministerio.gob', 'Plaza Central 1', ST_GeomFromText('POINT(-70.5745 -33.4501)', 4326)); -- La Reina


INSERT INTO ProductoServicio (nombre_producto, descripcion, categoria, precio_unitario, stock) VALUES
-- Empresarial (2)
('Recojo y entrega de contratos', 'Servicio puerta a puerta para contratos laborales', 'Empresarial', 2000.00, 150), -- 1
('Envío de facturas físicas', 'Facturación impresa para clientes', 'Empresarial', 2000.00, 140), -- 2

-- Doméstico (2)
('Distribución de recibos de servicios', 'Envío de recibos de luz, agua, teléfono', 'Doméstico', 1000.00, 250), -- 3
('Servicio doméstico express', 'Entrega urgente de documentos familiares', 'Doméstico', 2200.00, 80), -- 4

-- Civil (2)
('Trámite de partidas de nacimiento', 'Recojo y entrega de partidas registrales', 'Civil', 1800.00, 120), -- 5
('Recojo de documentos en entidades públicas', 'Gestión ante entidades estatales', 'Civil', 2800.00, 70), -- 6

-- Financiero (2)
('Envío de documentos bancarios', 'Trámite de cuentas, préstamos, seguros', 'Financiero', 2400.00, 130), -- 7
('Distribución de cheques', 'Envío seguro de cheques emitidos', 'Financiero', 3200.00, 90), -- 8

-- Educación (2)
('Entrega de documentos escolares', 'Reparto de libretas, actas y comunicados', 'Educación', 1400.00, 160), -- 9
('Servicio express educativo', 'Entrega urgente de documentos escolares', 'Educación', 2600.00, 60), -- 10

-- Salud (2)
('Envío de resultados médicos', 'Entrega segura de exámenes y diagnósticos', 'Salud', 2700.00, 100), -- 11
('Distribución de certificados de salud', 'Trámite ante ministerios de salud', 'Salud', 2100.00, 75), -- 12

-- Premium (2)
('Servicio de courier personalizado', 'Servicio premium con seguimiento', 'Premium', 4000.00, 30), -- 13
('Entrega VIP puerta a puerta', 'Courier exclusivo para clientes frecuentes', 'Premium', 4200.00, 25), -- 14

-- Documento Legal (2)
('Envío de documentos notariales', 'Entrega segura de documentos notariales a domicilio', 'Documento Legal', 2500.00, 100), -- 15
('Trámite de antecedentes penales', 'Entrega de documentos del Ministerio de Justicia', 'Documento Legal', 2200.00, 90), -- 16
 
-- Certificado Oficial (2)
('Entrega de certificados académicos', 'Distribución de certificados para estudiantes', 'Certificado Oficial', 1500.00, 200), --17
('Distribución de diplomas oficiales', 'Entrega de diplomas de instituciones educativas', 'Certificado Oficial', 1700.00, 180), -- 18

-- Judicial (2)
('Gestión de trámites judiciales', 'Recoger, presentar y devolver documentos', 'Judicial', 3800.00, 45), -- 19
('Servicio express legal', 'Entrega urgente de documentos judiciales', 'Judicial', 3500.00, 50); -- 20


INSERT INTO Repartidores (nombre_repartidor, rut, telefono, fecha_contratacion, activo, cantidad_entregas, ubicacion_actual) VALUES
('Carlos Medina', '75892345-1', '912300001', '2018-06-10', TRUE, 4, ST_GeomFromText('POINT(-70.6500 -33.5000)', 4326)), -- La Florida
('María Flores', '76983456-2', '912300002', '2018-02-15', TRUE, 6, ST_GeomFromText('POINT(-70.6770 -33.5861)', 4326)), -- San Ramón
('José Ramírez', '74876321-3', '912300003', '2018-11-03', TRUE, 2, ST_GeomFromText('POINT(-70.7064 -33.4573)', 4326)), -- Lo Espejo
('Diana Quispe', '73897542-4', '912300004', '2018-08-20', TRUE, 1, ST_GeomFromText('POINT(-70.5710 -33.4910)', 4326)), -- Peñalolén
('Andrés Castillo', '72839456-5', '912300005', '2018-04-01', TRUE, 3, ST_GeomFromText('POINT(-70.7600 -33.4560)', 4326)); -- Maipú


INSERT INTO Calificaciones (id_repartidor, puntuacion, comentario, fecha_calificacion) VALUES
-- Carlos Medina (4)
(1, 5, 'Entrega puntual y profesional.', '2020-08-16'),
(1, 4, 'Buen servicio, llegó un poco justo de tiempo.', '2021-07-11'),
(1, 5, 'Muy amable y cuidadoso con el documento.', '2022-11-05'),
(1, 4, 'Todo bien, sin inconvenientes.', '2023-02-19'),

-- María Flores (6)
(2, 5, 'Excelente atención, muy rápida.', '2024-01-11'),
(2, 5, 'Súper profesional y cordial.', '2019-10-22'),
(2, 4, 'Un pequeño retraso pero buen trato.', '2025-04-02'),
(2, 5, 'Entrega rápida y segura.', '2025-03-15'),
(2, 5, 'Todo perfecto, muchas gracias.', '2021-09-30'),
(2, 4, 'Buen servicio general.', '2022-07-01'),

-- José Ramírez (2)
(3, 4, 'Servicio correcto, cumplió.', '2023-07-22'),
(3, 3, 'Se demoró un poco más de lo esperado.', '2024-10-12'),

-- Diana Quispe (1)
(4, 5, 'Excelente actitud y entrega rápida.', '2019-04-04'),

-- Andrés Castillo (3)
(5, 5, 'Buen trato y puntual.', '2021-03-28'),
(5, 4, 'Buen servicio, todo correcto.', '2022-12-08'),
(5, 5, 'Muy profesional y confiable.', '2023-11-19');

INSERT INTO Medios_de_pago (metodo_pago, fecha_pago, monto_total) VALUES
('Tarjeta de crédito', '2025-05-14', 49200),
('Transferencia bancaria', '2020-08-14', 49200),
('Yape', '2021-07-09', 49200),
('Efectivo', '2022-11-05', 19000),
('Plin', '2023-02-18', 2500), -- 5 Doc

-- CertificadoOficiales -- (repartidor con mas de 5 entregas)
('Tarjeta de débito', '2024-01-10', 2200), -- 6 Doc
('Yape', '2024-05-09', 22000),
('Efectivo', '2020-12-01', 16200),
('Transferencia bancaria', '2019-10-22', 62400),
('Plin', '2025-04-01', 16000),
('Tarjeta de crédito', '2025-03-14', 12600),
('Efectivo', '2021-09-29', 48600),
('Tarjeta de débito', '2022-06-30', 44000),

('Yape', '2023-07-22', 44000),
('Transferencia bancaria', '2024-10-12', 44000),

('Efectivo', '2019-04-03', 44000),

('Tarjeta de crédito', '2020-05-16', 44000),
('Plin', '2021-03-28', 44000),
('Transferencia bancaria', '2022-08-08', 44000),
('Yape', '2023-11-19', 49200);


-- Por simplicidad se asociarán los primeros 20 clientes, productos, pagos y repartidores en orden
INSERT INTO Pedido (id_cliente, id_empresa, id_repartidor, id_pago, fecha_pedido, fecha_entrega, estado, urgente) VALUES
-- Pedido que diga Documento Legal debe estar en urgente TRUE

(1, 1, 1, 1, '2025-05-14', '2025-05-14', 'Pendiente', FALSE), -- Pendiente
(1, 2, 1, 2, '2020-08-14', '2020-08-16', 'Finalizado', FALSE),
(1, 3, 1, 3, '2021-07-09', '2021-07-11', 'Finalizado', FALSE),
(1, 4, 1, 4, '2022-11-04', '2022-11-05', 'Finalizado', FALSE),
(2, 5, 1, 5, '2023-02-18', '2023-02-19', 'Finalizado', TRUE), --DOC

(3, 1, 2, 6, '2024-01-10', '2024-01-11', 'Finalizado', TRUE), -- DOC
(4, 2, 2, 7, '2025-05-14', '2025-05-14', 'Pendiente', FALSE), -- Pendiente
(5, 3, 2, 8, '2020-12-01', '2020-12-02', 'Cancelado', FALSE),
(6, 4, 2, 9, '2019-10-22', '2019-10-22', 'Finalizado', FALSE),
(7, 5, 2, 10, '2025-04-01', '2025-04-02', 'Finalizado', FALSE),
(8, 1, 2, 11, '2025-03-14', '2025-03-15', 'Finalizado', FALSE),
(9, 2, 2, 12, '2021-09-29', '2021-09-30', 'Finalizado', FALSE),
(10, 3, 2, 13, '2022-06-30', '2022-07-01', 'Finalizado', FALSE),

(2, 4, 3, 14, '2023-07-22', '2023-07-22', 'Finalizado', FALSE),
(3, 5, 3, 15, '2024-10-12', '2024-10-12', 'Finalizado', FALSE),

(4, 1, 4, 16, '2019-04-03', '2019-04-04', 'Finalizado', FALSE),

(5, 2, 5, 17, '2020-05-16', '2020-05-17', 'Cancelado', FALSE),
(6, 3, 5, 18, '2021-03-28', '2021-03-28', 'Finalizado', FALSE),
(7, 4, 5, 19, '2022-08-08', '2022-08-08', 'Finalizado', FALSE),
(8, 5, 5, 20, '2023-11-19', '2023-11-19', 'Finalizado', FALSE);

INSERT INTO Detalle_de_pedido (id_producto, id_pedido, cantidad, subtotal) VALUES
(4, 1, 16, 35200),
(6, 1, 5, 14000),
(4, 2, 16, 35200),
(6, 2, 5, 14000),
(4, 3, 16, 35200),
(6, 3, 5, 14000),

(19, 4, 5, 19000),
(15, 5, 1, 2500),
(16, 6, 1, 2200),

(1, 7, 5, 10000),
(2, 7, 16, 6000),
(3, 7, 5, 6000),
(5, 8, 9, 16200),
(7, 9, 26, 62400),
(8, 10, 5, 16000),
(9, 11, 9, 12600),
(11, 12, 18, 48600),
(13, 13, 11, 44000),

(13, 14, 11, 44000),
(13, 15, 11, 44000),

(13, 16, 11, 44000),

(13, 17, 11, 44000),
(13, 18, 11, 44000),
(13, 19, 11, 44000),

(4, 20, 16, 35200),
(6, 20, 5, 14000);

-- Actualizar LineString a cada pedido
UPDATE Pedido p
SET ruta_estimada = ST_MakeLine(
    ARRAY[
        e.ubicacion,
        c.ubicacion
    ])
FROM Empresas_Asociadas e, Cliente c
WHERE p.id_empresa = e.id_empresa AND p.id_cliente = c.id_cliente;


