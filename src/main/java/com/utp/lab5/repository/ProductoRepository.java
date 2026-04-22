package com.utp.lab5.repository;

import com.utp.lab5.dto.producto.CrearProductoRequest;
import com.utp.lab5.dto.producto.ProductoResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer obtenerSiguienteIdProducto() {
        String sql = "SELECT COALESCE(MAX(IdProducto), 0) + 1 FROM productos";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int insertarProducto(Integer idProducto, CrearProductoRequest request) {
        String sql = """
                INSERT INTO productos
                (IdProducto, NombreProducto, IdProveedor, IdCategoría, CantidadPorUnidad,
                 PrecioUnidad, UnidadesEnExistencia, UnidadesEnPedido, NivelNuevoPedido, Suspendido)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                idProducto,
                request.getNombreProducto(),
                request.getIdProveedor(),
                request.getIdCategoria(),
                request.getCantidadPorUnidad(),
                request.getPrecioUnidad(),
                request.getUnidadesEnExistencia(),
                request.getUnidadesEnPedido(),
                request.getNivelNuevoPedido(),
                request.getSuspendido()
        );
    }

    public Optional<ProductoResponse> buscarPorId(Integer idProducto) {
        String sql = """
                SELECT IdProducto, NombreProducto, IdProveedor, IdCategoría, CantidadPorUnidad,
                       PrecioUnidad, UnidadesEnExistencia, UnidadesEnPedido, NivelNuevoPedido, Suspendido
                FROM productos
                WHERE IdProducto = ?
                """;

        List<ProductoResponse> resultados = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ProductoResponse(
                        rs.getInt("IdProducto"),
                        rs.getString("NombreProducto"),
                        rs.getInt("IdProveedor"),
                        rs.getInt("IdCategoría"),
                        rs.getString("CantidadPorUnidad"),
                        rs.getDouble("PrecioUnidad"),
                        rs.getInt("UnidadesEnExistencia"),
                        rs.getInt("UnidadesEnPedido"),
                        rs.getInt("NivelNuevoPedido"),
                        rs.getString("Suspendido")
                ),
                idProducto
        );

        return resultados.stream().findFirst();
    }

    public List<ProductoResponse> listarProductos() {
        String sql = """
                SELECT IdProducto, NombreProducto, IdProveedor, IdCategoría, CantidadPorUnidad,
                       PrecioUnidad, UnidadesEnExistencia, UnidadesEnPedido, NivelNuevoPedido, Suspendido
                FROM productos
                ORDER BY IdProducto
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ProductoResponse(
                        rs.getInt("IdProducto"),
                        rs.getString("NombreProducto"),
                        rs.getInt("IdProveedor"),
                        rs.getInt("IdCategoría"),
                        rs.getString("CantidadPorUnidad"),
                        rs.getDouble("PrecioUnidad"),
                        rs.getInt("UnidadesEnExistencia"),
                        rs.getInt("UnidadesEnPedido"),
                        rs.getInt("NivelNuevoPedido"),
                        rs.getString("Suspendido")
                )
        );
    }
}