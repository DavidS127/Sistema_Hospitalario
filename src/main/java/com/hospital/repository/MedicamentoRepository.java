package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hospital.model.Medicamento;

@Repository
public class MedicamentoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public MedicamentoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Medicamento> medicamentoMapper = (rs, rowNum) -> {

        Medicamento m = new Medicamento();

        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setConcentracion(rs.getString("concentracion"));
        m.setViaAdministracion(rs.getString("via_administracion"));
        m.setFormaFarmaceutica(rs.getString("forma_farmaceutica"));

        return m;
    };

    // Listar todos los medicamentos
    public List<Medicamento> findAll() {

        String sql = "SELECT * FROM medicamento ORDER BY id";

        return jdbc.query(sql, medicamentoMapper);
    }

    // Buscar por ID 
    public Optional<Medicamento> findById(int id) {

        String sql = "SELECT * FROM medicamento WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Medicamento> medicamentos = namedJdbc.query(
                sql,
                params,
                medicamentoMapper
        );

        return medicamentos.stream().findFirst();
    }

    // Crear medicamento
    public Medicamento save(Medicamento medicamento) {

        String sql = "INSERT INTO medicamento ("
                + "nombre, "
                + "concentracion, "
                + "via_administracion, "
                + "forma_farmaceutica"
                + ") VALUES ("
                + ":nombre, "
                + ":concentracion, "
                + ":viaAdministracion, "
                + ":formaFarmaceutica"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", medicamento.getNombre())
                .addValue("concentracion", medicamento.getConcentracion())
                .addValue("viaAdministracion", medicamento.getViaAdministracion())
                .addValue("formaFarmaceutica", medicamento.getFormaFarmaceutica());

        namedJdbc.update(sql, params);

        return medicamento;
    }

    // Actualizar medicamento
    public boolean update(int id, Medicamento medicamento) {

        String sql = "UPDATE medicamento "
                + "SET nombre = :nombre, "
                + "    concentracion = :concentracion, "
                + "    via_administracion = :viaAdministracion, "
                + "    forma_farmaceutica = :formaFarmaceutica "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombre", medicamento.getNombre())
                .addValue("concentracion", medicamento.getConcentracion())
                .addValue("viaAdministracion", medicamento.getViaAdministracion())
                .addValue("formaFarmaceutica", medicamento.getFormaFarmaceutica())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // Eliminar medicamento
    public boolean delete(int id) {

        String sql = "DELETE FROM medicamento WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }
}