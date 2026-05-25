package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.model.dto.AdministrativoDTO;

@Repository
public class AdministrativoRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public AdministrativoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    //RowMapper
    private final RowMapper<AdministrativoDTO> administrativoMapper =
            (rs, rowNum) -> {

        AdministrativoDTO a = new AdministrativoDTO();

        a.setId(rs.getInt("id"));

        a.setTipoDocumento(
                rs.getString("tipo_documento"));

        a.setNumeroDocumento(
                rs.getString("numero_documento"));

        a.setNombres(
                rs.getString("nombres"));

        a.setApellidos(
                rs.getString("apellidos"));

        a.setFechaNacimiento(
                rs.getDate("fecha_nacimiento")
                        .toLocalDate());

        a.setTelefono(
                rs.getString("telefono"));

        a.setCorreo(
                rs.getString("correo"));

        a.setDireccion(
                rs.getString("direccion"));

        a.setEstado(
                rs.getString("estado"));

        a.setIdDepartamento(
                rs.getInt("id_departamento"));

        a.setArea(
                rs.getString("area"));

        return a;
    };

    // Obtener todos los administrativos
    public List<AdministrativoDTO> findAll() {
        String sql =
            "SELECT * "
          + "FROM empleado e "
          + "JOIN administrativo a "
          + "ON e.id = a.id_empleado "
          + "ORDER BY e.id";

        return jdbc.query(sql, administrativoMapper);
    }

    // Buscar por ID
    public Optional<AdministrativoDTO> findById(int id) {

        String sql =
            "SELECT * "
          + "FROM empleado e "
          + "JOIN administrativo a "
          + "ON e.id = a.id_empleado "
          + "WHERE e.id = :id";

        MapSqlParameterSource params =
            new MapSqlParameterSource()
                .addValue("id", id);

        List<AdministrativoDTO> administrativos =
            namedJdbc.query(
                sql,
                params,
                administrativoMapper
            );

        return administrativos.stream().findFirst();
    }

    // Crear nuevo administrativo
    @Transactional
    public AdministrativoDTO save(AdministrativoDTO administrativo) {

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
                    administrativo.getTipoDocumento())

                .addValue(
                    "numeroDocumento",
                    administrativo.getNumeroDocumento())

                .addValue(
                    "nombres",
                    administrativo.getNombres())

                .addValue(
                    "apellidos",
                    administrativo.getApellidos())

                .addValue(
                    "fechaNacimiento",
                    administrativo.getFechaNacimiento())

                .addValue(
                    "telefono",
                    administrativo.getTelefono())

                .addValue(
                    "correo",
                    administrativo.getCorreo())

                .addValue(
                    "direccion",
                    administrativo.getDireccion())

                .addValue(
                    "estado",
                    administrativo.getEstado())

                .addValue(
                    "idDepartamento",
                    administrativo.getIdDepartamento());

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

        // Insert administrativo
        String sqlAdministrativo =
            "INSERT INTO administrativo ("
          + "id_empleado, "
          + "area"
          + ") VALUES ("
          + ":idEmpleado, "
          + ":area"
          + ")";
        MapSqlParameterSource paramsAdministrativo =
            new MapSqlParameterSource()
                .addValue(
                    "idEmpleado",
                    idEmpleado)

                .addValue(
                    "area",
                    administrativo.getArea());

        namedJdbc.update(
            sqlAdministrativo,
            paramsAdministrativo
        );
        administrativo.setId(idEmpleado);
        return administrativo;
    }

    //Actualizar administrativo
    @Transactional
    public boolean update(int id, AdministrativoDTO administrativo) {
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
                .addValue("tipoDocumento", administrativo.getTipoDocumento())
                .addValue("numeroDocumento", administrativo.getNumeroDocumento())
                .addValue("fechaNacimiento", administrativo.getFechaNacimiento())
                .addValue("nombres", administrativo.getNombres())
                .addValue("apellidos", administrativo.getApellidos())
                .addValue("telefono", administrativo.getTelefono())
                .addValue("correo", administrativo.getCorreo())
                .addValue("direccion", administrativo.getDireccion())
                .addValue("estado", administrativo.getEstado())
                .addValue("idDepartamento", administrativo.getIdDepartamento())
                .addValue("id", id);

        int filasEmpleado =
            namedJdbc.update(
                sqlEmpleado,
                paramsEmpleado
            );

        String sqlAdministrativo =
            "UPDATE administrativo "
          + "SET area = :area "
          + "WHERE id_empleado = :id";

        MapSqlParameterSource paramsAdministrativo =
            new MapSqlParameterSource()
                .addValue(
                    "area",
                    administrativo.getArea())

                .addValue(
                    "id",
                    id);

        namedJdbc.update(
            sqlAdministrativo,
            paramsAdministrativo
        );
        return filasEmpleado > 0;
    }

    //Eliminar administrativo
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



