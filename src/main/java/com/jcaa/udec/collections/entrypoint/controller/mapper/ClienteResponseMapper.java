package com.jcaa.udec.collections.entrypoint.controller.mapper;

import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ClienteResponse;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;
import java.util.List;

public final class ClienteResponseMapper {
    private ClienteResponseMapper() {
    }

    public static ObtenerClienteResponse mapearAResponse(Cliente cliente) {
        return new ObtenerClienteResponse(List.of(mapearAResponseCliente(cliente)));
    }

    public static ObtenerClienteResponse mapearAResponse(List<Cliente> clientes) {
        return new ObtenerClienteResponse(clientes.stream()
                .map(ClienteResponseMapper::mapearAResponseCliente)
                .toList());
    }

    private static ClienteResponse mapearAResponseCliente(Cliente cliente) {
        return ClienteResponse.builder()
                .codigo(cliente.getCodigo())
                .nombre(cliente.getNombre())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .personaContacto(cliente.getPersonaContacto())
                .tipoActividad(cliente.getTipoActividad().getDescripcion())
                .build();
    }
}
