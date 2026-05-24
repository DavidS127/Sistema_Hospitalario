package com.hospital.repository;

import com.hospital.model.Almacena;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlmacenaRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public AlmacenaRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Almacena> almacenaMapper = (rs, rowNum) -> {

        Almacena a = new Almacena();

        a.setIdFarmacia(rs.getInt("id_farmacia"));
        a.setIdMedicamento(rs.getInt("id_medicamento"));
        a.setStock(rs.getInt("stock"));

        return a;
    };

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Almacena> findAll() {

        String sql = "SELECT * FROM almacena "
                   + "ORDER BY id_farmacia, id_medicamento";

        return jdbc.query(sql, almacenaMapper);
    }

    // =========================
    // BUSCAR
    // =========================
    public Optional<Almacena> findByIds(
            int idFarmacia,
            int idMedicamento) {

        String sql = "SELECT * FROM almacena "
                   + "WHERE id_farmacia = :idFarmacia "
                   + "AND id_medicamento = :idMedicamento";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idFarmacia", idFarmacia)
                .addValue("idMedicamento", idMedicamento);

        List<Almacena> resultados = namedJdbc.query(
                sql,
                params,
                almacenaMapper
        );

        return resultados.stream().findFirst();
    }

    // =========================
    // CREAR
    // =========================
    public Almacena save(Almacena almacena) {

        String sql = "INSERT INTO almacena ("
                + "id_farmacia, "
                + "id_medicamento, "
                + "stock"
                + ") VALUES ("
                + ":idFarmacia, "
                + ":idMedicamento, "
                + ":stock"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idFarmacia", almacena.getIdFarmacia())
                .addValue("idMedicamento", almacena.getIdMedicamento())
                .addValue("stock", almacena.getStock());

        namedJdbc.update(sql, params);

        return almacena;
    }

    // =========================
    // ACTUALIZAR STOCK
    // =========================
    public boolean update(
            int idFarmacia,
            int idMedicamento,
            Almacena almacena) {

        String sql = "UPDATE almacena "
                + "SET stock = :stock "
                + "WHERE id_farmacia = :idFarmacia "
                + "AND id_medicamento = :idMedicamento";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("stock", almacena.getStock())
                .addValue("idFarmacia", idFarmacia)
                .addValue("idMedicamento", idMedicamento);

        return namedJdbc.update(sql, params) > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean delete(
            int idFarmacia,
            int idMedicamento) {

        String sql = "DELETE FROM almacena "
                   + "WHERE id_farmacia = :idFarmacia "
                   + "AND id_medicamento = :idMedicamento";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idFarmacia", idFarmacia)
                .addValue("idMedicamento", idMedicamento);

        return namedJdbc.update(sql, params) > 0;
    }
}