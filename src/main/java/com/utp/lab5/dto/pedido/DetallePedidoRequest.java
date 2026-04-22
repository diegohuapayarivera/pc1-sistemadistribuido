package com.utp.lab5.dto.pedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedidoRequest {

    private Integer idProducto;
    private Double precioUnidad;
    private Integer cantidad;
    private Integer descuento;
}