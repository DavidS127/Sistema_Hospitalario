package com.hospital.repository;

import com.hospital.model.dto.MedicoDTO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public MedicoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    //RowMapper
    private final RowMapper<MedicoDTO> medicoMapper =
            (rs, rowNum) -> {

        MedicoDTO m = new MedicoDTO();

        m.setId(rs.getInt("id"));

        m.setTipoDocumento(
                rs.getString("tipo_documento"));

        m.setNumeroDocumento(
                rs.getString("numero_documento"));

        m.setNombres(
                rs.getString("nombres"));

        m.setApellidos(
                rs.getString("apellidos"));

        m.setFechaNacimiento(
                rs.getDate("fecha_nacimiento")
                        .toLocalDate());

        m.setTelefono(
                rs.getString("telefono"));

        m.setCorreo(
                rs.getString("correo"));

        m.setDireccion(
                rs.getString("direccion"));

        m.setEstado(
                rs.getString("estado"));

        m.setIdDepartamento(
                rs.getInt("id_departamento"));

        m.setEspecialidad(
                rs.getString("especialidad"));

        m.setRegistroMedico(
                rs.getString("registro_medico"));

        return m;
    };

    // Obtener todos los médicos
    public List<MedicoDTO> findAll() {
        String sql =
            "SELECT * "
          + "FROM empleado e "
          + "JOIN medico m "
          + "ON e.id = m.id_empleado "
          + "ORDER BY e.id";

        return jdbc.query(sql, medicoMapper);
    }

    // Buscar por ID
    public Optional<MedicoDTO> findById(int id) {

        String sql =
            "SELECT * "
          + "FROM empleado e "
          + "JOIN medico m "
          + "ON e.id = m.id_empleado "
          + "WHERE e.id = :id";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("id", id);

        List<MedicoDTO> medicos =
            namedJdbc.query(
                sql,
                params,
                medicoMapper
            );

        return medicos.stream().findFirst();
    }

    // Crear nuevo médico
    @Transactional
    public MedicoDTO save(MedicoDTO medico) {

        // Insert Empleado
        String sqlEmpleado =
            "INSERT INTO empleado ("
          + "tipo_documento, "
          + "numero_documento, "
          + "nombres, "
          + "apellidos, "
          + "fecha_nacimiento, "
          + "telefono, "
          + "correo, "
          + "direccion, "
          + "estado, "
          + "id_departamento"
          + ") VALUES ("
          + ":tipoDocumento, "
          + ":numeroDocumento, "
          + ":nombres, "
          + ":apellidos, "
          + ":fechaNacimiento, "
          + ":telefono, "
          + ":correo, "
          + ":direccion, "
          + ":estado, "
          + ":idDepartamento"
          + ")";

        MapSqlParameterSource paramsEmpleado =
            new MapSqlParameterSource()
                .addValue(
                    "tipoDocumento",
                    medico.getTipoDocumento())

                .addValue(
                    "numeroDocumento",
                    medico.getNumeroDocumento())

                .addValue(
                    "nombres",
                    medico.getNombres())

                .addValue(
                    "apellidos",
                    medico.getApellidos())

                .addValue(
                    "fechaNacimiento",
                    medico.getFechaNacimiento())

                .addValue(
                    "telefono",
                    medico.getTelefono())

                .addValue(
                    "correo",
                    medico.getCorreo())

                .addValue(
                    "direccion",
                    medico.getDireccion())

                .addValue(
                    "estado",
                    medico.getEstado())

                .addValue(
                    "idDepartamento",
                    medico.getIdDepartamento());

        KeyHolder keyHolder =
            new GeneratedKeyHolder();

        namedJdbc.update(
            sqlEmpleado,
            paramsEmpleado,
            keyHolder,
            new String[]{"id"}
        );

        int idEmpleado =
            keyHolder.getKey().intValue();

        // Insert medico
        String sqlMedico =
            "INSERT INTO medico ("
          + "id_empleado, "
          + "especialidad, "
          + "registro_medico"
          + ") VALUES ("
          + ":idEmpleado, "
          + ":especialidad, "
          + ":registroMedico"
          + ")";
        MapSqlParameterSource paramsMedico =
            new MapSqlParameterSource()
                .addValue(
                    "idEmpleado",
                    idEmpleado)

                .addValue(
                    "especialidad",
                    medico.getEspecialidad())

                .addValue(
                    "registroMedico",
                    medico.getRegistroMedico());

        namedJdbc.update(
            sqlMedico,
            paramsMedico
        );
        medico.setId(idEmpleado);
        return medico;
    }

    //Actualizar médico
    @Transactional
    public boolean update(int id, MedicoDTO medico) {
        String sqlEmpleado =
            "UPDATE empleado "
        + "SET tipo_documento = :tipoDocumento, "
        + "numero_documento = :numeroDocumento, "
        + "nombres = :nombres, "
        + "apellidos = :apellidos, "
        + "fecha_nacimiento = :fechaNacimiento, "
        + "telefono = :telefono, "
        + "correo = :correo, "
        + "direccion = :direccion, "
        + "estado = :estado, "
        + "id_departamento = :idDepartamento "
        + "WHERE id = :id";

        MapSqlParameterSource paramsEmpleado =
            new MapSqlParameterSource()
                .addValue("tipoDocumento", medico.getTipoDocumento())
                .addValue("numeroDocumento", medico.getNumeroDocumento())
                .addValue("fechaNacimiento", medico.getFechaNacimiento())
                .addValue("nombres", medico.getNombres())
                .addValue("apellidos", medico.getApellidos())
                .addValue("telefono", medico.getTelefono())
                .addValue("correo", medico.getCorreo())
                .addValue("direccion", medico.getDireccion())
                .addValue("estado", medico.getEstado())
                .addValue("idDepartamento", medico.getIdDepartamento())
                .addValue("id", id);

        int filasEmpleado =
            namedJdbc.update(
                sqlEmpleado,
                paramsEmpleado
            );

        String sqlMedico =
            "UPDATE medico "
          + "SET especialidad = :especialidad, "
          + "registro_medico = :registroMedico "
          + "WHERE id_empleado = :id";

        MapSqlParameterSource paramsMedico =
            new MapSqlParameterSource()
                .addValue(
                    "especialidad",
                    medico.getEspecialidad())

                .addValue(
                    "registroMedico",
                    medico.getRegistroMedico())

                .addValue(
                    "id",
                    id);

        namedJdbc.update(
            sqlMedico,
            paramsMedico
        );
        return filasEmpleado > 0;
    }

    //Eliminar médico
    public boolean delete(int id) {
        String sql =
            "UPDATE empleado "
          + "SET estado = 'inactivo' "
          + "WHERE id = :id";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("id", id);

        return namedJdbc.update(
                sql,
                params) > 0;
    }
}

