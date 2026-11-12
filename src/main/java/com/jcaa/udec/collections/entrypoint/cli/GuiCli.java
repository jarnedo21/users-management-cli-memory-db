package com.jcaa.udec.collections.entrypoint.cli;

import com.jcaa.udec.collections.domain.core.exception.UsuarioInvalidoException;
import com.jcaa.udec.collections.domain.core.exception.UsuarioNoExisteException;
import com.jcaa.udec.collections.domain.core.exception.UsuarioYaExisteException;
import com.jcaa.udec.collections.domain.core.valueobject.Email;
import com.jcaa.udec.collections.domain.core.valueobject.NombreUsuario;
import com.jcaa.udec.collections.domain.core.valueobject.Password;
import com.jcaa.udec.collections.domain.core.valueobject.UsuarioId;
import com.jcaa.udec.collections.entrypoint.controller.ClienteControlador;
import com.jcaa.udec.collections.entrypoint.controller.UsuarioControlador;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarUsuarioPeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerUsuarioResponse;

import java.util.Scanner;

public class GuiCli {
    private static final int OPCION_AGREGAR = 1;
    private static final int OPCION_BUSCAR = 2;
    private static final int OPCION_MOSTRAR_TODOS = 3;
    private static final int OPCION_CLIENTES = 4;
    private static final int OPCION_SALIR = 5;
    private static final String TEXTO_TITULO = "** EJEMPLO DE USO DE LISTAS Y HEXAGONAL **";
    private static final String TITULO_REGISTRO = "** INGRESE LOS DATOS DEL NUEVO USUARIO **";
    private static final String SEPARADOR = "- - - - - - - - - ";
    private static final String OPCIONES = "Opciones:";
    private static final String TEXTO_OPCION_AGREGAR = "1 - Agregar";
    private static final String TEXTO_OPCION_BUSCAR = "2 - Buscar por Id";
    private static final String TEXTO_OPCION_MOSTRAR_TODOS = "3 - Ver todos";
    private static final String TEXTO_OPCION_CLIENTES = "4 - Gestionar clientes (Agencia de Castings)";
    private static final String TEXTO_OPCION_SALIR = "5 - Salir";
    private static final String TEXTO_SOLICITUD_OPCION = "Ingrese el numero de la opcion: ";
    private static final String SOLICITUD_ID = "ID: ";
    private static final String SOLICITUD_PASSWORD = "PASSWORD: ";
    private static final String SOLICITUD_NOMBRE = "NOMBRE: ";
    private static final String SOLICITUD_EMAIL = "EMAIL: ";
    private static final String MENSAJE_OPCION_INVALIDA = "Opcion [%s] invalida";
    private static final String MENSAJE_ID_INVALIDO = "ID INVALIDO: debe ser un numero entero";
    private static final String MENSAJE_PASSWORD_INVALIDO =
            "PASSWORD INVALIDO: minimo 10 caracteres, con mayuscula, minuscula, numero y simbolo";
    private static final String MENSAJE_NOMBRE_INVALIDO = "NOMBRE INVALIDO: minimo 3 caracteres";
    private static final String MENSAJE_EMAIL_INVALIDO = "EMAIL INVALIDO: ingrese un correo valido";
    private static final String MENSAJE_ERROR = "ERROR: ";
    private static final String MENSAJE_REGISTRO_EXITOSO = "Usuario registrado correctamente.";
    private static final String MENSAJE_LISTA_VACIA = "No hay usuarios registrados.";
    private static final String MENSAJE_DESPEDIDA = "Esperamos tu regreso. Bye, Bye";
    private static final String MARCA_ORDEN_BYTES = "\uFEFF";
    private static final String TEXTO_VACIO = "";
    private final UsuarioControlador usuarioControlador;
    private final ClienteCli clienteCli;
    private final Scanner entrada;

    public GuiCli(UsuarioControlador usuarioControlador, ClienteControlador clienteControlador) {
        this(usuarioControlador, clienteControlador, new Scanner(System.in));
    }

    GuiCli(
            UsuarioControlador usuarioControlador,
            ClienteControlador clienteControlador,
            Scanner entrada) {
        this.usuarioControlador = usuarioControlador;
        this.clienteCli = new ClienteCli(clienteControlador, entrada);
        this.entrada = entrada;
    }

