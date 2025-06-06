-- Conectarse a la base
\connect control2_grupo3;

-- Activar la extensión PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

SET client_encoding = 'UTF8';

INSERT INTO usuario (username, correo, contrasena, ubicacion) VALUES
('usuario1', 'user1@example.com', 'pass1', ST_SetSRID(ST_MakePoint(-70.6500, -33.4400), 4326)),
('usuario2', 'user2@example.cl', 'pass2', ST_SetSRID(ST_MakePoint(-70.6100, -33.5000), 4326)),
('usuario3', 'user3@example.cl', 'pass3', ST_SetSRID(ST_MakePoint(-70.5800, -33.4200), 4326)),
('usuario4', 'user4@example.com', 'pass4', ST_SetSRID(ST_MakePoint(-70.6000, -33.4600), 4326)),
('usuario5', 'user5@example.com', 'pass5', ST_SetSRID(ST_MakePoint(-70.6300, -33.4700), 4326)),
('usuario6', 'user6@example.com', 'pass6', ST_SetSRID(ST_MakePoint(-70.6400, -33.4500), 4326)),
('usuario7', 'user7@example.com', 'pass7', ST_SetSRID(ST_MakePoint(-70.6200, -33.4800), 4326)),
('usuario8', 'user8@example.com', 'pass8', ST_SetSRID(ST_MakePoint(-70.5900, -33.4300), 4326)),
('usuario9', 'user9@example.com', 'pass9', ST_SetSRID(ST_MakePoint(-70.5700, -33.4100), 4326)),
('usuario10', 'user10@example.com', 'pass10', ST_SetSRID(ST_MakePoint(-70.6050, -33.4550), 4326)),
('usuario11', 'user11@example.com', 'pass11', ST_SetSRID(ST_MakePoint(-70.6350, -33.4420), 4326)),
('usuario12', 'user12@example.com', 'pass12', ST_SetSRID(ST_MakePoint(-70.6200, -33.4440), 4326)),
('usuario13', 'user13@example.cl', 'pass13', ST_SetSRID(ST_MakePoint(-70.6110, -33.4570), 4326)),
('usuario14', 'user14@example.cl', 'pass14', ST_SetSRID(ST_MakePoint(-70.6030, -33.4490), 4326)),
('usuario15', 'user15@example.cl', 'pass15', ST_SetSRID(ST_MakePoint(-70.5990, -33.4700), 4326)),
('usuario16', 'user16@example.cl', 'pass16', ST_SetSRID(ST_MakePoint(-70.6420, -33.4300), 4326)),
('usuario17', 'user17@example.cl', 'pass17', ST_SetSRID(ST_MakePoint(-70.6300, -33.4230), 4326)),
('usuario18', 'user18@example.cl', 'pass18', ST_SetSRID(ST_MakePoint(-70.6120, -33.4330), 4326)),
('usuario19', 'user19@example.cl', 'pass19', ST_SetSRID(ST_MakePoint(-70.5950, -33.4470), 4326)),
('usuario20', 'user20@example.cl', 'pass20', ST_SetSRID(ST_MakePoint(-70.6060, -33.4600), 4326));


INSERT INTO sector (nombre, descripcion, ubicacion) VALUES
('Sector 1', 'Zona en la Alameda',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6500 -33.4560, -70.6490 -33.4560, -70.6490 -33.4550, -70.6500 -33.4550, -70.6500 -33.4560))'), 4326)),
('Sector 2', 'Parque en Ñuñoa',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6105 -33.4545, -70.6095 -33.4545, -70.6095 -33.4535, -70.6105 -33.4535, -70.6105 -33.4545))'), 4326)),
('Sector 3', 'Área residencial',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.5805 -33.4205, -70.5795 -33.4205, -70.5795 -33.4195, -70.5805 -33.4195, -70.5805 -33.4205))'), 4326)),
('Sector 4', 'Plazoleta en Providencia',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6055 -33.4555, -70.6045 -33.4555, -70.6045 -33.4545, -70.6055 -33.4545, -70.6055 -33.4555))'), 4326)),
('Sector 5', 'Área de juegos en La Florida',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.5805 -33.5195, -70.5795 -33.5195, -70.5795 -33.5185, -70.5805 -33.5185, -70.5805 -33.5195))'), 4326)),
('Sector 6', 'Vereda transitada en Macul',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6210 -33.4810, -70.6200 -33.4810, -70.6200 -33.4800, -70.6210 -33.4800, -70.6210 -33.4810))'), 4326)),
('Sector 7', 'Cancha en Recoleta',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6405 -33.4105, -70.6395 -33.4105, -70.6395 -33.4095, -70.6405 -33.4095, -70.6405 -33.4105))'), 4326)),
('Sector 8', 'Mini parque en Las Condes',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.5305 -33.4105, -70.5295 -33.4105, -70.5295 -33.4095, -70.5305 -33.4095, -70.5305 -33.4105))'), 4326)),
('Sector 9', 'Patio techado en San Miguel',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6405 -33.5005, -70.6395 -33.5005, -70.6395 -33.4995, -70.6405 -33.4995, -70.6405 -33.5005))'), 4326)),
('Sector 10', 'Zócalo municipal en Macul',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.5805 -33.4805, -70.5795 -33.4805, -70.5795 -33.4795, -70.5805 -33.4795, -70.5805 -33.4805))'), 4326)),
('Sector 11', 'Área de reciclaje en Peñalolén',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6360 -33.4430, -70.6350 -33.4430, -70.6350 -33.4420, -70.6360 -33.4420, -70.6360 -33.4430))'), 4326)),
('Sector 12', 'Plaza infantil en Independencia',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6205 -33.4445, -70.6195 -33.4445, -70.6195 -33.4435, -70.6205 -33.4435, -70.6205 -33.4445))'), 4326)),
('Sector 13', 'Bandejón central',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6115 -33.4575, -70.6105 -33.4575, -70.6105 -33.4565, -70.6115 -33.4565, -70.6115 -33.4575))'), 4326)),
('Sector 14', 'Vereda en San Joaquín',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6035 -33.4505, -70.6025 -33.4505, -70.6025 -33.4495, -70.6035 -33.4495, -70.6035 -33.4505))'), 4326)),
('Sector 15', 'Pasaje techado en La Reina',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.5995 -33.4715, -70.5985 -33.4715, -70.5985 -33.4705, -70.5995 -33.4705, -70.5995 -33.4715))'), 4326)),
('Sector 16', 'Estacionamiento vecinal',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6430 -33.4310, -70.6420 -33.4310, -70.6420 -33.4300, -70.6430 -33.4300, -70.6430 -33.4310))'), 4326)),
('Sector 17', 'Zona de carga',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6310 -33.4240, -70.6300 -33.4240, -70.6300 -33.4230, -70.6310 -33.4230, -70.6310 -33.4240))'), 4326)),
('Sector 18', 'Mirador urbano',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6130 -33.4340, -70.6120 -33.4340, -70.6120 -33.4330, -70.6130 -33.4330, -70.6130 -33.4340))'), 4326)),
('Sector 19', 'Callejón sin salida',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.5960 -33.4480, -70.5950 -33.4480, -70.5950 -33.4470, -70.5960 -33.4470, -70.5960 -33.4480))'), 4326)),
('Sector 20', 'Rincón verde en Pedro Aguirre Cerda',
 ST_SetSRID(ST_GeomFromText('POLYGON((-70.6070 -33.4610, -70.6060 -33.4610, -70.6060 -33.4600, -70.6070 -33.4600, -70.6070 -33.4610))'), 4326));


