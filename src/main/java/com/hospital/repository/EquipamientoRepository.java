package com.hospital.repository;

import com.hospital.model.Equipamiento;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EquipamientoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public EquipamientoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Equipamiento> equipamientoMapper = (rs, rowNum) -> {

        Equipamiento e = new Equipamiento();

        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setEstado(rs.getString("estado"));
        e.setIdDepartamento(rs.getInt("id_departamento"));

        return e;
    };

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Equipamiento> findAll() {

        String sql = "SELECT * FROM equipamiento ORDER BY id";

        return jdbc.query(sql, equipamientoMapper);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public Optional<Equipamiento> findById(int id) {

        String sql = "SELECT * FROM equipamiento WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Equipamiento> equipos = namedJdbc.query(
                sql,
                params,
                equipamientoMapper
        );

        return equipos.stream().findFirst();
    }

    // =========================
    // CREAR
    // =========================
    public Equipamiento save(Equipamiento equipamiento) {

        String sql = "INSERT INTO equipamiento ("
                + "nombre, "
                + "estado, "
                + "id_departamento"
                + ") VALUES ("
                + ":nombre, "
                + ":estado, "
                + ":idDepartamento"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", equipamiento.getNombre())
                .addValue("estado", equipamiento.getEstado())
                .addValue("idDepartamento", equipamiento.getIdDepartamento());

        namedJdbc.update(sql, params);

        return equipamiento;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean update(int id, Equipamiento equipamiento) {

        String sql = "UPDATE equipamiento "
                + "SET nombre = :nombre, "
                + "    estado = :estado, "
                + "    id_departamento = :idDepartamento "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", equipamiento.getNombre())
                .addValue("estado", equipamiento.getEstado())
                .addValue("idDepartamento", equipamiento.getIdDepartamento())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // =========================
    // CAMBIAR ESTADO
    // =========================
    public boolean delete(int id) {

        String sql = "UPDATE equipamiento "
                   + "SET estado = 'fuera_servicio' "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}