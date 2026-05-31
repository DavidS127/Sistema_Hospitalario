package com.hospital.repository;
 
import com.hospital.model.dto.InventarioFarmacia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
/**
 * REPOSITORY: InventarioFarmaciaRepository
 *
 * PROPÓSITO:
 *   Consulta la vista vw_inventario_farmacia con filtros por farmacia
 *   y por nivel de alerta de stock.
 */
@Repository
public class InventarioFarmaciaRepository {
 
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
 
    public InventarioFarmaciaRepository(JdbcTemplate jdbc,
                                         NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }
 
    private final RowMapper<InventarioFarmacia> mapper = (rs, rowNum) -> {
        InventarioFarmacia i = new InventarioFarmacia();
        i.setIdFarmacia(rs.getInt("id_farmacia"));
        i.setFarmacia(rs.getString("farmacia"));
        i.setUbicacionFarmacia(rs.getString("ubicacion_farmacia"));
        i.setEstadoFarmacia(rs.getString("estado_farmacia"));
        i.setIdMedicamento(rs.getInt("id_medicamento"));
        i.setMedicamento(rs.getString("medicamento"));
        i.setConcentracion(rs.getString("concentracion"));
        i.setFormaFarmaceutica(rs.getString("forma_farmaceutica"));
        i.setViaAdministracion(rs.getString("via_administracion"));
        i.setStock(rs.getInt("stock"));
        i.setAlertaStock(rs.getString("alerta_stock"));
        return i;
    };
 
    // Inventario completo de todas las farmacias
    public List<InventarioFarmacia> findAll() {
        return jdbc.query("SELECT * FROM vw_inventario_farmacia", mapper);
    }
 
    // Inventario de una farmacia específica
    public List<InventarioFarmacia> findByFarmacia(int idFarmacia) {
        String sql = "SELECT * FROM vw_inventario_farmacia WHERE id_farmacia = :idFarmacia";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idFarmacia", idFarmacia);
        return namedJdbc.query(sql, params, mapper);
    }
 
    // Solo medicamentos críticos o bajos (para alertas)
    public List<InventarioFarmacia> findCriticos() {
        return jdbc.query(
            "SELECT * FROM vw_inventario_farmacia WHERE alerta_stock IN ('CRITICO', 'BAJO')",
            mapper
        );
    }
 
    // Medicamentos críticos o bajos de una farmacia específica
    public List<InventarioFarmacia> findCriticosByFarmacia(int idFarmacia) {
        String sql = "SELECT * FROM vw_inventario_farmacia " +
                     "WHERE id_farmacia = :idFarmacia AND alerta_stock IN ('CRITICO', 'BAJO')";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idFarmacia", idFarmacia);
        return namedJdbc.query(sql, params, mapper);
    }
}