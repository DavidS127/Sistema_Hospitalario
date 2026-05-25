package com.hospital.repository;

import com.hospital.model.Procedimiento;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProcedimientoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public ProcedimientoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Procedimiento> procedimientoMapper = (rs, rowNum) -> {

        Procedimiento p = new Procedimiento();

        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setIdConsultaMedica(rs.getInt("id_consulta_medica"));

        return p;
    };

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Procedimiento> findAll() {

        String sql = "SELECT * FROM procedimiento "
                   + "ORDER BY id";

        return jdbc.query(sql, procedimientoMapper);
    }

    // =========================
    // BUSCAR
    // =========================
    public Optional<Procedimiento> findById(int id) {

        String sql = "SELECT * FROM procedimiento "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Procedimiento> resultados = namedJdbc.query(
                sql,
                params,
                procedimientoMapper
        );

        return resultados.stream().findFirst();
    }

    // =========================
    // CREAR
    // =========================
    public Procedimiento save(Procedimiento procedimiento) {

        String sql = "INSERT INTO procedimiento ("
                + "nombre, "
                + "id_consulta_medica"
                + ") VALUES ("
                + ":nombre, "
                + ":idConsultaMedica"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", procedimiento.getNombre())
                .addValue("idConsultaMedica", procedimiento.getIdConsultaMedica());

        namedJdbc.update(sql, params);

        return procedimiento;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean update(int id, Procedimiento procedimiento) {

        String sql = "UPDATE procedimiento "
                + "SET nombre = :nombre, "
                + "id_consulta_medica = :idConsultaMedica "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", procedimiento.getNombre())
                .addValue("idConsultaMedica", procedimiento.getIdConsultaMedica())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean delete(int id) {

        String sql = "DELETE FROM procedimiento "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}