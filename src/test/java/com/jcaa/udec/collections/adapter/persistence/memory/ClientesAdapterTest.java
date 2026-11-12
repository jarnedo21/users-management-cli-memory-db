package com.jcaa.udec.collections.adapter.persistence.memory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.exception.ClienteYaExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientesAdapterTest {
    private static final AtomicInteger SECUENCIA_CODIGO = new AtomicInteger(100);
    private final GuardarClienteAdapter guardarClienteAdapter = new GuardarClienteAdapter();
    private final ObtenerClientesAdapter obtenerClientesAdapter = new ObtenerClientesAdapter();

    @BeforeEach
    void limpiarClientes() {
        ClientesMemoria.obtenerClientes().clear();
    }

    @Test
    void deberiaGuardarCliente() {
        // Arrange
        Cliente cliente = crearCliente();

        // Act
        guardarClienteAdapter.guardar(cliente);

        // Assert
        assertThat(ClientesMemoria.obtenerClientes()).containsExactly(cliente);
    }

    @Test
    void deberiaRechazarClienteDuplicadoSinImportarMayusculas() {
        // Arrange
        Cliente cliente = crearCliente("CLI-001");
        guardarClienteAdapter.guardar(cliente);

        // Act
        // Assert
        assertThatThrownBy(() -> guardarClienteAdapter.guardar(crearCliente("cli-001")))
                .isInstanceOf(ClienteYaExisteException.class)
                .hasMessage("El cliente ya existe.");
    }

    @Test
    void deberiaBuscarClientePorCodigoSinImportarMayusculas() {
        // Arrange
        Cliente cliente = crearCliente("CLI-001");
        guardarClienteAdapter.guardar(cliente);

        // Act
        Cliente clienteEncontrado = obtenerClientesAdapter.buscarPorCodigo("cli-001");

        // Assert
        assertThat(clienteEncontrado).isSameAs(cliente);
    }

    @Test
    void deberiaReportarClienteInexistente() {
        // Arrange
        guardarClienteAdapter.guardar(crearCliente());

        // Act
        // Assert
        assertThatThrownBy(() -> obtenerClientesAdapter.buscarPorCodigo("NO-EXISTE"))
                .isInstanceOf(ClienteNoExisteException.class)
                .hasMessage("El cliente no existe.");
    }

    @Test
    void deberiaObtenerTodosLosClientesGuardados() {
        // Arrange
        Cliente primerCliente = crearCliente();
        Cliente segundoCliente = crearCliente();
        guardarClienteAdapter.guardar(primerCliente);
        guardarClienteAdapter.guardar(segundoCliente);

        // Act
        List<Cliente> clientes = obtenerClientesAdapter.obtenerTodos();

        // Assert
        assertThat(clientes).containsExactly(primerCliente, segundoCliente);
    }

    @Test
    void deberiaRetornarUnaListaInmutable() {
        // Arrange
        List<Cliente> clientes = obtenerClientesAdapter.obtenerTodos();

        // Act
        // Assert
        assertThatThrownBy(() -> clientes.add(crearCliente()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    private static Cliente crearCliente() {
        return crearCliente("CLI-" + SECUENCIA_CODIGO.incrementAndGet());
    }

    private static Cliente crearCliente(String codigo) {
        return new Cliente(
                codigo,
                "Moda Caribe S.A.S.",
                "Calle 30 # 17-25, Cartagena",
                "3001234567",
                "Laura Gomez",
                TipoActividad.MODA);
    }
}
