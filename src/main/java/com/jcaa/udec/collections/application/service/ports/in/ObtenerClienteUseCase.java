package com.jcaa.udec.collections.application.service.ports.in;

import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.domain.core.model.Cliente;

public interface ObtenerClienteUseCase {
  Cliente obtenerPorCodigo(ObtenerClienteConsulta consulta);
}
