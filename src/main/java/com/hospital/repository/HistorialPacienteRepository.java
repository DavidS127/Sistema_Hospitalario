package com.hospital.repository;
 
import com.hospital.model.dto.HistorialPaciente;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
/**
 * REPOSITORY: HistorialPacienteRepository
 *
 * PROPÓSITO:
 *   Consulta la vista vw_historial_paciente para obtener el historial
 *   clínico completo. La vista resuelve los 6 JOINs internamente.
 */
@Repository
public class HistorialPacienteRepository {
 
    private final NamedParameterJdbcTemplate namedJdbc;
 
    public HistorialPacienteRepository(NamedParameterJdbcTemplate namedJdbc) {
        this.namedJdbc = namedJdbc;
    }
 
    private final RowMapper<HistorialPaciente> mapper = (rs, rowNum) -> {
        HistorialPaciente h = new HistorialPaciente();
        h.setIdPaciente(rs.getInt("id_paciente"));
        h.setPaciente(rs.getString("paciente"));
        if (rs.getDate("fecha_nacimiento") != null)
            h.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        h.setEps(rs.getString("eps"));
        h.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
        h.setEstadoHistoria(rs.getString("estado_historia"));
        if (rs.getTimestamp("fecha_apertura_historia") != null)
            h.setFechaAperturaHistoria(rs.getTimestamp("fecha_apertura_historia").toLocalDateTime());
        h.setIdEvento(rs.getInt("id_evento"));
        if (rs.getDate("fecha_evento") != null)
            h.setFechaEvento(rs.getDate("fecha_evento").toLocalDate());
        if (rs.getTime("hora_evento") != null)
            h.setHoraEvento(rs.getTime("hora_evento").toLocalTime());
        h.setTipoEvento(rs.getString("tipo_evento"));
        h.setDescripcionEvento(rs.getString("descripcion_evento"));
        int idConsulta = rs.getInt("id_consulta");
        if (!rs.wasNull()) h.setIdConsulta(idConsulta);
        h.setMotivoConsulta(rs.getString("motivo_consulta"));
        h.setTipoConsulta(rs.getString("tipo_consulta"));
        h.setMedico(rs.getString("medico"));
        h.setEspecialidad(rs.getString("especialidad"));
        h.setDepartamento(rs.getString("departamento"));
        int idReceta = rs.getInt("id_receta");
        if (!rs.wasNull()) h.setIdReceta(idReceta);
        if (rs.getDate("fecha_receta") != null)
            h.setFechaReceta(rs.getDate("fecha_receta").toLocalDate());
        return h;
    };
 
    // Historial completo de un paciente
    public List<HistorialPaciente> findByPaciente(int idPaciente) {
        String sql = "SELECT * FROM vw_historial_paciente WHERE id_paciente = :idPaciente";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idPaciente", idPaciente);
        return namedJdbc.query(sql, params, mapper);
    }
}