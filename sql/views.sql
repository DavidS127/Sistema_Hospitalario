/* 
 * VISTAS DEL SISTEMA HOSPITALARIO
 *
 * Se definen 3 vistas que simplifican consultas complejas con JOINs.
 * Una vez creadas, la API las consume como si fueran tablas simples.
 *
 * VISTA 1: vw_detalle_cita        → info completa de citas (paciente + médico + depto)
 * VISTA 2: vw_historial_paciente  → historial clínico completo por paciente
 * VISTA 3: vw_inventario_farmacia → estado del inventario con alertas de stock
 */

/* 
 * VISTA 1: vw_detalle_cita
 *
 * PROPÓSITO:
 *   Consolida en una sola consulta todos los datos relevantes de una cita:
 *   información del paciente, del médico, del departamento y el estado.
 *   Sin esta vista, el frontend necesitaría 4 JOINs para mostrar
 *   la agenda del día.
 *
 * TABLAS QUE UNE:
 *   cita → paciente, medico → empleado (como medico), departamento
 *
 * USO TÍPICO:
 *   - Agenda del día de un médico
 *   - Listado de citas de un paciente
 *   - Panel administrativo de citas por departamento
 */

CREATE OR REPLACE VIEW vw_detalle_cita AS
SELECT
    c.id                                        AS id_cita,
    c.fecha,
    c.hora,
    c.estado,
    c.fecha_creacion,

    -- Datos del paciente
    p.id                                        AS id_paciente,
    p.nombres   || ' ' || p.apellidos           AS paciente,
    p.tipo_documento || ' ' || p.numero_documento AS documento_paciente,
    p.telefono                                  AS telefono_paciente,
    p.eps,

    -- Datos del médico
    e.id                                        AS id_medico,
    e.nombres   || ' ' || e.apellidos           AS medico,
    med.especialidad,

    -- Datos del departamento
    d.id                                        AS id_departamento,
    d.nombre                                    AS departamento,
    d.ubicacion                                 AS ubicacion_departamento

FROM cita c
JOIN paciente     p   ON c.id_paciente     = p.id
JOIN medico       med ON c.id_medico       = med.id_empleado
JOIN empleado     e   ON med.id_empleado   = e.id
JOIN departamento d   ON c.id_departamento = d.id;

/* 
 * VISTA 2: vw_historial_paciente
 *
 * PROPÓSITO:
 *   Muestra el historial clínico completo de cada paciente: cada evento
 *   registrado con su consulta, el médico que atendió, el departamento,
 *   y si tiene receta asociada. Reemplaza una consulta de 6 JOINs.
 *
 * TABLAS QUE UNE:
 *   paciente → historia_clinica → evento → consulta_medica
 *           → medico → empleado, departamento, receta
 *
 * USO TÍPICO:
 *   - Ver el historial completo de un paciente filtrado por su ID
 *   - Reporte médico de evolución del paciente
 *   - Auditoría clínica
 */

CREATE OR REPLACE VIEW vw_historial_paciente AS
SELECT
    -- Datos del paciente
    p.id                                        AS id_paciente,
    p.nombres   || ' ' || p.apellidos           AS paciente,
    p.fecha_nacimiento,
    p.eps,

    -- Historia clínica
    hc.id                                       AS id_historia_clinica,
    hc.estado                                   AS estado_historia,
    hc.fecha_creacion                           AS fecha_apertura_historia,

    -- Evento
    ev.id                                       AS id_evento,
    ev.fecha                                    AS fecha_evento,
    ev.hora                                     AS hora_evento,
    ev.tipo                                     AS tipo_evento,
    ev.descripcion                              AS descripcion_evento,

    -- Consulta médica
    cm.id                                       AS id_consulta,
    cm.motivo_consulta,
    cm.tipo_consulta,

    -- Médico que atendió
    e.nombres   || ' ' || e.apellidos           AS medico,
    med.especialidad,

    -- Departamento
    d.nombre                                    AS departamento,

    -- Receta (puede ser NULL si la consulta no generó receta)
    r.id                                        AS id_receta,
    r.fecha                                     AS fecha_receta

