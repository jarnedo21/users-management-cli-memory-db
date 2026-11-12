package com.jcaa.udec.collections.application.service;

import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.application.service.ports.in.ObtenerClienteUseCase;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.ObtenerClientesPort;

import java.util.List;

public class ObtenerClientesService implements ObtenerClienteUseCase {
  private final ObtenerClientesPort obtenerClientesPort;

  public ObtenerClientesService(ObtenerClientesPort obtenerClientesPort) {
    this.obtenerClientesPort = obtenerClientesPort;
  }

  @Override
  public List<Cliente> obtenerTodos() {
    return obtenerClientesPort.obtenerTodos();
  }

  @Override
  public Cliente obtenerPorCodigo(ObtenerClienteConsulta consulta) {
    return obtenerClientesPort.buscarPorCodigo(consulta.codigo());
  }
}