    public int obtenerOpcionMenu() {
        do {
            mostrarMenu();
            String valorIngresado = limpiarEntrada(entrada.nextLine());
            try {
                int opcion = Integer.parseInt(valorIngresado);
                if (opcion >= OPCION_AGREGAR && opcion <= OPCION_SALIR) {
                    return opcion;
                }
            } catch (NumberFormatException exception) {
                // El flujo informa el valor invalido y vuelve a mostrar el menu.
            }
            System.out.printf(MENSAJE_OPCION_INVALIDA + "%n", valorIngresado);
        } while (true);
    }

    public void ejecutarAccion() {
        boolean continuar = true;
        while (continuar) {
            int opcion = obtenerOpcionMenu();
            try {
                switch (opcion) {
                    case OPCION_AGREGAR -> registrarUsuario();
                    case OPCION_BUSCAR -> mostrarUsuarioPorId();
                    case OPCION_MOSTRAR_TODOS -> mostrarTodosLosUsuarios();
                    case OPCION_CLIENTES -> clienteCli.ejecutarAccion();
                    case OPCION_SALIR -> continuar = false;
                }
            } catch (UsuarioInvalidoException
                    | UsuarioNoExisteException
                    | UsuarioYaExisteException exception) {
                System.out.println(MENSAJE_ERROR + exception.getMessage());
            }
        }
        System.out.println(MENSAJE_DESPEDIDA);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println(TEXTO_TITULO);
        System.out.println(SEPARADOR);
        System.out.println(OPCIONES);
        System.out.println(SEPARADOR);
        System.out.println(TEXTO_OPCION_AGREGAR);
        System.out.println(TEXTO_OPCION_BUSCAR);
        System.out.println(TEXTO_OPCION_MOSTRAR_TODOS);
        System.out.println(TEXTO_OPCION_CLIENTES);
        System.out.println(TEXTO_OPCION_SALIR);
        System.out.print(TEXTO_SOLICITUD_OPCION);
    }

    private void registrarUsuario() {
        usuarioControlador.registrar(capturarDatosUsuario());
        System.out.println(MENSAJE_REGISTRO_EXITOSO);
    }

    private void mostrarUsuarioPorId() {
        System.out.println(usuarioControlador.obtenerPorId(capturarId()));
    }

    private void mostrarTodosLosUsuarios() {
        ObtenerUsuarioResponse response = usuarioControlador.obtenerTodos();
        if (response.estaVacia()) {
            System.out.println(MENSAJE_LISTA_VACIA);
            return;
        }
        System.out.println(response);
    }

    private RegistrarUsuarioPeticion capturarDatosUsuario() {
        System.out.println();
        System.out.println(TITULO_REGISTRO);
        return new RegistrarUsuarioPeticion(
                capturarId(), capturarPassword(), capturarNombre(), capturarEmail());
    }

    private String capturarId() {
        do {
            System.out.print(SOLICITUD_ID);
            String id = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new UsuarioId(id))) {
                return id;
            }
            System.out.println(MENSAJE_ID_INVALIDO);
        } while (true);
    }

    private String capturarPassword() {
        do {
            System.out.print(SOLICITUD_PASSWORD);
            String password = entrada.nextLine();
            if (esValido(() -> new Password(password))) {
                return password;
            }
            System.out.println(MENSAJE_PASSWORD_INVALIDO);
        } while (true);
    }

    private String capturarNombre() {
        do {
            System.out.print(SOLICITUD_NOMBRE);
            String nombre = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new NombreUsuario(nombre))) {
                return nombre;
            }
            System.out.println(MENSAJE_NOMBRE_INVALIDO);
        } while (true);
    }

    private String capturarEmail() {
        do {
            System.out.print(SOLICITUD_EMAIL);
            String email = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new Email(email))) {
                return email;
            }
            System.out.println(MENSAJE_EMAIL_INVALIDO);
        } while (true);
    }

    private static String limpiarEntrada(String valor) {
        return valor.replace(MARCA_ORDEN_BYTES, TEXTO_VACIO).trim();
    }

    private static boolean esValido(Runnable validacion) {
        try {
            validacion.run();
            return true;
        } catch (UsuarioInvalidoException exception) {
            return false;
        }
    }
}
