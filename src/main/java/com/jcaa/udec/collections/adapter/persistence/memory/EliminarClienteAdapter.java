package com.jcaa.udec.collections.adapter.persistence.memory;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.EliminarClientePort;

import java.util.List;

public class EliminarClienteAdapter implements EliminarClientePort {
    private final List<Cliente> clientes = ClientesMemoria.obtenerClientes();

    @Override
    public void eliminar(String codigo) {
        boolean eliminado = clientes.removeIf(cliente -> cliente.getCodigo().equalsIgnoreCase(codigo));
        if (!eliminado) {
            throw new ClienteNoExisteException();
        }
    }
}
