package com.jcaa.udec.collections.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.application.service.dto.command.EliminarClienteComando;
import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import com.jcaa.udec.collections.domain.port.out.EliminarClientePort;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class EliminarClienteServiceTest {
  private static final String CODIGO = "CLI-001";

  @Test
  void deberiaEliminarClientePorCodigo() {
    // Arrange
    EliminarClientePortStub eliminarClientePort = new EliminarClientePortStub();
    EliminarClienteService service = new EliminarClienteService(eliminarClientePort);

    // Act
    service.eliminar(new EliminarClienteComando(CODIGO));

    // Assert
    assertThat(eliminarClientePort.getCodigosEliminados()).containsExactly(CODIGO);
  }

  @Test
  void deberiaRechazarCodigoInvalidoSinLlamarAlPuerto() {
    // Arrange
    EliminarClientePortStub eliminarClientePort = new EliminarClientePortStub();
    EliminarClienteService service = new EliminarClienteService(eliminarClientePort);

    // Act
    // Assert
    assertThatThrownBy(() -> service.eliminar(new EliminarClienteComando("C 1")))
        .isInstanceOf(ClienteInvalidoException.class);
    assertThat(eliminarClientePort.getCodigosEliminados()).isEmpty();
  }

  private static final class EliminarClientePortStub implements EliminarClientePort {
    private final List<String> codigosEliminados = new ArrayList<>();

    @Override
    public void eliminar(String codigo) {
      codigosEliminados.add(codigo);
    }

    private List<String> getCodigosEliminados() {
      return List.copyOf(codigosEliminados);
    }
  }
}
