package com.hospital.repository;

import com.hospital.model.Cita;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
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
}