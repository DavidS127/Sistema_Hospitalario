package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hospital.model.Evento;

@Repository
public class EventoRepository {

    private final JdbcTemplate jdbc;

    public EventoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Evento> mapper =
            (rs, rowNum) -> {

        Evento e = new Evento();

        e.setId(rs.getInt("id"));

        e.setFecha(
                rs.getDate("fecha").toLocalDate());

        e.setHora(
                rs.getTime("hora").toLocalTime());

        e.setTipo(
                rs.getString("tipo"));

        e.setDescripcion(
                rs.getString("descripcion"));

        e.setIdHistoriaclinica(
                rs.getInt("id_historiaclinica"));

        e.setIdDepartamento(
                rs.getInt("id_departamento"));

        e.setIdMedico(
                rs.getInt("id_medico"));

        e.setIdConsultamedica(
                rs.getInt("id_consultamedica"));

        return e;
    };

    // Listar todos los eventos
    public List<Evento> findAll() {

        String sql = """
                SELECT *
                FROM evento
                ORDER BY id
                """;

        return jdbc.query(sql, mapper);
    }

    // Buscar evento por ID
    public Optional<Evento> findById(int id) {

        String sql = """
                SELECT *
                FROM evento
                WHERE id = ?
                """;

        return jdbc.query(sql, mapper, id)
                .stream()
                .findFirst();
    }

    // Crear evento
    public Evento save(Evento e) {

        String sql = """
                INSERT INTO evento(
                    fecha,
                    hora,
                    tipo,
                    descripcion,
                    id_historiaclinica,
                    id_departamento,
                    id_medico,
                    id_consultamedica
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        Integer id = jdbc.queryForObject(
                sql,
                Integer.class,
                e.getFecha(),
                e.getHora(),
                e.getTipo(),
                e.getDescripcion(),
                e.getIdHistoriaclinica(),
                e.getIdDepartamento(),
                e.getIdMedico(),
                e.getIdConsultamedica()
        );

        e.setId(id);

        return e;
    }

    // Actualizar evento
    public boolean update(
            int id,
            Evento e) {

        String sql = """
                UPDATE evento
                SET
                    fecha = ?,
                    hora = ?,
                    tipo = ?,
                    descripcion = ?,
                    id_historiaclinica = ?,
                    id_departamento = ?,
                    id_medico = ?,
                    id_consultamedica = ?
                WHERE id = ?
                """;

        return jdbc.update(
                sql,
                e.getFecha(),
                e.getHora(),
                e.getTipo(),
                e.getDescripcion(),
                e.getIdHistoriaclinica(),
                e.getIdDepartamento(),
                e.getIdMedico(),
                e.getIdConsultamedica(),
                id
        ) > 0;
    }

    // Eliminar evento
    public boolean delete(int id) {

        String sql = """
                DELETE FROM evento
                WHERE id = ?
                """;

        return jdbc.update(sql, id) > 0;
    }
}
