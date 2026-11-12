package com.jcaa.udec.collections.domain.port.out;

import com.jcaa.udec.collections.domain.core.model.Cliente;

import java.util.List;

public interface ObtenerClientesPort {
    List<Cliente> obtenerTodos();

    Cliente buscarPorCodigo(String codigo);
}
