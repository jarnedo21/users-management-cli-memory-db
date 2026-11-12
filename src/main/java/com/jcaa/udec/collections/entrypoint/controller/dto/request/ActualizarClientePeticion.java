package com.jcaa.udec.collections.entrypoint.controller.dto.request;

public record ActualizarClientePeticion(
        String codigo,
        String nombre,
        String direccion,
        String telefono,
        String personaContacto,
        String tipoActividad) {
}
