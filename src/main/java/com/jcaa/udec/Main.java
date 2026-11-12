package com.jcaa.udec;

import com.jcaa.udec.collections.adapter.persistence.memory.ActualizarClienteAdapter;
import com.jcaa.udec.collections.adapter.persistence.memory.EliminarClienteAdapter;
import com.jcaa.udec.collections.adapter.persistence.memory.GuardarClienteAdapter;
import com.jcaa.udec.collections.adapter.persistence.memory.GuardarUsuarioAdapter;
import com.jcaa.udec.collections.adapter.persistence.memory.ObtenerClientesAdapter;
import com.jcaa.udec.collections.adapter.persistence.memory.ObtenerUsuariosAdapter;
import com.jcaa.udec.collections.application.service.ActualizarClienteService;
import com.jcaa.udec.collections.application.service.AgregarClienteService;
import com.jcaa.udec.collections.application.service.AgregarUsuarioService;
import com.jcaa.udec.collections.application.service.EliminarClienteService;
import com.jcaa.udec.collections.application.service.ObtenerClientesService;
import com.jcaa.udec.collections.application.service.ObtenerUsuariosService;
import com.jcaa.udec.collections.application.service.ports.in.ActualizarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.AgregarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.AgregarUsuarioUseCase;
import com.jcaa.udec.collections.application.service.ports.in.EliminarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.ObtenerClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.ObtenerUsuarioUseCase;
import com.jcaa.udec.collections.domain.port.out.ActualizarClientePort;
import com.jcaa.udec.collections.domain.port.out.EliminarClientePort;
import com.jcaa.udec.collections.domain.port.out.GuardarClientePort;
import com.jcaa.udec.collections.domain.port.out.GuardarUsuarioPort;
import com.jcaa.udec.collections.domain.port.out.ObtenerClientesPort;
import com.jcaa.udec.collections.domain.port.out.ObtenerUsuariosPort;
import com.jcaa.udec.collections.entrypoint.cli.GuiCli;
import com.jcaa.udec.collections.entrypoint.controller.ClienteControlador;
import com.jcaa.udec.collections.entrypoint.controller.ClienteControladorImpl;
import com.jcaa.udec.collections.entrypoint.controller.UsuarioControlador;
import com.jcaa.udec.collections.entrypoint.controller.UsuarioControladorImpl;

public class Main {
    public static void main(String[] args) {
        GuardarUsuarioPort guardarUsuarioPort = new GuardarUsuarioAdapter();
        ObtenerUsuariosPort obtenerUsuariosPort = new ObtenerUsuariosAdapter();
        AgregarUsuarioUseCase agregarUsuarioUseCase = new AgregarUsuarioService(guardarUsuarioPort);
        ObtenerUsuarioUseCase obtenerUsuarioUseCase = new ObtenerUsuariosService(obtenerUsuariosPort);
        UsuarioControlador usuarioControlador =
                new UsuarioControladorImpl(agregarUsuarioUseCase, obtenerUsuarioUseCase);

        GuardarClientePort guardarClientePort = new GuardarClienteAdapter();
        ObtenerClientesPort obtenerClientesPort = new ObtenerClientesAdapter();
        ActualizarClientePort actualizarClientePort = new ActualizarClienteAdapter();
        EliminarClientePort eliminarClientePort = new EliminarClienteAdapter();
        AgregarClienteUseCase agregarClienteUseCase = new AgregarClienteService(guardarClientePort);
        ObtenerClienteUseCase obtenerClienteUseCase = new ObtenerClientesService(obtenerClientesPort);
        ActualizarClienteUseCase actualizarClienteUseCase =
                new ActualizarClienteService(actualizarClientePort);
        EliminarClienteUseCase eliminarClienteUseCase =
                new EliminarClienteService(eliminarClientePort);
        ClienteControlador clienteControlador = new ClienteControladorImpl(
                agregarClienteUseCase,
                obtenerClienteUseCase,
                actualizarClienteUseCase,
                eliminarClienteUseCase);

        GuiCli guiCli = new GuiCli(usuarioControlador, clienteControlador);
        guiCli.ejecutarAccion();
    }
}
