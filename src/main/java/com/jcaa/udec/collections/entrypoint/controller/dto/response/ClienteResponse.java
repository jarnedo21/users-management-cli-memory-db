package com.jcaa.udec.collections.entrypoint.controller.dto.response;

import lombok.Builder;

@Builder
public record ClienteResponse(
        String codigo,
        String nombre,
        String direccion,
        String telefono,
        String personaContacto,
        String tipoActividad) {
    private static final String FORMATO_DATOS = """
            CODIGO: %s
            NOMBRE: %s
            DIRECCION: %s
            TELEFONO: %s
            CONTACTO: %s
            TIPO DE ACTIVIDAD: %s
            """;

    @Override
    public String toString() {
        return FORMATO_DATOS.formatted(
                codigo, nombre, direccion, telefono, personaContacto, tipoActividad);
    }
}
