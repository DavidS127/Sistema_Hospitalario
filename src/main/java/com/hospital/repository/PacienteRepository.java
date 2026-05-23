package com.hospital.repository;

import com.hospital.model.Paciente;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PacienteRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public PacienteRepository(JdbcTemplate jdbc,
                              NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // RowMapper
    private final RowMapper<Paciente> pacienteMapper = (rs, rowNum) -> {

        Paciente p = new Paciente();

        p.setId(rs.getInt("id"));
        p.setTipoDocumento(rs.getString("tipo_documento"));
        p.setNumeroDocumento(rs.getString("numero_documento"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidos(rs.getString("apellidos"));
        p.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        p.setSexo(rs.getString("sexo"));
        p.setGrupoSanguineo(rs.getString("grupo_sanguineo"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setEps(rs.getString("eps"));
        p.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        p.setEstado(rs.getString("estado"));

        return p;
    };

    // Obtener todos los pacientes
    public List<Paciente> findAll() {
        String sql = "SELECT * FROM paciente ORDER BY id";
        return jdbc.query(sql, pacienteMapper);
    }

    // Busca por ID
    public Optional<Paciente> findById(int id) {
        try{
            return Optional.ofNullable(
                jdbc.queryForObject(
                    "SELECT * FROM paciente WHERE id = ?",
                    pacienteMapper,id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Crear nuevo paciente
    public Paciente save(Paciente paciente) {

        String sql = "INSERT INTO paciente ("
                + "tipo_documento, "
                + "numero_documento, "
                + "nombres, "
                + "apellidos, "
                + "fecha_nacimiento, "
                + "sexo, "
                + "grupo_sanguineo, "
                + "telefono, "
                + "correo, "
                + "direccion, "
                + "eps, "
                + "estado"
                + ") VALUES ("
                + ":tipoDocumento, "
                + ":numeroDocumento, "
                + ":nombres, "
                + ":apellidos, "
                + ":fechaNacimiento, "
                + ":sexo, "
                + ":grupoSanguineo, "
                + ":telefono, "
                + ":correo, "
                + ":direccion, "
                + ":eps, "
                + ":estado"
                + ")";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("tipoDocumento", paciente.getTipoDocumento())
                .addValue("numeroDocumento", paciente.getNumeroDocumento())
                .addValue("nombres", paciente.getNombres())
                .addValue("apellidos", paciente.getApellidos())
                .addValue("fechaNacimiento", paciente.getFechaNacimiento())
                .addValue("sexo", paciente.getSexo())
                .addValue("grupoSanguineo", paciente.getGrupoSanguineo())
                .addValue("telefono", paciente.getTelefono())
                .addValue("correo", paciente.getCorreo())
                .addValue("direccion", paciente.getDireccion())
                .addValue("eps", paciente.getEps())
                .addValue("estado", paciente.getEstado());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql, params, keyHolder, new String[]{"id"});
        paciente.setId(keyHolder.getKey().intValue());
        return paciente;
    }

    // Actualizar paciente
    public boolean update(int id, Paciente paciente) {
        String sql = "UPDATE paciente "
                + "SET tipo_documento = :tipoDocumento, "
                + "    numero_documento = :numeroDocumento, "
                + "    nombres = :nombres, "
                + "    apellidos = :apellidos, "
                + "    fecha_nacimiento = :fechaNacimiento, "
                + "    sexo = :sexo, "
                + "    grupo_sanguineo = :grupoSanguineo, "
                + "    telefono = :telefono, "
                + "    correo = :correo, "
                + "    direccion = :direccion, "
                + "    eps = :eps, "
                + "    estado = :estado "
                + "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("tipoDocumento", paciente.getTipoDocumento())
                .addValue("numeroDocumento", paciente.getNumeroDocumento())
                .addValue("nombres", paciente.getNombres())
                .addValue("apellidos", paciente.getApellidos())
                .addValue("fechaNacimiento", paciente.getFechaNacimiento())
                .addValue("sexo", paciente.getSexo())
                .addValue("grupoSanguineo", paciente.getGrupoSanguineo())
                .addValue("telefono", paciente.getTelefono())
                .addValue("correo", paciente.getCorreo())
                .addValue("direccion", paciente.getDireccion())
                .addValue("eps", paciente.getEps())
                .addValue("estado", paciente.getEstado())
                .addValue("id", id);

        return namedJdbc.update(sql, params) > 0;
    }

    // "Eliminar" paciente (cambiar estado a 'Inactivo')
    public boolean delete(int id) {
        String sql = "UPDATE paciente SET estado = 'Inactivo' WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return namedJdbc.update(sql, params) > 0;
    }
}