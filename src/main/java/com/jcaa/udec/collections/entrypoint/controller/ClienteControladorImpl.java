package com.jcaa.udec.collections.entrypoint.controller;

import com.jcaa.udec.collections.application.service.dto.command.ActualizarClienteComando;
import com.jcaa.udec.collections.application.service.dto.command.CrearClienteComando;
import com.jcaa.udec.collections.application.service.dto.command.EliminarClienteComando;
import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.application.service.ports.in.ActualizarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.AgregarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.EliminarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.ObtenerClienteUseCase;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.ActualizarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;
import com.jcaa.udec.collections.entrypoint.controller.mapper.ClienteResponseMapper;

public class ClienteControladorImpl implements ClienteControlador {
    private final AgregarClienteUseCase agregarClienteUseCase;
    private final ObtenerClienteUseCase obtenerClienteUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final EliminarClienteUseCase eliminarClienteUseCase;

    public ClienteControladorImpl(
            AgregarClienteUseCase agregarClienteUseCase,
            ObtenerClienteUseCase obtenerClienteUseCase,
            ActualizarClienteUseCase actualizarClienteUseCase,
            EliminarClienteUseCase eliminarClienteUseCase) {
        this.agregarClienteUseCase = agregarClienteUseCase;
        this.obtenerClienteUseCase = obtenerClienteUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
        this.eliminarClienteUseCase = eliminarClienteUseCase;
    }

    @Override
    public void registrar(RegistrarClientePeticion peticion) {
        CrearClienteComando comando = new CrearClienteComando(
                peticion.codigo(),
                peticion.nombre(),
                peticion.direccion(),
                peticion.telefono(),
                peticion.personaContacto(),
                peticion.tipoActividad());
        agregarClienteUseCase.guardar(comando);
    }

    @Override
    public ObtenerClienteResponse obtenerPorCodigo(String codigo) {
        ObtenerClienteConsulta consulta = new ObtenerClienteConsulta(codigo);
        return ClienteResponseMapper.mapearAResponse(
                obtenerClienteUseCase.obtenerPorCodigo(consulta));
    }

    @Override
    public ObtenerClienteResponse obtenerTodos() {
        return ClienteResponseMapper.mapearAResponse(obtenerClienteUseCase.obtenerTodos());
    }

    @Override
    public void actualizar(ActualizarClientePeticion peticion) {
        ActualizarClienteComando comando = new ActualizarClienteComando(
                peticion.codigo(),
                peticion.nombre(),
                peticion.direccion(),
                peticion.telefono(),
                peticion.personaContacto(),
                peticion.tipoActividad());
        actualizarClienteUseCase.actualizar(comando);
    }

    @Override
    public void eliminar(String codigo) {
        eliminarClienteUseCase.eliminar(new EliminarClienteComando(codigo));
    }
}
