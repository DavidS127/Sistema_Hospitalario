/*
 FUNCIÓN: fn_agendar_cita
 
 PROPÓSITO:
    Encapsula la lógica de negocio para agendar una cita médica de forma segura.
    Realiza todas las validaciones necesarias antes de insertar la cita, y retorna
    el ID de la cita creada si todo sale bien.
	
  CUÁNDO SE EJECUTA:
    Cuando un paciente o un administrativo agenda una nueva cita.
    Se llama desde CitaRepository en Spring Boot.
	
  RETORNA:
    INT - ID de la cita creada si todo es correcto.
    Lanza EXCEPTION si alguna validación falla (el backend captura el mensaje).
 */
 
CREATE OR REPLACE FUNCTION fn_agendar_cita(
    p_id_paciente     INT,
    p_id_medico       INT,
    p_id_departamento INT,
    p_fecha           DATE,
    p_hora            TIME
)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    v_id_cita         INT;
    v_depto_medico    INT;
BEGIN
--VALIDAR QUE EL PACIENTE EXISTE Y ESTÁ ACTIVO
    IF NOT EXISTS (
        SELECT 1 FROM paciente
        WHERE id = p_id_paciente AND estado = 'activo'
    ) THEN
        RAISE EXCEPTION 'El paciente con id % no existe o no está activo.', p_id_paciente;
    END IF;

 --VALIDAR QUE EL MÉDICO EXISTE Y ESTÁ ACTIVO
    IF NOT EXISTS (
        SELECT 1 FROM medico m
        JOIN empleado e ON m.id_empleado = e.id
        WHERE m.id_empleado = p_id_medico AND e.estado = 'activo'
    ) THEN
        RAISE EXCEPTION 'El médico con id % no existe o no está activo.', p_id_medico;
    END IF;

--VALIDAR QUE EL MÉDICO PERTENECE AL DEPARTAMENTO
    SELECT e.id_departamento INTO v_depto_medico
    FROM medico m
    JOIN empleado e ON m.id_empleado = e.id
    WHERE m.id_empleado = p_id_medico;
    IF v_depto_medico <> p_id_departamento THEN
        RAISE EXCEPTION
            'El médico % pertenece al departamento %, no al departamento %.',
            p_id_medico, v_depto_medico, p_id_departamento;
    END IF;
--VALIDAR DISPONIBILIDAD DEL MÉDICO
    IF EXISTS (
        SELECT 1 FROM cita
        WHERE id_medico = p_id_medico
          AND fecha      = p_fecha
          AND hora       = p_hora
          AND estado NOT IN ('cancelada', 'no_asistio')
    ) THEN
        RAISE EXCEPTION
            'El médico % ya tiene una cita programada el % a las %.',
            p_id_medico, p_fecha, p_hora;
    END IF;

--VALIDAR QUE EL PACIENTE NO TENGA CITA A ESA HORA
    IF EXISTS (
        SELECT 1 FROM cita
        WHERE id_paciente = p_id_paciente
          AND fecha        = p_fecha
          AND hora         = p_hora
          AND estado NOT IN ('cancelada', 'no_asistio')
    ) THEN
        RAISE EXCEPTION
            'El paciente % ya tiene una cita agendada el % a las %.',
            p_id_paciente, p_fecha, p_hora;
    END IF;

--VALIDAR QUE LA FECHA NO SEA EN EL PASADO
    IF p_fecha < CURRENT_DATE THEN
        RAISE EXCEPTION
            'No se puede agendar una cita en una fecha pasada (%).', p_fecha;
    END IF;

--INSERTAR LA CITA
    INSERT INTO cita (
        fecha,
        hora,
        estado,
        id_paciente,
        id_departamento,
        id_medico
    )
    VALUES (
        p_fecha,
        p_hora,
        'programada',
        p_id_paciente,
        p_id_departamento,
        p_id_medico
    )
    RETURNING id INTO v_id_cita;
	
    --RETORNO Se retorna el ID de la cita recién creada.
    RETURN v_id_cita;
END;
$$;