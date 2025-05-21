package com.mycompany.cajeroautomatico;

import java.util.*;

public class CajeroAutomatico {
    private List<Usuario> usuarios;
    private GestorUsuarios gestor;

    public CajeroAutomatico(GestorUsuarios gestor) {
        this.gestor = gestor;
        this.usuarios = gestor.cargarUsuarios();
    }

    public void mostrarMenuPrincipal() {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- CAJERO AUTOMATICO ---");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesion");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    registrarUsuario(sc);
                    break;
                case 2:
                    iniciarSesion(sc);
                    break;
                case 3:
                    System.out.println("Gracias por usar el sistema.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 3);
    }

    private void registrarUsuario(Scanner sc) {
        String nombreCompleto = leerCampoValido(sc, "nombre completo (sin numeros): ", "nombre");
        String fechaNacimiento = leerTexto(sc, "fecha de nacimiento (ej. 2000-01-01): ");
        String telefono = leerCampoValido(sc, "telefono (solo numeros): ", "telefono");
        String correo = leerCampoValido(sc, "correo (debe tener '@'): ", "correo");
        double saldo = leerDouble(sc, "saldo inicial (mayor o igual a 0): ");

        String usuario;
        while (true) {
            usuario = leerTexto(sc, "nombre de usuario: ");
            boolean existe = false;
            for (Usuario u : usuarios) {
                if (u.getUsuario().equals(usuario)) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                System.out.println("Ese usuario ya existe. Intente otro.");
            } else {
                break;
            }
        }

        String contrasena = leerTexto(sc, "contraseña: ");

        try {
            Usuario nuevo = new Usuario(usuario, contrasena, saldo,
                    nombreCompleto, fechaNacimiento, telefono, correo);
            usuarios.add(nuevo);
            gestor.guardarUsuarios(usuarios);
            System.out.println("Usuario registrado con exito.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    private void iniciarSesion(Scanner sc) {
        System.out.print("Usuario: ");
        String nombre = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        Usuario usuarioActual = gestor.autenticar(nombre, pass, usuarios);

        if (usuarioActual == null) {
            System.out.println("Usuario o contraseña incorrectos.");
            return;
        }

        System.out.println("Bienvenido señor " + usuarioActual.getNombreCompleto());
        mostrarMenuUsuario(usuarioActual, sc);
    }

    private void mostrarMenuUsuario(Usuario usuarioActual, Scanner sc) {
        int opcion;
        do {
            System.out.println("\n--- MENU DE OPERACIONES ---");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Retirar dinero");
            System.out.println("3. Depositar dinero");
            System.out.println("4. Cerrar sesion");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    System.out.println("Saldo actual: $" + usuarioActual.getSaldo());
                    break;
                case 2:
                    double retiro = leerDouble(sc, "Cantidad a retirar: ");
                    if (retiro > 0 && retiro <= usuarioActual.getSaldo()) {
                        usuarioActual.setSaldo(usuarioActual.getSaldo() - retiro);
                        System.out.println("Retiro exitoso. Nuevo saldo: $" + usuarioActual.getSaldo());
                        gestor.guardarUsuarios(usuarios);
                    } else {
                        System.out.println("Monto invalido o saldo insuficiente.");
                    }
                    break;
                case 3:
                    double deposito = leerDouble(sc, "Cantidad a depositar: ");
                    if (deposito > 0) {
                        usuarioActual.setSaldo(usuarioActual.getSaldo() + deposito);
                        System.out.println("Deposito exitoso. Nuevo saldo: $" + usuarioActual.getSaldo());
                        gestor.guardarUsuarios(usuarios);
                    } else {
                        System.out.println("Monto invalido.");
                    }
                    break;
                case 4:
                    System.out.println("Sesion cerrada.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 4);
    }


    private String leerCampoValido(Scanner sc, String mensaje, String tipo) {
        while (true) {
            System.out.print("Ingrese " + mensaje);
            String entrada = sc.nextLine();
            try {
                switch (tipo) {
                    case "nombre":
                        if (!entrada.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                            throw new IllegalArgumentException("El nombre no debe contener numeros.");
                        }
                        break;
                    case "telefono":
                        if (!entrada.matches("\\d{7,}")) {
                            throw new IllegalArgumentException("Telefono invalido.");
                        }
                        break;
                    case "correo":
                        if (!entrada.contains("@")) {
                            throw new IllegalArgumentException("Correo invalido.");
                        }
                        break;
                }
                return entrada;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private String leerTexto(Scanner sc, String mensaje) {
        System.out.print("Ingrese " + mensaje);
        return sc.nextLine();
    }

    private int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }
        }
    }

    private double leerDouble(Scanner sc, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(sc.nextLine());
                if (valor < 0) {
                    System.out.println("El numero no puede ser negativo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero válido.");
            }
        }
    }

    public static void main(String[] args) {
        GestorUsuarios gestor = new GestorUsuarios();
        CajeroAutomatico cajero = new CajeroAutomatico(gestor);
        cajero.mostrarMenuPrincipal();
    }
}
