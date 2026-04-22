package com.utp.lab5.repository;

import com.utp.lab5.dto.pedido.DetallePedidoRequest;
import com.utp.lab5.dto.pedido.RegistrarPedidoRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class PedidoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existeCliente(String idCliente) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE IdCliente = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idCliente);
        return count != null && count > 0;
    }

    public boolean existeEmpleado(Integer idEmpleado) {
        String sql = "SELECT COUNT(*) FROM empleados WHERE IdEmpleado = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idEmpleado);
        return count != null && count > 0;
    }

    public boolean existeFormaEnvio(Integer formaEnvio) {
        String sql = "SELECT COUNT(*) FROM envios WHERE `IdCompañiaEnvios` = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, formaEnvio);
        return count != null && count > 0;
    }

    public Integer insertarPedido(RegistrarPedidoRequest request) {
        String sql = """
                INSERT INTO pedidos
                (`IdCliente`, `IdEmpleado`, `FechaPedido`, `FechaEntrega`, `FechaEnvío`,
                 `FormaEnvío`, `Cargo`, `Destinatario`, `DirecciónDestinatario`,
                 `CiudadDestinatario`, `RegiónDestinatario`, `CódPostalDestinatario`,
                 `PaísDestinatario`)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getIdCliente());
            ps.setObject(2, request.getIdEmpleado());
            ps.setString(3, request.getFechaPedido());
            ps.setString(4, request.getFechaEntrega());
            ps.setString(5, request.getFechaEnvio());
            ps.setObject(6, request.getFormaEnvio());
            ps.setObject(7, request.getCargo());
            ps.setString(8, request.getDestinatario());
            ps.setString(9, request.getDireccionDestinatario());
            ps.setString(10, request.getCiudadDestinatario());
            ps.setString(11, request.getRegionDestinatario());
            ps.setString(12, request.getCodPostalDestinatario());
            ps.setString(13, request.getPaisDestinatario());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("No se pudo obtener el IdPedido generado");
        }

        return keyHolder.getKey().intValue();
    }

    public Integer obtenerStockConBloqueo(Integer idProducto) {
        String sql = """
                SELECT UnidadesEnExistencia
                FROM productos
                WHERE IdProducto = ?
                FOR UPDATE
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, idProducto);
    }

    public boolean existeProducto(Integer idProducto) {
        String sql = "SELECT COUNT(*) FROM productos WHERE IdProducto = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idProducto);
        return count != null && count > 0;
    }

    public void insertarDetalle(Integer idPedido, DetallePedidoRequest detalle) {
        String sql = """
                INSERT INTO detalle
                (IdPedido, IdProducto, PrecioUnidad, Cantidad, Descuento)
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                idPedido,
                detalle.getIdProducto(),
                detalle.getPrecioUnidad(),
                detalle.getCantidad(),
                detalle.getDescuento()
        );
    }

    public void descontarStock(Integer idProducto, Integer cantidad) {
        String sql = """
                UPDATE productos
                SET UnidadesEnExistencia = UnidadesEnExistencia - ?
                WHERE IdProducto = ?
                """;

        jdbcTemplate.update(sql, cantidad, idProducto);
    }
}