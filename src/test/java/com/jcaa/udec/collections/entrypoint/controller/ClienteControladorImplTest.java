package com.jcaa.udec.collections.entrypoint.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.udec.collections.application.service.dto.command.ActualizarClienteComando;
import com.jcaa.udec.collections.application.service.dto.command.CrearClienteComando;
import com.jcaa.udec.collections.application.service.dto.command.EliminarClienteComando;
import com.jcaa.udec.collections.application.service.dto.query.ObtenerClienteConsulta;
import com.jcaa.udec.collections.application.service.ports.in.ActualizarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.AgregarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.EliminarClienteUseCase;
import com.jcaa.udec.collections.application.service.ports.in.ObtenerClienteUseCase;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.ActualizarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ClienteResponse;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClienteControladorImplTest {
    private static final String CODIGO = "CLI-001";
    private static final String NOMBRE = "Moda Caribe S.A.S.";
    private static final String DIRECCION = "Calle 30 # 17-25, Cartagena";
    private static final String TELEFONO = "3001234567";
    private static final String CONTACTO = "Laura Gomez";
    private static final String TIPO = "MODA";

    private final AgregarClienteUseCaseStub agregarClienteUseCase = new AgregarClienteUseCaseStub();
    private final ObtenerClienteUseCaseStub obtenerClienteUseCase = new ObtenerClienteUseCaseStub();
    private final ActualizarClienteUseCaseStub actualizarClienteUseCase =
            new ActualizarClienteUseCaseStub();
    private final EliminarClienteUseCaseStub eliminarClienteUseCase =
            new EliminarClienteUseCaseStub();
    private final ClienteControlador controlador = new ClienteControladorImpl(
            agregarClienteUseCase,
            obtenerClienteUseCase,
            actualizarClienteUseCase,
            eliminarClienteUseCase);

    @Test
    void deberiaRegistrarCliente() {
        // Arrange
        RegistrarClientePeticion peticion =
                new RegistrarClientePeticion(CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TIPO);

        // Act
        controlador.registrar(peticion);

        // Assert
        assertThat(agregarClienteUseCase.getComandos())
                .singleElement()
                .isEqualTo(new CrearClienteComando(
                        CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TIPO));
    }

    @Test
    void deberiaObtenerClientePorCodigo() {
        // Arrange
        // Act
        ObtenerClienteResponse response = controlador.obtenerPorCodigo(CODIGO);

        // Assert
        assertThat(obtenerClienteUseCase.getConsultas())
                .singleElement()
                .extracting(ObtenerClienteConsulta::codigo)
                .isEqualTo(CODIGO);
        assertThat(response.toString()).contains(
                "CODIGO: " + CODIGO,
                "NOMBRE: " + NOMBRE,
                "DIRECCION: " + DIRECCION,
                "TELEFONO: " + TELEFONO,
                "CONTACTO: " + CONTACTO,
                "TIPO DE ACTIVIDAD: Moda");
    }

    @Test
    void deberiaObtenerTodosLosClientes() {
        // Arrange
        // Act
        ObtenerClienteResponse response = controlador.obtenerTodos();

        // Assert
        assertThat(response.estaVacia()).isFalse();
        assertThat(response.clientes())
                .singleElement()
                .extracting(
                        ClienteResponse::codigo,
                        ClienteResponse::direccion,
                        ClienteResponse::telefono,
                        ClienteResponse::tipoActividad)
                .containsExactly(CODIGO, DIRECCION, TELEFONO, "Moda");
    }

    @Test
    void deberiaActualizarCliente() {
        // Arrange
        ActualizarClientePeticion peticion = new ActualizarClientePeticion(
                CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, "PUBLICIDAD_CINE");

        // Act
        controlador.actualizar(peticion);

        // Assert
        assertThat(actualizarClienteUseCase.getComandos())
                .singleElement()
                .isEqualTo(new ActualizarClienteComando(
                        CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, "PUBLICIDAD_CINE"));
    }

    @Test
    void deberiaEliminarCliente() {
        // Arrange
        // Act
        controlador.eliminar(CODIGO);

        // Assert
        assertThat(eliminarClienteUseCase.getComandos())
                .singleElement()
                .isEqualTo(new EliminarClienteComando(CODIGO));
    }

    private static Cliente crearCliente() {
        return new Cliente(CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TipoActividad.MODA);
    }

    private static final class AgregarClienteUseCaseStub implements AgregarClienteUseCase {
        private final List<CrearClienteComando> comandos = new ArrayList<>();

        @Override
        public void guardar(CrearClienteComando comando) {
            comandos.add(comando);
        }

        private List<CrearClienteComando> getComandos() {
            return List.copyOf(comandos);
        }
    }

    private static final class ObtenerClienteUseCaseStub implements ObtenerClienteUseCase {
        private final List<ObtenerClienteConsulta> consultas = new ArrayList<>();

        @Override
        public List<Cliente> obtenerTodos() {
            return List.of(crearCliente());
        }

        @Override
        public Cliente obtenerPorCodigo(ObtenerClienteConsulta consulta) {
            consultas.add(consulta);
            return crearCliente();
        }

        private List<ObtenerClienteConsulta> getConsultas() {
            return List.copyOf(consultas);
        }
    }

    private static final class ActualizarClienteUseCaseStub implements ActualizarClienteUseCase {
        private final List<ActualizarClienteComando> comandos = new ArrayList<>();

        @Override
        public void actualizar(ActualizarClienteComando comando) {
            comandos.add(comando);
        }

        private List<ActualizarClienteComando> getComandos() {
            return List.copyOf(comandos);
        }
    }

    private static final class EliminarClienteUseCaseStub implements EliminarClienteUseCase {
        private final List<EliminarClienteComando> comandos = new ArrayList<>();

        @Override
        public void eliminar(EliminarClienteComando comando) {
            comandos.add(comando);
        }

        private List<EliminarClienteComando> getComandos() {
            return List.copyOf(comandos);
        }
    }
}
