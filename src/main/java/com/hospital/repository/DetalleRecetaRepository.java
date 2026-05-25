package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hospital.model.DetalleReceta;

@Repository
public class DetalleRecetaRepository {

    private final JdbcTemplate jdbc;

    public DetalleRecetaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<DetalleReceta> mapper =
            (rs, rowNum) -> {

        DetalleReceta d = new DetalleReceta();

        d.setId(rs.getInt("id"));

        d.setDosis(
                rs.getString("dosis"));

        d.setFrecuencia(
                rs.getString("frecuencia"));

        d.setDuracion(
                rs.getString("duracion"));

        d.setIdReceta(
                rs.getInt("id_receta"));

        d.setIdMedicamento(
                rs.getInt("id_medicamento"));

        return d;
    };

    // Listar todos los detalles de receta
    public List<DetalleReceta> findAll() {

        String sql = """
                SELECT *
                FROM detalle_receta
                ORDER BY id
                """;

        return jdbc.query(sql, mapper);
    }

    // Buscar detalle de receta por ID
    public Optional<DetalleReceta> findById(int id) {

        String sql = """
                SELECT *
                FROM detalle_receta
                WHERE id = ?
                """;

        return jdbc.query(sql, mapper, id)
                .stream()
                .findFirst();
    }

    // Crear detalle de receta
    public DetalleReceta save(
            DetalleReceta d) {

        String sql = """
                INSERT INTO detalle_receta(
                    dosis,
                    frecuencia,
                    duracion,
                    id_receta,
                    id_medicamento
                )
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        Integer id = jdbc.queryForObject(
                sql,
                Integer.class,
                d.getDosis(),
                d.getFrecuencia(),
                d.getDuracion(),
                d.getIdReceta(),
                d.getIdMedicamento()
        );

        d.setId(id);

        return d;
    }

    // Actualizar detalle de receta
    public boolean update(
            int id,
            DetalleReceta d) {

        String sql = """
                UPDATE detalle_receta
                SET
                    dosis = ?,
                    frecuencia = ?,
                    duracion = ?,
                    id_receta = ?,
                    id_medicamento = ?
                WHERE id = ?
                """;

        return jdbc.update(
                sql,
                d.getDosis(),
                d.getFrecuencia(),
                d.getDuracion(),
                d.getIdReceta(),
                d.getIdMedicamento(),
                id
        ) > 0;
    }

    // Eliminar detalle de receta
    public boolean delete(int id) {

        String sql = """
                DELETE FROM detalle_receta
                WHERE id = ?
                """;

        return jdbc.update(sql, id) > 0;
    }
}
