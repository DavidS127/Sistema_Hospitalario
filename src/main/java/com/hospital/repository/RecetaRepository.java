package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hospital.model.Receta;

@Repository
public class RecetaRepository {

    private final JdbcTemplate jdbc;

    public RecetaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // RowMapper
    private final RowMapper<Receta> mapper =
            (rs, rowNum) -> {

        Receta r = new Receta();

        r.setId(rs.getInt("id"));

        r.setFecha(
                rs.getDate("fecha").toLocalDate());

        r.setHora(
                rs.getTime("hora").toLocalTime());

        r.setIdConsultamedica(
                rs.getInt("id_consultamedica"));

        return r;
    };

    // Listar todas las recetas
    public List<Receta> findAll() {

        String sql = """
                SELECT *
                FROM receta
                ORDER BY id
                """;

        return jdbc.query(sql, mapper);
    }

    // Buscar receta por ID
    public Optional<Receta> findById(int id) {

        String sql = """
                SELECT *
                FROM receta
                WHERE id = ?
                """;

        return jdbc.query(sql, mapper, id)
                .stream()
                .findFirst();
    }

    // Crear receta
    public Receta save(Receta r) {

        String sql = """
                INSERT INTO receta(
                    id_consultamedica
                )
                VALUES (?)
                RETURNING id
                """;

        Integer id = jdbc.queryForObject(
                sql,
                Integer.class,
                r.getIdConsultamedica()
        );

        r.setId(id);

        return r;
    }

    // Actualizar receta
    public boolean update(
            int id,
            Receta r) {

        String sql = """
                UPDATE receta
                SET
                id_consultamedica = ?
                
                WHERE id = ?
                """;

        return jdbc.update(
                sql,
                r.getIdConsultamedica(),
                id
        ) > 0;
    }

    // Eliminar receta
    public boolean delete(int id) {

        String sql = """
                DELETE FROM receta
                WHERE id = ?
                """;

        return jdbc.update(sql, id) > 0;
    }
}
