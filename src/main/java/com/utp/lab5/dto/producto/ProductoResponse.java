package com.utp.lab5.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductoResponse {

    private Integer idProducto;
    private String nombreProducto;
    private Integer idProveedor;
    private Integer idCategoria;
    private String cantidadPorUnidad;
    private Double precioUnidad;
    private Integer unidadesEnExistencia;
    private Integer unidadesEnPedido;
    private Integer nivelNuevoPedido;
    private String suspendido;
}