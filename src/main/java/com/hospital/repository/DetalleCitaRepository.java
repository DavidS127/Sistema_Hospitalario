package com.hospital.repository;
 
import com.hospital.model.dto.DetalleCita;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
/**
 * REPOSITORY: DetalleCitaRepository
 *
 * PROPÓSITO:
 *   Consulta la vista vw_detalle_cita para obtener citas con
 *   todos sus datos relacionados en una sola query.
 *   La vista ya resuelve los 4 JOINs internamente.
 */
@Repository
public class DetalleCitaRepository {
 
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
 
    public DetalleCitaRepository(JdbcTemplate jdbc,
                                  NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }
 
    /*
     * RowMapper: mapea cada columna de la vista a los campos del modelo.
     * Se reutiliza en todos los métodos del repositorio.
     */
    private final RowMapper<DetalleCita> mapper = (rs, rowNum) -> {
        DetalleCita d = new DetalleCita();
        d.setIdCita(rs.getInt("id_cita"));
        d.setFecha(rs.getDate("fecha").toLocalDate());
        d.setHora(rs.getTime("hora").toLocalTime());
        d.setEstado(rs.getString("estado"));
        if (rs.getTimestamp("fecha_creacion") != null)
            d.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        d.setIdPaciente(rs.getInt("id_paciente"));
        d.setPaciente(rs.getString("paciente"));
        d.setDocumentoPaciente(rs.getString("documento_paciente"));
        d.setTelefonoPaciente(rs.getString("telefono_paciente"));
        d.setEps(rs.getString("eps"));
        d.setIdMedico(rs.getInt("id_medico"));
        d.setMedico(rs.getString("medico"));
        d.setEspecialidad(rs.getString("especialidad"));
        d.setIdDepartamento(rs.getInt("id_departamento"));
        d.setDepartamento(rs.getString("departamento"));
        d.setUbicacionDepartamento(rs.getString("ubicacion_departamento"));
        return d;
    };
 
    // Todas las citas
    public List<DetalleCita> findAll() {
        return jdbc.query("SELECT * FROM vw_detalle_cita", mapper);
    }
 
    // Citas de hoy
    public List<DetalleCita> findHoy() {
        return jdbc.query(
            "SELECT * FROM vw_detalle_cita WHERE fecha = CURRENT_DATE ORDER BY hora",
            mapper
        );
    }
 
    // Citas por médico
    public List<DetalleCita> findByMedico(int idMedico) {
        String sql = "SELECT * FROM vw_detalle_cita WHERE id_medico = :idMedico ORDER BY fecha, hora";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idMedico", idMedico);
        return namedJdbc.query(sql, params, mapper);
    }
 
    // Citas por paciente
    public List<DetalleCita> findByPaciente(int idPaciente) {
        String sql = "SELECT * FROM vw_detalle_cita WHERE id_paciente = :idPaciente ORDER BY fecha DESC, hora";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idPaciente", idPaciente);
        return namedJdbc.query(sql, params, mapper);
    }
 
    // Citas por departamento
    public List<DetalleCita> findByDepartamento(int idDepartamento) {
        String sql = "SELECT * FROM vw_detalle_cita WHERE id_departamento = :idDepartamento ORDER BY fecha, hora";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idDepartamento", idDepartamento);
        return namedJdbc.query(sql, params, mapper);
    }
}