INSERT INTO tarea (usuario_id, sector_id, nombre, descripcion, fecha_termino, completada) VALUES
(1, 1, 'Limpieza sector 1', 'Recoger basura y escombros', '2025-06-01', FALSE),
(2, 2, 'Poda de árboles', 'Poda preventiva de ramas largas', '2025-06-02', TRUE),
(1, 3, 'Revisión alumbrado', 'Revisión de luminarias públicas', '2025-06-03', FALSE),
(4, 4, 'Pintura de bancas', 'Pintado de mobiliario urbano', '2025-06-04', TRUE),
(1, 5, 'Mantenimiento juegos', 'Reparar juegos infantiles', '2025-06-05', FALSE),
(6, 6, 'Reparación veredas', 'Veredas levantadas por raíces', '2025-06-06', FALSE),
(7, 7, 'Instalación cámaras', 'Cámaras de seguridad municipal', '2025-06-07', TRUE),
(8, 8, 'Desinfección plaza', 'Desinfección contra plagas', '2025-06-08', FALSE),
(1, 9, 'Fiscalización comercio', 'Control de comercio informal', '2025-06-09', FALSE),
(1, 10, 'Reparación señalética', 'Cambio de señales deterioradas', '2025-06-10', TRUE),
(12, 11, 'Recolección reciclaje', 'Clasificar y recoger residuos reciclables', '2025-06-11', FALSE),
(12, 12, 'Inspección de juegos', 'Verificar estado de juegos infantiles', '2025-06-12', TRUE),
(1, 13, 'Instalación de bancas nuevas', 'Colocar nuevo mobiliario urbano', '2025-06-13', FALSE),
(14, 14, 'Reparación de vereda', 'Nivelar adoquines sueltos', '2025-06-14', TRUE),
(15, 15, 'Mantenimiento pasaje techado', 'Limpieza e iluminación', '2025-06-15', FALSE),
(16, 16, 'Demarcación estacionamientos', 'Pintado de espacios para vehículos', '2025-06-16', FALSE),
(17, 17, 'Supervisión zona carga', 'Control del uso por camiones', '2025-06-17', TRUE),
(12, 18, 'Mejoras en mirador', 'Colocar barandas y paneles solares', '2025-06-18', FALSE),
(19, 19, 'Cierre callejón', 'Bloqueo de acceso indebido', '2025-06-19', FALSE),
(20, 20, 'Siembra de plantas', 'Plantación de arbustos decorativos', '2025-06-20', TRUE);


INSERT INTO notificacion (id_tarea, mensaje, leida) VALUES
(1, 'Se ha asignado la tarea de limpieza en sector 1', FALSE),
(2, 'Tarea de poda de árboles completada', TRUE),
(3, 'Pendiente revisión de luminarias', FALSE),
(4, 'Pintura de bancas finalizada', TRUE),
(5, 'Inicia mantenimiento de juegos infantiles', FALSE),
(6, 'Programada reparación de veredas', FALSE),
(7, 'Instalación de cámaras realizada', TRUE),
(8, 'Plaza lista para desinfección', FALSE),
(9, 'Fiscalización de comercio pendiente', FALSE),
(10, 'Señalética reemplazada con éxito', TRUE),
(11, 'Reciclaje asignado en sector 11', FALSE),
(12, 'Inspección completada de juegos en sector 12', TRUE),
(13, 'Bancas nuevas en proceso de instalación', FALSE),
(14, 'Vereda reparada satisfactoriamente', TRUE),
(15, 'Mantenimiento programado del pasaje', FALSE),
(16, 'Iniciada demarcación de estacionamientos', FALSE),
(17, 'Zona de carga bajo supervisión', TRUE),
(18, 'Mejoras de mirador en curso', FALSE),
(19, 'Cierre de callejón programado', FALSE),
(20, 'Plantación realizada con éxito', TRUE);

