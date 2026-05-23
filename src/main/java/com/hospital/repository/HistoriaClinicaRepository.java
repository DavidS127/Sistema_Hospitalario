package com.hospital.repository;

import com.hospital.model.HistoriaClinica;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HistoriaClinicaRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public HistoriaClinicaRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<HistoriaClinica> historiaMapper = (rs, rowNum) -> {

        HistoriaClinica h = new HistoriaClinica();

        h.setId(rs.getInt("id"));
        h.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        h.setEstado(rs.getString("estado"));
        h.setIdPaciente(rs.getInt("id_paciente"));

        return h;
    };


    // Listar todas las historias clínicas
    public List<HistoriaClinica> findAll() {

        String sql = "SELECT * FROM historia_clinica ORDER BY id";

        return jdbc.query(sql, historiaMapper);
    }

    // Buscar historia clínica por ID
    public Optional<HistoriaClinica> findById(int id) {

        String sql = "SELECT * FROM historia_clinica WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<HistoriaClinica> historias = namedJdbc.query(
                sql,
                params,
                historiaMapper
        );

        return historias.stream().findFirst();
    }

    // Crear nueva historia clínica
    public HistoriaClinica save(HistoriaClinica historia) {

        String sql = "INSERT INTO historia_clinica ("
                + "estado, "
                + "id_paciente"
                + ") VALUES ("
                + ":estado, "
                + ":idPaciente"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("estado", historia.getEstado())
                .addValue("idPaciente", historia.getIdPaciente());

        namedJdbc.update(sql, params);

        return historia;
    }

    // Actualizar historia clínica existente
    public boolean update(int id, HistoriaClinica historia) {

        String sql = "UPDATE historia_clinica "
                + "SET estado = :estado, "
                + "    id_paciente = :idPaciente "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("estado", historia.getEstado())
                .addValue("idPaciente", historia.getIdPaciente())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // Cambiar estado a "archivada" para eliminar historia clínica (Eliminar)
    public boolean delete(int id) {

        String sql = "UPDATE historia_clinica "
                   + "SET estado = 'archivada' "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}