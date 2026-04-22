package com.utp.lab5.controller;

import com.utp.lab5.dto.pedido.PedidoResponse;
import com.utp.lab5.dto.pedido.RegistrarPedidoRequest;
import com.utp.lab5.service.PedidoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public PedidoResponse registrarPedido(@RequestBody RegistrarPedidoRequest request) {
        return pedidoService.registrarPedido(request);
    }
}