CREATE TABLE entidad_audit (
    id SERIAL PRIMARY KEY,
    tabla TEXT,
    operacion CHAR(1),
    usuario TEXT,
    ts TIMESTAMPTZ DEFAULT now(),
    dato_viejo JSONB,
    dato_nuevo JSONB
);

CREATE OR REPLACE FUNCTION fn_audit_entidad()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO entidad_audit (tabla, operacion, usuario, dato_viejo, dato_nuevo)
        VALUES (TG_TABLE_NAME, 'I', current_user, NULL, to_jsonb(NEW));
        RETURN NEW;

    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO entidad_audit (tabla, operacion, usuario, dato_viejo, dato_nuevo)
        VALUES (TG_TABLE_NAME, 'U', current_user, to_jsonb(OLD), to_jsonb(NEW));
        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO entidad_audit (tabla, operacion, usuario, dato_viejo, dato_nuevo)
        VALUES (TG_TABLE_NAME, 'D', current_user, to_jsonb(OLD), NULL);
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Historia clínica
CREATE TRIGGER trg_audit_historia
AFTER INSERT OR UPDATE OR DELETE
ON historia_clinica
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Consulta médica
CREATE TRIGGER trg_audit_consulta
AFTER INSERT OR UPDATE OR DELETE
ON consulta_medica
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Receta
CREATE TRIGGER trg_audit_receta
AFTER INSERT OR UPDATE OR DELETE
ON receta
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

-- Detalle receta
CREATE TRIGGER trg_audit_detalle
AFTER INSERT OR UPDATE OR DELETE
ON detalle_receta
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

--Paciente
CREATE TRIGGER trg_audit_detalle
AFTER INSERT OR UPDATE OR DELETE
ON paciente
FOR EACH ROW
EXECUTE FUNCTION fn_audit_entidad();

--Triggers punto 2
--Regla (Consulta Médica)
--Un médico solo puede atender consultas en su mismo departamento

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

CREATE TRIGGER trg_validar_consulta
BEFORE INSERT OR UPDATE
ON consulta_medica
FOR EACH ROW
EXECUTE FUNCTION fn_validar_consulta();