FROM paciente         p
JOIN historia_clinica hc  ON hc.id_paciente       = p.id
JOIN evento           ev  ON ev.id_historiaclinica = hc.id
LEFT JOIN consulta_medica cm ON ev.id_consultamedica = cm.id
LEFT JOIN medico          med ON cm.id_medico        = med.id_empleado
LEFT JOIN empleado        e   ON med.id_empleado     = e.id
LEFT JOIN departamento    d   ON cm.id_departamento  = d.id
LEFT JOIN receta          r   ON r.id_consultamedica = cm.id
ORDER BY p.id, ev.fecha DESC, ev.hora DESC;

/* 
 * VISTA 3: vw_inventario_farmacia
 *
 * PROPÓSITO:
 *   Muestra el inventario actual de cada farmacia con el nombre del
 *   medicamento, su forma farmacéutica, stock disponible y una
 *   columna calculada de alerta ('CRITICO', 'BAJO', 'OK') para
 *   identificar rápidamente qué medicamentos necesitan reabastecimiento.
 *
 * TABLAS QUE UNE:
 *   almacena → farmacia, medicamento
 *
 * LÓGICA DE ALERTAS:
 *   stock = 0        → 'CRITICO'  (agotado)
 *   stock entre 1-5  → 'BAJO'     (casi agotado)
 *   stock > 5        → 'OK'       (suficiente)
 *
 * USO TÍPICO:
 *   - Panel de inventario del farmacéutico
 *   - Reporte de medicamentos críticos para reabastecimiento
 *   - Validación previa a dispensar una receta
 */

CREATE OR REPLACE VIEW vw_inventario_farmacia AS
SELECT
    -- Datos de la farmacia
    f.id                                        AS id_farmacia,
    f.nombre                                    AS farmacia,
    f.ubicacion                                 AS ubicacion_farmacia,
    f.estado                                    AS estado_farmacia,

    -- Datos del medicamento
    m.id                                        AS id_medicamento,
    m.nombre                                    AS medicamento,
    m.concentracion,
    m.forma_farmaceutica,
    m.via_administracion,

    -- Stock e indicador de alerta
    a.stock,
    CASE
        WHEN a.stock = 0        THEN 'CRITICO'
        WHEN a.stock BETWEEN 1 AND 5 THEN 'BAJO'
        ELSE 'OK'
    END                                         AS alerta_stock

FROM almacena    a
JOIN farmacia    f ON a.id_farmacia    = f.id
JOIN medicamento m ON a.id_medicamento = m.id
ORDER BY
    CASE
        WHEN a.stock = 0            THEN 1
        WHEN a.stock BETWEEN 1 AND 5 THEN 2
        ELSE 3
    END,
    f.nombre, m.nombre;


/* 
 * EJEMPLOS DE USO DIRECTO EN SQL
 */

-- Vista 1: citas del día de hoy
SELECT * FROM vw_detalle_cita
WHERE fecha = CURRENT_DATE
ORDER BY hora;

-- Vista 1: todas las citas de un médico específico
SELECT * FROM vw_detalle_cita
WHERE id_medico = 1
ORDER BY fecha, hora;

-- Vista 1: todas las citas de un paciente
SELECT * FROM vw_detalle_cita
WHERE id_paciente = 1;

-- Vista 2: historial completo de un paciente
SELECT * FROM vw_historial_paciente
WHERE id_paciente = 1;

-- Vista 3: todos los medicamentos críticos o bajos
SELECT * FROM vw_inventario_farmacia
WHERE alerta_stock IN ('CRITICO', 'BAJO');

-- Vista 3: inventario completo de una farmacia
SELECT * FROM vw_inventario_farmacia
WHERE id_farmacia = 1;