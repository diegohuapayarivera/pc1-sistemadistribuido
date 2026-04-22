package com.utp.lab5.dto.pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PedidoResponse {

    private Integer idPedido;
    private String mensaje;
}