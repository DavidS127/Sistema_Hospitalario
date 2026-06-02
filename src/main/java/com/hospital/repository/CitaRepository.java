package com.hospital.repository;

import com.hospital.model.Cita;
import com.hospital.model.dto.ReprogramarCitaRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CitaRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public CitaRepository(JdbcTemplate jdbc,
                          NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    private final RowMapper<Cita> citaMapper = (rs, rowNum) -> {

        Cita c = new Cita();

        c.setId(rs.getInt("id"));
        c.setFecha(rs.getDate("fecha").toLocalDate());
        c.setHora(rs.getTime("hora").toLocalTime());

        c.setEstado(rs.getString("estado"));

        if(rs.getTimestamp("fecha_creacion") != null) {
            c.setFechaCreacion(
                rs.getTimestamp("fecha_creacion").toLocalDateTime()
            );
        }

        c.setIdPaciente(rs.getInt("id_paciente"));
        c.setIdDepartamento(rs.getInt("id_departamento"));
        c.setIdMedico(rs.getInt("id_medico"));

        int idConsulta = rs.getInt("id_consultamedica");

        if(!rs.wasNull()) {
            c.setIdConsultamedica(idConsulta);
        }

        return c;
    };

    // Listar todas las citas
    public List<Cita> findAll() {

        String sql = "SELECT * FROM cita ORDER BY id";

        return jdbc.query(sql, citaMapper);
    }

    // Buscar cita por ID
    public Optional<Cita> findById(int id) {

        String sql = "SELECT * FROM cita WHERE id = :id";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("id", id);

        List<Cita> lista =
            namedJdbc.query(sql, params, citaMapper);

        return lista.stream().findFirst();
    }

    // Crear cita
    public Cita save(Cita cita) {

        String sql =
            "INSERT INTO cita " +
            "(fecha, hora, estado, id_paciente, " +
            "id_departamento, id_medico, id_consultamedica) " +
            "VALUES " +
            "(:fecha, :hora, :estado, :idPaciente, " +
            ":idDepartamento, :idMedico, :idConsultamedica)";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("fecha", cita.getFecha())
                .addValue("hora", cita.getHora())
                .addValue("estado", cita.getEstado())
                .addValue("idPaciente", cita.getIdPaciente())
                .addValue("idDepartamento", cita.getIdDepartamento())
                .addValue("idMedico", cita.getIdMedico())
                .addValue("idConsultamedica", cita.getIdConsultamedica());

        namedJdbc.update(sql, params);

        return cita;
    }

    // Actualizar cita
    public boolean update(int id, Cita cita) {

        String sql =
            "UPDATE cita " +
            "SET fecha = :fecha, " +
            "hora = :hora, " +
            "estado = :estado, " +
            "id_paciente = :idPaciente, " +
            "id_departamento = :idDepartamento, " +
            "id_medico = :idMedico, " +
            "id_consultamedica = :idConsultamedica " +
            "WHERE id = :id";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("fecha", cita.getFecha())
                .addValue("hora", cita.getHora())
                .addValue("estado", cita.getEstado())
                .addValue("idPaciente", cita.getIdPaciente())
                .addValue("idDepartamento", cita.getIdDepartamento())
                .addValue("idMedico", cita.getIdMedico())
                .addValue("idConsultamedica", cita.getIdConsultamedica())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // Eliminar cita (marcar como cancelada)
    public boolean delete(int id) {

        String sql =
            "UPDATE cita" 
            +" SET estado = 'cancelada'"
            +" WHERE id = :id";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // ─────────────────────────────────────────────────────────────
    // FUNCIÓN DE NEGOCIO: Agendar cita con validaciones
    //
    // PROPÓSITO:
    //   Llama a fn_agendar_cita en PostgreSQL, que valida todas las
    //   reglas de negocio (médico activo, paciente activo, disponibilidad,
    //   departamento correcto, fecha no pasada) y crea la cita.
    //
    // POR QUÉ queryForObject y no update:
    //   La función PostgreSQL se invoca con SELECT y retorna el ID
    //   de la cita creada. queryForObject ejecuta el SELECT y mapea
    //   el único valor retornado a un Integer.
    //
    // MANEJO DE ERRORES:
    //   Si fn_agendar_cita lanza un RAISE EXCEPTION, el driver JDBC
    //   lo convierte en una DataAccessException. El controller la
    //   captura y extrae el mensaje legible para retornarlo al cliente.
    // ─────────────────────────────────────────────────────────────
    public int agendarCita(Cita cita) {
 
        String sql =
            "SELECT fn_agendar_cita(" +
            "  :idPaciente, " +
            "  :idMedico, " +
            "  :idDepartamento, " +
            "  :fecha, " +
            "  :hora" +
            ")";
 
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idPaciente",     cita.getIdPaciente())
                .addValue("idMedico",       cita.getIdMedico())
                .addValue("idDepartamento", cita.getIdDepartamento())
                .addValue("fecha",          cita.getFecha())
                .addValue("hora",           cita.getHora());
 
        // queryForObject ejecuta el SELECT fn_... y retorna el INT que devuelve la función
        return namedJdbc.queryForObject(sql, params, Integer.class);
    }

    public Map<String, Object> reprogramarCita(ReprogramarCitaRequest request) {
    
        String bloqueDo = String.format("""
            DO $$
            DECLARE
                v_id_cita_original  INT  := %d;
                v_nueva_fecha       DATE := '%s';
                v_nueva_hora        TIME := '%s';
                v_id_paciente       INT;
                v_id_medico         INT;
                v_id_departamento   INT;
                v_estado_actual     TEXT;
                v_fecha_original    DATE;
                v_hora_original     TIME;
                v_id_nueva_cita     INT;
                v_nombre_paciente   TEXT;
                v_nombre_medico     TEXT;
            BEGIN
                -- PASO 1: Validar cita original
                SELECT id_paciente, id_medico, id_departamento, estado, fecha, hora
                INTO v_id_paciente, v_id_medico, v_id_departamento, v_estado_actual, v_fecha_original, v_hora_original
                FROM cita WHERE id = v_id_cita_original;
    
                IF NOT FOUND THEN
                    RAISE EXCEPTION 'La cita %% no existe.', v_id_cita_original;
                END IF;
    
                IF v_estado_actual NOT IN ('programada', 'confirmada') THEN
                    RAISE EXCEPTION
                        'La cita %% no puede reprogramarse porque está en estado "%%".',
                        v_id_cita_original, v_estado_actual;
                END IF;
    
                SELECT p.nombres || ' ' || p.apellidos INTO v_nombre_paciente
                FROM paciente p WHERE p.id = v_id_paciente;
    
                SELECT e.nombres || ' ' || e.apellidos INTO v_nombre_medico
                FROM empleado e WHERE e.id = v_id_medico;
    
                -- PASO 2: Validar nueva fecha
                IF v_nueva_fecha < CURRENT_DATE THEN
                    RAISE EXCEPTION 'La nueva fecha (%%) no puede ser en el pasado.', v_nueva_fecha;
                END IF;
    
                IF v_nueva_fecha = CURRENT_DATE AND v_nueva_hora <= CURRENT_TIME THEN
                    RAISE EXCEPTION 'La nueva hora (%%) ya pasó para el día de hoy.', v_nueva_hora;
                END IF;
    
                IF v_nueva_fecha = v_fecha_original AND v_nueva_hora = v_hora_original THEN
                    RAISE EXCEPTION 'La nueva fecha y hora son iguales a la cita original.';
                END IF;
    
                -- PASO 3: Validar disponibilidad del médico
                IF EXISTS (
                    SELECT 1 FROM cita
                    WHERE id_medico = v_id_medico AND fecha = v_nueva_fecha AND hora = v_nueva_hora
                    AND estado NOT IN ('cancelada', 'no_asistio') AND id <> v_id_cita_original
                ) THEN
                    RAISE EXCEPTION 'El médico %% ya tiene una cita el %% a las %%.', v_nombre_medico, v_nueva_fecha, v_nueva_hora;
                END IF;
    
                -- PASO 4: Validar disponibilidad del paciente
                IF EXISTS (
                    SELECT 1 FROM cita
                    WHERE id_paciente = v_id_paciente AND fecha = v_nueva_fecha AND hora = v_nueva_hora
                    AND estado NOT IN ('cancelada', 'no_asistio') AND id <> v_id_cita_original
                ) THEN
                    RAISE EXCEPTION 'El paciente %% ya tiene una cita el %% a las %%.', v_nombre_paciente, v_nueva_fecha, v_nueva_hora;
                END IF;
    
                -- PASO 5: Cancelar cita original
                UPDATE cita SET estado = 'cancelada' WHERE id = v_id_cita_original;
    
                -- PASO 6: Crear nueva cita
                INSERT INTO cita (fecha, hora, estado, id_paciente, id_departamento, id_medico)
                VALUES (v_nueva_fecha, v_nueva_hora, 'programada', v_id_paciente, v_id_departamento, v_id_medico)
                RETURNING id INTO v_id_nueva_cita;
    
                -- PASO 7: Evento de auditoría
                INSERT INTO evento (fecha, hora, tipo, descripcion, id_historiaclinica, id_departamento, id_medico)
                SELECT CURRENT_DATE, CURRENT_TIME, 'Reprogramación',
                    FORMAT('Cita %%s del %%s a las %%s reprogramada al %%s a las %%s. Nueva cita: %%s.',
                        v_id_cita_original, v_fecha_original, v_hora_original,
                        v_nueva_fecha, v_nueva_hora, v_id_nueva_cita),
                    hc.id, v_id_departamento, v_id_medico
                FROM historia_clinica hc
                WHERE hc.id_paciente = v_id_paciente AND hc.estado = 'activa'
                LIMIT 1;
    
            EXCEPTION
                WHEN OTHERS THEN RAISE;
            END;
            $$
            """,
            request.getIdCitaOriginal(),
            request.getNuevaFecha().toString(),   // "2026-07-10"
            request.getNuevaHora().toString()     // "10:00:00"
        );
    
        namedJdbc.getJdbcTemplate().execute(bloqueDo);
    
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("mensaje",         "Cita reprogramada correctamente.");
        resultado.put("idCitaOriginal",  request.getIdCitaOriginal());
        resultado.put("nuevaFecha",      request.getNuevaFecha().toString());
        resultado.put("nuevaHora",       request.getNuevaHora().toString());
        return resultado;
    }
}