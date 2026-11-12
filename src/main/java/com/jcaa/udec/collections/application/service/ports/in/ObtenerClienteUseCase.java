package com.jcaa.udec.collections.application.service.ports.in;

import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.domain.core.model.Cliente;

import java.util.List;

public interface ObtenerClienteUseCase {
  List<Cliente> obtenerTodos();

  Cliente obtenerPorCodigo(ObtenerClienteConsulta consulta);
}
