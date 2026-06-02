package com.hospital.repository;

import com.hospital.model.Almacena;
import com.hospital.model.dto.MedicamentoBajoStock;

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

    //RowMapper para el reporte de medicamentos bajo stock
    private final RowMapper<MedicamentoBajoStock>
                bajoStockMapper = (rs, rowNum) -> {
        MedicamentoBajoStock m = new MedicamentoBajoStock();

        m.setFarmacia(rs.getString("farmacia"));

        m.setMedicamento(rs.getString("medicamento"));

        m.setConcentracion(rs.getString("concentracion"));

        m.setStockActual(rs.getInt("stock_actual"));

        m.setStockPromedioGlobal(rs.getDouble("stock_promedio_global"));
        return m;
};

    // LISTAR TODOS
    public List<Almacena> findAll() {

        String sql = "SELECT * FROM almacena "
                   + "ORDER BY id_farmacia, id_medicamento";

        return jdbc.query(sql, almacenaMapper);
    }

    // BUSCAR
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

    // CREAR
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

    // ACTUALIZAR STOCK
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

    // ELIMINAR
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

    //Funcion fn_dispensar_medicamento para validar si hay stock suficiente 
    //y actualizar el almacenado en caso de que se pueda dispensar el medicamento
    public String dispensarMedicamento(
        Integer idFarmacia,
        Integer idMedicamento,
        Integer cantidad) {

        String sql = """
                SELECT fn_dispensar_medicamento(
                :idFarmacia,
                :idMedicamento,
                :cantidad
                )
        """;

        MapSqlParameterSource params =
                new MapSqlParameterSource()
                        .addValue("idFarmacia", idFarmacia)
                        .addValue("idMedicamento", idMedicamento)
                        .addValue("cantidad", cantidad);

        return namedJdbc.queryForObject(
                sql,
                params,
                String.class);
     }

      /* 
      * Recupera los medicamentos cuyo stock actual está por debajo
      * del promedio global de stock entre todas las farmacias.
      *
      * Útil para identificar medicamentos críticos que requieren
      * reabastecimiento prioritario.
      */ 

     public List<MedicamentoBajoStock>
                getMedicamentosBajoStock() {

        String sql = """
                SELECT
                f.nombre AS farmacia,
                m.nombre AS medicamento,
                m.concentracion,
                a.stock AS stock_actual,
                
                /*
                 * Subconsulta escalar: calcula el promedio global de stock
                 * considerando TODOS los registros de la tabla almacena,
                 * sin importar farmacia ni medicamento.
                 * Se redondea a 2 decimales para mejor legibilidad.
                 * Se repite más abajo en el WHERE con el mismo propósito.
                 */

                ROUND(
                        (
                        SELECT AVG(stock)
                        FROM almacena
                        ),
                        2
                ) AS stock_promedio_global

                FROM almacena a

                JOIN farmacia f
                ON a.id_farmacia = f.id

                JOIN medicamento m
                ON a.id_medicamento = m.id

                /*
                * Subconsulta de filtro: solo incluye registros cuyo stock
                * esté por debajo del promedio global. Esto identifica los
                * medicamentos que están en niveles por debajo de lo normal
                * en el sistema.
                */

                WHERE a.stock < (
                SELECT AVG(stock)
                FROM almacena
                )

                ORDER BY a.stock ASC
        """;

        return jdbc.query(sql, bajoStockMapper);
     }

}