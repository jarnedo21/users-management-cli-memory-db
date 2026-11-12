package com.jcaa.udec.collections.entrypoint.controller.dto.response;

import java.util.List;

public record ObtenerClienteResponse(List<ClienteResponse> clientes) {
    public ObtenerClienteResponse {
        clientes = List.copyOf(clientes);
    }

    public boolean estaVacia() {
        return clientes.isEmpty();
    }

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), clientes.stream()
                .map(ClienteResponse::toString)
                .toList());
    }
}
