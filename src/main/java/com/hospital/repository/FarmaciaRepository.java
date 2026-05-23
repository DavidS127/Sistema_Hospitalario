package com.hospital.repository;

import com.hospital.model.Farmacia;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FarmaciaRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public FarmaciaRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Farmacia> farmaciaMapper = (rs, rowNum) -> {

        Farmacia f = new Farmacia();

        f.setId(rs.getInt("id"));
        f.setNombre(rs.getString("nombre"));
        f.setEstado(rs.getString("estado"));
        f.setUbicacion(rs.getString("ubicacion"));
        f.setTelefono(rs.getString("telefono"));

        return f;
    };

    // =========================
    // LISTAR ACTIVAS
    // =========================
    public List<Farmacia> findAll() {

        String sql = "SELECT * FROM farmacia "
                   + "WHERE estado = 'activa' "
                   + "ORDER BY id";

        return jdbc.query(sql, farmaciaMapper);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public Optional<Farmacia> findById(int id) {

        String sql = "SELECT * FROM farmacia WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Farmacia> farmacias = namedJdbc.query(
                sql,
                params,
                farmaciaMapper
        );

        return farmacias.stream().findFirst();
    }

    // =========================
    // CREAR
    // =========================
    public Farmacia save(Farmacia farmacia) {

        String sql = "INSERT INTO farmacia ("
                + "nombre, "
                + "estado, "
                + "ubicacion, "
                + "telefono"
                + ") VALUES ("
                + ":nombre, "
                + ":estado, "
                + ":ubicacion, "
                + ":telefono"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", farmacia.getNombre())
                .addValue("estado", farmacia.getEstado())
                .addValue("ubicacion", farmacia.getUbicacion())
                .addValue("telefono", farmacia.getTelefono());

        namedJdbc.update(sql, params);

        return farmacia;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean update(int id, Farmacia farmacia) {

        String sql = "UPDATE farmacia "
                + "SET nombre = :nombre, "
                + "    estado = :estado, "
                + "    ubicacion = :ubicacion, "
                + "    telefono = :telefono "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", farmacia.getNombre())
                .addValue("estado", farmacia.getEstado())
                .addValue("ubicacion", farmacia.getUbicacion())
                .addValue("telefono", farmacia.getTelefono())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // =========================
    // DESACTIVAR FARMACIA
    // =========================
    public boolean delete(int id) {

        String sql = "UPDATE farmacia "
                   + "SET estado = 'inactiva' "
                   + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}