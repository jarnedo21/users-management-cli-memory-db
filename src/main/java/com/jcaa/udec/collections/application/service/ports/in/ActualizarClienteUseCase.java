package com.jcaa.udec.collections.application.service.ports.in;

import com.jcaa.udec.collections.application.service.dto.command.ActualizarClienteComando;

public interface ActualizarClienteUseCase {
  void actualizar(ActualizarClienteComando comando);
}
