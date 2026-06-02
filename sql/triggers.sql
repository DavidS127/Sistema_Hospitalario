-- TABLA DE AUDITORÍA
-- Registra todos los cambios (INSERT, UPDATE, DELETE) realizados
-- sobre las tablas críticas del sistema. Cada fila representa
-- un evento de modificación con el estado anterior y posterior
-- del registro afectado en formato JSON.

CREATE TABLE entidad_audit (
    id SERIAL PRIMARY KEY,
    tabla TEXT,
    operacion CHAR(1),
    usuario TEXT,
    ts TIMESTAMPTZ DEFAULT now(),
    dato_viejo JSONB,
    dato_nuevo JSONB
);

-- FUNCIÓN DE AUDITORÍA GENÉRICA: fn_audit_entidad
--
-- Propósito:
--   Registrar automáticamente en entidad_audit cualquier
--   INSERT, UPDATE o DELETE ejecutado sobre la tabla que
--   dispara el trigger.
--
-- Cuándo se ejecuta:
--   Es invocada por los triggers trg_audit_* definidos más
--   abajo. Se ejecuta AFTER (después) de cada operación DML,
--   una vez por cada fila afectada (FOR EACH ROW).

CREATE OR REPLACE FUNCTION fn_audit_entidad()
RETURNS TRIGGER AS $$
BEGIN
    -- Bloque INSERT: se activa cuando se crea un nuevo registro.
    IF TG_OP = 'INSERT' THEN
        INSERT INTO entidad_audit (tabla, operacion, usuario, dato_viejo, dato_nuevo)
        VALUES (TG_TABLE_NAME, 'I', current_user, NULL, to_jsonb(NEW));
        RETURN NEW;

    -- Bloque UPDATE: se activa cuando se modifica un registro existente.
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO entidad_audit (tabla, operacion, usuario, dato_viejo, dato_nuevo)
        VALUES (TG_TABLE_NAME, 'U', current_user, to_jsonb(OLD), to_jsonb(NEW));
        RETURN NEW;

    -- Bloque DELETE: se activa cuando se elimina un registro.
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO entidad_audit (tabla, operacion, usuario, dato_viejo, dato_nuevo)
        VALUES (TG_TABLE_NAME, 'D', current_user, to_jsonb(OLD), NULL);
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Audita creaciones, modificaciones y cierres de historias clínicas
CREATE TRIGGER trg_audit_historia
AFTER INSERT OR UPDATE OR DELETE
ON historia_clinica
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Audita el ciclo de vida de las consultas médicas
CREATE TRIGGER trg_audit_consulta
AFTER INSERT OR UPDATE OR DELETE
ON consulta_medica
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Audita la emisión y modificación de recetas
CREATE TRIGGER trg_audit_receta
AFTER INSERT OR UPDATE OR DELETE
ON receta
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Audita los medicamentos agregados, modificados o eliminados de una receta
CREATE TRIGGER trg_audit_detalle
AFTER INSERT OR UPDATE OR DELETE
ON detalle_receta
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Audita el registro, actualización o baja de pacientes
CREATE TRIGGER trg_audit_detalle
AFTER INSERT OR UPDATE OR DELETE
ON paciente
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- FUNCIÓN DE VALIDACIÓN: fn_validar_consulta
--
-- Propósito:
--   Garantizar que un médico solo pueda atender consultas
--   pertenecientes al mismo departamento al que está asignado.
--   Esto evita inconsistencias organizativas y errores de
--   asignación en el sistema.
--
-- Cuándo se ejecuta:
--   Es invocada por trg_validar_consulta BEFORE INSERT OR UPDATE
--   sobre consulta_medica. Al ejecutarse antes de la operación,
--   puede cancelarla lanzando una excepción si no se cumple
--   la regla de negocio.

CREATE OR REPLACE FUNCTION fn_validar_consulta()
RETURNS TRIGGER AS $$
DECLARE
    depto_medico INT;
BEGIN
    -- Obtener el departamento del médico (desde empleado)
    SELECT e.id_departamento
    INTO depto_medico
    FROM medico m
    JOIN empleado e ON m.id_empleado = e.id
    WHERE m.id_empleado = NEW.id_medico;

    -- Validar que coincida con el de la consulta
    IF depto_medico IS NULL THEN
        RAISE EXCEPTION 'Error: el médico no existe';
    END IF;

    IF depto_medico <> NEW.id_departamento THEN
        RAISE EXCEPTION 
        'Error: el médico pertenece al departamento %, pero la consulta es del %',
        depto_medico, NEW.id_departamento;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TRIGGER DE VALIDACIÓN: trg_validar_consulta
--
-- Se dispara BEFORE INSERT OR UPDATE sobre consulta_medica.
-- Al ejecutarse antes de escribir en la tabla, puede bloquear
-- la operación si fn_validar_consulta lanza una excepción,
-- asegurando que nunca se persista una consulta inválida.

CREATE TRIGGER trg_validar_consulta
BEFORE INSERT OR UPDATE
ON consulta_medica
FOR EACH ROW
EXECUTE FUNCTION fn_validar_consulta();