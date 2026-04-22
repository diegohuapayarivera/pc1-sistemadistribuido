package com.utp.lab5.dto.pedido;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrarPedidoRequest {

    private String idCliente;
    private Integer idEmpleado;
    private String fechaPedido;
    private String fechaEntrega;
    private String fechaEnvio;
    private Integer formaEnvio;
    private Double cargo;
    private String destinatario;
    private String direccionDestinatario;
    private String ciudadDestinatario;
    private String regionDestinatario;
    private String codPostalDestinatario;
    private String paisDestinatario;
    private List<DetallePedidoRequest> detalles;
}