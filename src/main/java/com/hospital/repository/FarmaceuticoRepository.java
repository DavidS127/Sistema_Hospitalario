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

import com.hospital.model.dto.FarmaceuticoDTO;

@Repository
public class FarmaceuticoRepository {

    private final JdbcTemplate jdbc;

    private final NamedParameterJdbcTemplate namedJdbc;

    public FarmaceuticoRepository(
            JdbcTemplate jdbc,
            NamedParameterJdbcTemplate namedJdbc) {

        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    //rowmapper
    private final RowMapper<FarmaceuticoDTO>
            farmaceuticoMapper = (rs, rowNum) -> {

        FarmaceuticoDTO f =
                new FarmaceuticoDTO();

        f.setId(rs.getInt("id"));

        f.setTipoDocumento(
                rs.getString("tipo_documento"));

        f.setNumeroDocumento(
                rs.getString("numero_documento"));

        f.setNombres(
                rs.getString("nombres"));

        f.setApellidos(
                rs.getString("apellidos"));

        f.setFechaNacimiento(
                rs.getDate("fecha_nacimiento")
                        .toLocalDate());

        f.setTelefono(
                rs.getString("telefono"));

        f.setCorreo(
                rs.getString("correo"));

        f.setDireccion(
                rs.getString("direccion"));

        f.setEstado(
                rs.getString("estado"));

        f.setIdDepartamento(
                rs.getInt("id_departamento"));

        f.setLicenciaProfesional(
                rs.getString(
                        "licencia_profesional"));

        f.setIdFarmacia(
                rs.getInt("id_farmacia"));

        return f;
    };

    //Listar todos los farmaceuticos
    public List<FarmaceuticoDTO> findAll() {

        String sql =
                "SELECT * "
              + "FROM empleado e "
              + "JOIN farmaceutico f "
              + "ON e.id = f.id_empleado "
              + "ORDER BY e.id";

        return jdbc.query(
                sql,
                farmaceuticoMapper);
    }

    //Buscar por ID
    public Optional<FarmaceuticoDTO>
            findById(int id) {

        String sql =
                "SELECT * "
              + "FROM empleado e "
              + "JOIN farmaceutico f "
              + "ON e.id = f.id_empleado "
              + "WHERE e.id = :id";

        MapSqlParameterSource params =
                new MapSqlParameterSource()
                        .addValue("id", id);

        List<FarmaceuticoDTO> lista =
                namedJdbc.query(
                        sql,
                        params,
                        farmaceuticoMapper
                );

        return lista.stream().findFirst();
    }

    //Crear farmaceutico (inserta tanto en EMPLEADO como en FARMACEUTICO)
    @Transactional
    public FarmaceuticoDTO save(
            FarmaceuticoDTO farmaceutico) {

        //Insert en Empleado
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
                        farmaceutico.getTipoDocumento())

                .addValue(
                        "numeroDocumento",
                        farmaceutico.getNumeroDocumento())

                .addValue(
                        "nombres",
                        farmaceutico.getNombres())

                .addValue(
                        "apellidos",
                        farmaceutico.getApellidos())

                .addValue(
                        "fechaNacimiento",
                        farmaceutico.getFechaNacimiento())

                .addValue(
                        "telefono",
                        farmaceutico.getTelefono())

                .addValue(
                        "correo",
                        farmaceutico.getCorreo())

                .addValue(
                        "direccion",
                        farmaceutico.getDireccion())

                .addValue(
                        "estado",
                        farmaceutico.getEstado())

                .addValue(
                        "idDepartamento",
                        farmaceutico.getIdDepartamento());

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

        //Insert en Farmaceutico
        String sqlFarmaceutico =
                "INSERT INTO farmaceutico ("
              + "id_empleado, "
              + "licencia_profesional, "
              + "id_farmacia"
              + ") VALUES ("
              + ":idEmpleado, "
              + ":licenciaProfesional, "
              + ":idFarmacia"
              + ")";

        MapSqlParameterSource paramsFarmaceutico =
                new MapSqlParameterSource()

                .addValue(
                        "idEmpleado",
                        idEmpleado)

                .addValue(
                        "licenciaProfesional",
                        farmaceutico
                                .getLicenciaProfesional())

                .addValue(
                        "idFarmacia",
                        farmaceutico.getIdFarmacia());

        namedJdbc.update(
                sqlFarmaceutico,
                paramsFarmaceutico
        );

        farmaceutico.setId(idEmpleado);

        return farmaceutico;
    }

    // Actualizar Farmaceutico (actualiza tanto EMPLEADO como FARMACEUTICO)
    @Transactional
    public boolean update(
            int id,
            FarmaceuticoDTO farmaceutico) {

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

                .addValue(
                        "tipoDocumento",
                        farmaceutico.getTipoDocumento())

                .addValue(
                        "numeroDocumento",
                        farmaceutico.getNumeroDocumento())

                .addValue(
                        "nombres",
                        farmaceutico.getNombres())

                .addValue(
                        "apellidos",
                        farmaceutico.getApellidos())

                .addValue(
                        "fechaNacimiento",
                        farmaceutico.getFechaNacimiento())

                .addValue(
                        "telefono",
                        farmaceutico.getTelefono())

                .addValue(
                        "correo",
                        farmaceutico.getCorreo())

                .addValue(
                        "direccion",
                        farmaceutico.getDireccion())

                .addValue(
                        "estado",
                        farmaceutico.getEstado())

                .addValue(
                        "idDepartamento",
                        farmaceutico.getIdDepartamento())

                .addValue(
                        "id",
                        id);

        int filasEmpleado =
                namedJdbc.update(
                        sqlEmpleado,
                        paramsEmpleado
                );

        String sqlFarmaceutico =
                "UPDATE farmaceutico "
              + "SET licencia_profesional = :licenciaProfesional, "
              + "id_farmacia = :idFarmacia "
              + "WHERE id_empleado = :id";

        MapSqlParameterSource paramsFarmaceutico =
                new MapSqlParameterSource()

                .addValue(
                        "licenciaProfesional",
                        farmaceutico
                                .getLicenciaProfesional())

                .addValue(
                        "idFarmacia",
                        farmaceutico.getIdFarmacia())

                .addValue(
                        "id",
                        id);

        namedJdbc.update(
                sqlFarmaceutico,
                paramsFarmaceutico
        );

        return filasEmpleado > 0;
    }

    // Eliminar Farmaceutico (cambia estado a 'INACTIVO' en EMPLEADO)
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