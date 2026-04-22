package com.utp.lab5.controller;

import com.utp.lab5.dto.producto.CrearProductoRequest;
import com.utp.lab5.dto.producto.ProductoResponse;
import com.utp.lab5.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ProductoResponse crearProducto(@RequestBody CrearProductoRequest request) {
        return productoService.crearProducto(request);
    }

    @GetMapping("/{idProducto}")
    public ProductoResponse obtenerProducto(@PathVariable Integer idProducto) {
        return productoService.obtenerProducto(idProducto);
    }

    @GetMapping
    public List<ProductoResponse> listarProductos() {
        return productoService.listarProductos();
    }
}