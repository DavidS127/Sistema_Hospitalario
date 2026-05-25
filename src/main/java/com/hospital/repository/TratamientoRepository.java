package com.hospital.repository;

import com.hospital.model.Tratamiento;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TratamientoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public TratamientoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Tratamiento> tratamientoMapper = (rs, rowNum) -> {

        Tratamiento t = new Tratamiento();

        t.setId(rs.getInt("id"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setIdConsultaMedica(rs.getInt("id_consultamedica"));

        return t;
    };

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Tratamiento> findAll() {

        String sql = "SELECT * FROM tratamiento "
                   + "ORDER BY id";

        return jdbc.query(sql, tratamientoMapper);
    }

    // =========================
    // BUSCAR
    // =========================
    public Optional<Tratamiento> findById(int id) {

        String sql = "SELECT * FROM tratamiento "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Tratamiento> resultados = namedJdbc.query(
                sql,
                params,
                tratamientoMapper
        );

        return resultados.stream().findFirst();
    }

    // =========================
    // CREAR
    // =========================
    public Tratamiento save(Tratamiento tratamiento) {

        String sql = "INSERT INTO tratamiento ("
                + "descripcion, "
                + "id_consultamedica"
                + ") VALUES ("
                + ":descripcion, "
                + ":idConsultaMedica"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("descripcion", tratamiento.getDescripcion())
                .addValue("idConsultaMedica", tratamiento.getIdConsultaMedica());

        namedJdbc.update(sql, params);

        return tratamiento;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean update(int id, Tratamiento tratamiento) {

        String sql = "UPDATE tratamiento "
                + "SET descripcion = :descripcion, "
                + "id_consultamedica = :idConsultaMedica "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("descripcion", tratamiento.getDescripcion())
                .addValue("idConsultaMedica", tratamiento.getIdConsultaMedica())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean delete(int id) {

        String sql = "DELETE FROM tratamiento "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}