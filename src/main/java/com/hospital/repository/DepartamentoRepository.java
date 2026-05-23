package com.hospital.repository;

import com.hospital.model.Departamento;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartamentoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public DepartamentoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Departamento> departamentoMapper = (rs, rowNum) -> {

        Departamento d = new Departamento();

        d.setId(rs.getInt("id"));
        d.setNombre(rs.getString("nombre"));
        d.setTelefono(rs.getString("telefono"));
        d.setUbicacion(rs.getString("ubicacion"));

        return d;
    };

    // Listar todos los departamentos
    public List<Departamento> findAll() {

        String sql = "SELECT * FROM departamento ORDER BY id";

        return jdbc.query(sql, departamentoMapper);
    }

    // Buscar por ID
    public Optional<Departamento> findById(int id) {

        String sql = "SELECT * FROM departamento WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Departamento> departamentos = namedJdbc.query(
                sql,
                params,
                departamentoMapper
        );

        return departamentos.stream().findFirst();
    }

    // Crear departamento
    public Departamento save(Departamento departamento) {

        String sql = "INSERT INTO departamento ("
                + "id, "
                + "nombre, "
                + "telefono, "
                + "ubicacion"
                + ") VALUES ("
                + ":id, "
                + ":nombre, "
                + ":telefono, "
                + ":ubicacion"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", departamento.getId())
                .addValue("nombre", departamento.getNombre())
                .addValue("telefono", departamento.getTelefono())
                .addValue("ubicacion", departamento.getUbicacion());

        namedJdbc.update(sql, params);

        return departamento;
    }

    // Actualizar departamento
    public boolean update(int id, Departamento departamento) {

        String sql = "UPDATE departamento "
                + "SET nombre = :nombre, "
                + "    telefono = :telefono, "
                + "    ubicacion = :ubicacion "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", departamento.getNombre())
                .addValue("telefono", departamento.getTelefono())
                .addValue("ubicacion", departamento.getUbicacion())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // Eliminar departamento
    public boolean delete(int id) {

        String sql = "DELETE FROM departamento WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}