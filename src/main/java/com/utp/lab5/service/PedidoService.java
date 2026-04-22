package com.utp.lab5.service;

import com.utp.lab5.dto.pedido.DetallePedidoRequest;
import com.utp.lab5.dto.pedido.PedidoResponse;
import com.utp.lab5.dto.pedido.RegistrarPedidoRequest;
import com.utp.lab5.exception.BusinessException;
import com.utp.lab5.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public PedidoResponse registrarPedido(RegistrarPedidoRequest request) {
        validarCabecera(request);

        Integer idPedido = pedidoRepository.insertarPedido(request);

        for (DetallePedidoRequest detalle : request.getDetalles()) {
            validarDetalle(detalle);

            if (!pedidoRepository.existeProducto(detalle.getIdProducto())) {
                throw new BusinessException("No existe el producto con id: " + detalle.getIdProducto());
            }

            Integer stockActual = pedidoRepository.obtenerStockConBloqueo(detalle.getIdProducto());

            if (stockActual == null) {
                throw new BusinessException("No se pudo obtener el stock del producto " + detalle.getIdProducto());
            }

            if (stockActual < detalle.getCantidad()) {
                throw new BusinessException(
                        "Stock insuficiente para el producto " + detalle.getIdProducto() +
                                ". Stock actual: " + stockActual +
                                ", cantidad solicitada: " + detalle.getCantidad()
                );
            }

            pedidoRepository.insertarDetalle(idPedido, detalle);
            pedidoRepository.descontarStock(detalle.getIdProducto(), detalle.getCantidad());
        }

        return new PedidoResponse(idPedido, "Pedido registrado correctamente");
    }

    private void validarCabecera(RegistrarPedidoRequest request) {
        if (request.getIdCliente() == null || request.getIdCliente().trim().isEmpty()) {
            throw new BusinessException("El idCliente es obligatorio");
        }

        if (!pedidoRepository.existeCliente(request.getIdCliente())) {
            throw new BusinessException("No existe el cliente con id: " + request.getIdCliente());
        }

        if (request.getIdEmpleado() != null && !pedidoRepository.existeEmpleado(request.getIdEmpleado())) {
            throw new BusinessException("No existe el empleado con id: " + request.getIdEmpleado());
        }

        if (request.getFormaEnvio() != null && !pedidoRepository.existeFormaEnvio(request.getFormaEnvio())) {
            throw new BusinessException("No existe la forma de envío con id: " + request.getFormaEnvio());
        }

        if (request.getCargo() != null && request.getCargo() < 0) {
            throw new BusinessException("El cargo no puede ser negativo");
        }

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BusinessException("El pedido debe tener al menos un detalle");
        }
    }

    private void validarDetalle(DetallePedidoRequest detalle) {
        if (detalle.getIdProducto() == null) {
            throw new BusinessException("El idProducto del detalle es obligatorio");
        }

        if (detalle.getPrecioUnidad() == null || detalle.getPrecioUnidad() < 0) {
            throw new BusinessException("El precioUnidad del detalle debe ser mayor o igual a 0");
        }

        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new BusinessException("La cantidad del detalle debe ser mayor a 0");
        }

        if (detalle.getDescuento() == null || detalle.getDescuento() < 0) {
            throw new BusinessException("El descuento del detalle debe ser mayor o igual a 0");
        }
    }
}