package com.jcaa.udec.collections.adapter.persistence.memory;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.ActualizarClientePort;

import java.util.List;

public class ActualizarClienteAdapter implements ActualizarClientePort {
    private final List<Cliente> clientes = ClientesMemoria.obtenerClientes();

    @Override
    public void actualizar(Cliente cliente) {
        for (int indice = 0; indice < clientes.size(); indice++) {
            if (clientes.get(indice).getCodigo().equalsIgnoreCase(cliente.getCodigo())) {
                clientes.set(indice, cliente);
                return;
            }
        }
        throw new ClienteNoExisteException();
    }
}
