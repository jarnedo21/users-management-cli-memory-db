package com.jcaa.udec.collections.domain.port.out;

import com.jcaa.udec.collections.domain.core.model.Cliente;

public interface ObtenerClientesPort {
    Cliente buscarPorCodigo(String codigo);
}
