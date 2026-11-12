package com.jcaa.udec.collections.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import com.jcaa.udec.collections.domain.port.out.ObtenerClientesPort;
import org.junit.jupiter.api.Test;

class ObtenerClientesServiceTest {
  private static final String CODIGO = "CLI-001";

  @Test
  void deberiaObtenerClientePorCodigo() {
    // Arrange
    Cliente clienteEsperado = crearCliente();
    ObtenerClientesPortStub obtenerClientesPort = new ObtenerClientesPortStub(clienteEsperado);
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
    private final Cliente cliente;
    private String codigoConsultado;

    private ObtenerClientesPortStub(Cliente cliente) {
      this.cliente = cliente;
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
