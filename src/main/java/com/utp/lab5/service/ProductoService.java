package com.utp.lab5.service;

import com.utp.lab5.dto.producto.CrearProductoRequest;
import com.utp.lab5.dto.producto.ProductoResponse;
import com.utp.lab5.exception.BusinessException;
import com.utp.lab5.exception.NotFoundException;
import com.utp.lab5.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoResponse crearProducto(CrearProductoRequest request) {
        validarProducto(request);

        Integer nuevoId = productoRepository.obtenerSiguienteIdProducto();
        productoRepository.insertarProducto(nuevoId, request);

        return productoRepository.buscarPorId(nuevoId)
                .orElseThrow(() -> new BusinessException("No se pudo recuperar el producto creado"));
    }

    public ProductoResponse obtenerProducto(Integer idProducto) {
        return productoRepository.buscarPorId(idProducto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id: " + idProducto));
    }

    public List<ProductoResponse> listarProductos() {
        return productoRepository.listarProductos();
    }

    private void validarProducto(CrearProductoRequest request) {
        if (request.getNombreProducto() == null || request.getNombreProducto().trim().isEmpty()) {
            throw new BusinessException("El nombre del producto es obligatorio");
        }

        if (request.getIdProveedor() == null) {
            throw new BusinessException("El idProveedor es obligatorio");
        }

        if (request.getIdCategoria() == null) {
            throw new BusinessException("El idCategoria es obligatorio");
        }

        if (request.getCantidadPorUnidad() == null || request.getCantidadPorUnidad().trim().isEmpty()) {
            throw new BusinessException("La cantidadPorUnidad es obligatoria");
        }

        if (request.getPrecioUnidad() == null || request.getPrecioUnidad() < 0) {
            throw new BusinessException("El precioUnidad debe ser mayor o igual a 0");
        }

        if (request.getUnidadesEnExistencia() == null || request.getUnidadesEnExistencia() < 0) {
            throw new BusinessException("Las unidadesEnExistencia deben ser mayor o igual a 0");
        }

        if (request.getUnidadesEnPedido() == null || request.getUnidadesEnPedido() < 0) {
            throw new BusinessException("Las unidadesEnPedido deben ser mayor o igual a 0");
        }

        if (request.getNivelNuevoPedido() == null || request.getNivelNuevoPedido() < 0) {
            throw new BusinessException("El nivelNuevoPedido debe ser mayor o igual a 0");
        }

        if (request.getSuspendido() == null || request.getSuspendido().trim().isEmpty()) {
            throw new BusinessException("El campo suspendido es obligatorio");
        }
    }
}