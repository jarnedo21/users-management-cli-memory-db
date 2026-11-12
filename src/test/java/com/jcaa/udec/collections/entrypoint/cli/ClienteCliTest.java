package com.jcaa.udec.collections.entrypoint.cli;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.exception.ClienteYaExisteException;
import com.jcaa.udec.collections.entrypoint.controller.ClienteControlador;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.ActualizarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ClienteResponse;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class ClienteCliTest {
    private static final String CODIGO = "CLI-001";
    private static final String NOMBRE = "Moda Caribe S.A.S.";
    private static final String DIRECCION = "Calle 30 # 17-25, Cartagena";
    private static final String TELEFONO = "3001234567";
    private static final String CONTACTO = "Laura Gomez";
    private static final String TIPO = "MODA";
    private static final String VOLVER = "6";

    @Test
    void deberiaSolicitarOpcionHastaRecibirValorValido() {
        // Arrange
        ClienteCli clienteCli = crearClienteCli(new ClienteControladorStub(), "texto", "7", "﻿3");

        // Act
        String salida = capturarSalida(() -> assertThat(clienteCli.obtenerOpcionMenu()).isEqualTo(3));

        // Assert
        assertThat(salida).contains("Opcion [texto] invalida", "Opcion [7] invalida");
    }

    @Test
    void deberiaRegistrarClienteLuegoDeCorregirDatosInvalidos() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        ClienteCli clienteCli = crearClienteCli(
                controlador,
                "1",
                "C 1", CODIGO,
                "AB", NOMBRE,
                "Cl 1", DIRECCION,
                "123", TELEFONO,
                "Lu", CONTACTO,
                "deportes", TIPO,
                VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(controlador.getRegistros())
                .containsExactly(new RegistrarClientePeticion(
                        CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TIPO));
        assertThat(salida).contains(
                "CODIGO INVALIDO",
                "NOMBRE INVALIDO",
                "DIRECCION INVALIDA",
                "TELEFONO INVALIDO",
                "PERSONA DE CONTACTO INVALIDA",
                "TIPO DE ACTIVIDAD INVALIDO",
                "Cliente registrado correctamente.");
    }

    @Test
    void deberiaInformarCuandoNoHayClientesRegistrados() {
        // Arrange
        ClienteCli clienteCli = crearClienteCli(new ClienteControladorStub(), "3", VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains("No hay clientes registrados.");
    }

    @Test
    void deberiaEjecutarElCrudlCompletoDeClientes() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        ClienteCli clienteCli = crearClienteCli(
                controlador,
                "1", CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TIPO,
                "2", CODIGO,
                "4", CODIGO, "Cine Caribe Producciones", DIRECCION, TELEFONO, CONTACTO,
                "PUBLICIDAD_CINE",
                "3",
                "5", CODIGO,
                "3",
                VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains(
                "Cliente registrado correctamente.",
                "CODIGO: " + CODIGO,
                "NOMBRE: " + NOMBRE,
                "Cliente actualizado correctamente.",
                "NOMBRE: Cine Caribe Producciones",
                "TIPO DE ACTIVIDAD: PUBLICIDAD_CINE",
                "Cliente eliminado correctamente.",
                "No hay clientes registrados.");
        assertThat(controlador.obtenerTodos().estaVacia()).isTrue();
    }

    @Test
    void deberiaInformarErroresDelControlador() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        ClienteCli clienteCli = crearClienteCli(
                controlador,
                "1", CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TIPO,
                "1", CODIGO, NOMBRE, DIRECCION, TELEFONO, CONTACTO, TIPO,
                "2", "NO-EXISTE",
                VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains("ERROR: El cliente ya existe.", "ERROR: El cliente no existe.");
    }

    private static ClienteCli crearClienteCli(ClienteControlador controlador, String... entradas) {
        String contenido = String.join(System.lineSeparator(), entradas) + System.lineSeparator();
        return new ClienteCli(controlador, new Scanner(contenido));
    }

    private static String capturarSalida(Runnable accion) {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
        try {
            accion.run();
            return salida.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(salidaOriginal);
        }
    }

    private static final class ClienteControladorStub implements ClienteControlador {
        private final List<RegistrarClientePeticion> registros = new ArrayList<>();
        private final List<ClienteResponse> clientes = new ArrayList<>();

        @Override
        public void registrar(RegistrarClientePeticion peticion) {
            if (buscar(peticion.codigo()) >= 0) {
                throw new ClienteYaExisteException();
            }
            registros.add(peticion);
            clientes.add(new ClienteResponse(
                    peticion.codigo(),
                    peticion.nombre(),
                    peticion.direccion(),
                    peticion.telefono(),
                    peticion.personaContacto(),
                    peticion.tipoActividad()));
        }

        @Override
        public ObtenerClienteResponse obtenerPorCodigo(String codigo) {
            int indice = buscarExistente(codigo);
            return new ObtenerClienteResponse(List.of(clientes.get(indice)));
        }

        @Override
        public ObtenerClienteResponse obtenerTodos() {
            return new ObtenerClienteResponse(clientes);
        }

        @Override
        public void actualizar(ActualizarClientePeticion peticion) {
            int indice = buscarExistente(peticion.codigo());
            clientes.set(indice, new ClienteResponse(
                    peticion.codigo(),
                    peticion.nombre(),
                    peticion.direccion(),
                    peticion.telefono(),
                    peticion.personaContacto(),
                    peticion.tipoActividad()));
        }

        @Override
        public void eliminar(String codigo) {
            clientes.remove(buscarExistente(codigo));
        }

        private int buscarExistente(String codigo) {
            int indice = buscar(codigo);
            if (indice < 0) {
                throw new ClienteNoExisteException();
            }
            return indice;
        }

        private int buscar(String codigo) {
            for (int indice = 0; indice < clientes.size(); indice++) {
                if (clientes.get(indice).codigo().equalsIgnoreCase(codigo)) {
                    return indice;
                }
            }
            return -1;
        }

        private List<RegistrarClientePeticion> getRegistros() {
            return List.copyOf(registros);
        }
    }
}
