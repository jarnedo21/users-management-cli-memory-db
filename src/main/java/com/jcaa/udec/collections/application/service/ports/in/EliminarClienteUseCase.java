package com.jcaa.udec.collections.application.service.ports.in;

import com.jcaa.udec.collections.application.service.dto.command.EliminarClienteComando;

public interface EliminarClienteUseCase {
  void eliminar(EliminarClienteComando comando);
}
