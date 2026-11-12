package com.jcaa.udec.collections.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.udec.collections.application.service.dto.command.ActualizarClienteComando;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import com.jcaa.udec.collections.domain.port.out.ActualizarClientePort;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActualizarClienteServiceTest {
  private static final String CODIGO = "CLI-001";
  private static final String NOMBRE = "Cine Caribe Producciones";
  private static final String DIRECCION = "Avenida Pedro de Heredia 45-10";
  private static final String TELEFONO = "+576056401234";
  private static final String CONTACTO = "Carlos Ruiz";

  @Test
  void deberiaMapearYActualizarCliente() {
    // Arrange
    ActualizarClientePortStub actualizarClientePort = new ActualizarClientePortStub();
    ActualizarClienteService service = new ActualizarClienteService(actualizarClientePort);
    ActualizarClienteComando comando = new ActualizarClienteComando(
        CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, "PUBLICIDAD_CINE");

    // Act
    service.actualizar(comando);

    // Assert
    assertThat(actualizarClientePort.getClientesActualizados())
        .singleElement()
        .extracting(
            Cliente::getCodigo,
            Cliente::getNombre,
            Cliente::getDireccion,
            Cliente::getTelefono,
            Cliente::getPersonaContacto,
            Cliente::getTipoActividad)
        .containsExactly(
            CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TipoActividad.PUBLICIDAD_CINE);
  }

  private static final class ActualizarClientePortStub implements ActualizarClientePort {
    private final List<Cliente> clientesActualizados = new ArrayList<>();

    @Override
    public void actualizar(Cliente cliente) {
      clientesActualizados.add(cliente);
    }

    private List<Cliente> getClientesActualizados() {
      return List.copyOf(clientesActualizados);
    }
  }
}
