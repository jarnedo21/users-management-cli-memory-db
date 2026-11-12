package com.jcaa.udec.collections.application.service;

import com.jcaa.udec.collections.application.service.dto.command.ActualizarClienteComando;
import com.jcaa.udec.collections.application.service.mapper.ClienteMapper;
import com.jcaa.udec.collections.application.service.ports.in.ActualizarClienteUseCase;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.ActualizarClientePort;

public class ActualizarClienteService implements ActualizarClienteUseCase {
  private final ActualizarClientePort actualizarClientePort;

  public ActualizarClienteService(ActualizarClientePort actualizarClientePort) {
    this.actualizarClientePort = actualizarClientePort;
  }

  @Override
  public void actualizar(ActualizarClienteComando comando) {
    Cliente cliente = ClienteMapper.mapearACliente(comando);
    actualizarClientePort.actualizar(cliente);
  }
}
