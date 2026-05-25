package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hospital.model.ConsultaMedica;

@Repository
public class ConsultaMedicaRepository {

    private final JdbcTemplate jdbc;

    public ConsultaMedicaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    //RowMapper
    private final RowMapper<ConsultaMedica> mapper =
            (rs, rowNum) -> {

        ConsultaMedica c = new ConsultaMedica();

        c.setId(rs.getInt("id"));

        c.setMotivoConsulta(
                rs.getString("motivo_consulta"));

        c.setTipoConsulta(
                rs.getString("tipo_consulta"));

        c.setIdMedico(
                rs.getInt("id_medico"));

        c.setIdDepartamento(
                rs.getInt("id_departamento"));

        return c;
    };

    // Listar todos las consultas médicas
    public List<ConsultaMedica> findAll() {

        String sql = """
                SELECT *
                FROM consulta_medica
                ORDER BY id
                """;

        return jdbc.query(sql, mapper);
    }

    // Buscar consulta médica por ID
    public Optional<ConsultaMedica> findById(int id) {

        String sql = """
                SELECT *
                FROM consulta_medica
                WHERE id = ?
                """;

        return jdbc.query(sql, mapper, id)
                .stream()
                .findFirst();
    }

    // Crear consulta médica
    public ConsultaMedica save(
            ConsultaMedica c) {

        String sql = """
                INSERT INTO consulta_medica(
                    motivo_consulta,
                    tipo_consulta,
                    id_medico,
                    id_departamento
                )
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;

        Integer id = jdbc.queryForObject(
                sql,
                Integer.class,
                c.getMotivoConsulta(),
                c.getTipoConsulta(),
                c.getIdMedico(),
                c.getIdDepartamento()
        );

        c.setId(id);

        return c;
    }

    // Actualizar consulta médica
    public boolean update(
            int id,
            ConsultaMedica c) {

        String sql = """
                UPDATE consulta_medica
                SET
                    motivo_consulta = ?,
                    tipo_consulta = ?,
                    id_medico = ?,
                    id_departamento = ?
                WHERE id = ?
                """;

        return jdbc.update(
                sql,
                c.getMotivoConsulta(),
                c.getTipoConsulta(),
                c.getIdMedico(),
                c.getIdDepartamento(),
                id
        ) > 0;
    }

    // Eliminar consulta médica
    public boolean delete(int id) {

        String sql = """
                DELETE FROM consulta_medica
                WHERE id = ?
                """;

        return jdbc.update(sql, id) > 0;
    }
}
