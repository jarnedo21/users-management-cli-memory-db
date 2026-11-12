package com.jcaa.udec.collections.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import com.jcaa.udec.collections.domain.port.out.ObtenerClientesPort;
import java.util.List;
import org.junit.jupiter.api.Test;

class ObtenerClientesServiceTest {
  private static final String CODIGO = "CLI-001";

  @Test
  void deberiaObtenerTodosLosClientes() {
    // Arrange
    Cliente cliente = crearCliente();
    ObtenerClientesPortStub obtenerClientesPort =
        new ObtenerClientesPortStub(List.of(cliente), cliente);
    ObtenerClientesService service = new ObtenerClientesService(obtenerClientesPort);

    // Act
    List<Cliente> clientes = service.obtenerTodos();

    // Assert
    assertThat(clientes).containsExactly(cliente);
  }

  @Test
  void deberiaObtenerClientePorCodigo() {
    // Arrange
    Cliente clienteEsperado = crearCliente();
    ObtenerClientesPortStub obtenerClientesPort =
        new ObtenerClientesPortStub(List.of(clienteEsperado), clienteEsperado);
    ObtenerClientesService service = new ObtenerClientesService(obtenerClientesPort);
    ObtenerClienteConsulta consulta = new ObtenerClienteConsulta(CODIGO);

    // Act
    Cliente cliente = service.obtenerPorCodigo(consulta);

    // Assert
    assertThat(cliente).isSameAs(clienteEsperado);
    assertThat(obtenerClientesPort.getCodigoConsultado()).isEqualTo(CODIGO);
  }

  private static Cliente crearCliente() {
    return new Cliente(
        CODIGO,
        "Moda Caribe S.A.S.",
        "Calle 30 # 17-25, Cartagena",
        "3001234567",
        "Laura Gomez",
        TipoActividad.MODA);
  }

  private static final class ObtenerClientesPortStub implements ObtenerClientesPort {
    private final List<Cliente> clientes;
    private final Cliente cliente;
    private String codigoConsultado;

    private ObtenerClientesPortStub(List<Cliente> clientes, Cliente cliente) {
      this.clientes = clientes;
      this.cliente = cliente;
    }

    @Override
    public List<Cliente> obtenerTodos() {
      return clientes;
    }

    @Override
    public Cliente buscarPorCodigo(String codigo) {
      codigoConsultado = codigo;
      return cliente;
    }

    private String getCodigoConsultado() {
      return codigoConsultado;
    }
  }
}
