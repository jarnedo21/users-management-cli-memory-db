package com.jcaa.udec.collections.application.service;

import com.jcaa.udec.collections.application.service.dto.command.EliminarClienteComando;
import com.jcaa.udec.collections.application.service.ports.in.EliminarClienteUseCase;
import com.jcaa.udec.collections.domain.core.valueobject.CodigoCliente;
import com.jcaa.udec.collections.domain.port.out.EliminarClientePort;

public class EliminarClienteService implements EliminarClienteUseCase {
  private final EliminarClientePort eliminarClientePort;

  public EliminarClienteService(EliminarClientePort eliminarClientePort) {
    this.eliminarClientePort = eliminarClientePort;
  }

  @Override
  public void eliminar(EliminarClienteComando comando) {
    CodigoCliente codigo = new CodigoCliente(comando.codigo());
    eliminarClientePort.eliminar(codigo.valor());
  }
}
