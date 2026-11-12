package com.jcaa.udec.collections.entrypoint.controller;

import com.jcaa.udec.collections.entrypoint.controller.dto.request.ActualizarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;

public interface ClienteControlador {
    void registrar(RegistrarClientePeticion peticion);

    ObtenerClienteResponse obtenerPorCodigo(String codigo);

    ObtenerClienteResponse obtenerTodos();

    void actualizar(ActualizarClientePeticion peticion);

    void eliminar(String codigo);
}
