package com.mycompany.cajeroautomatico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CajeroAutomatico {

    static final String ARCHIVO = "usuarios.csv";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("\n--- CAJERO AUTOMATICO ---");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesion");
            System.out.println("3. Salir");
            System.out.print("Elija una opcion: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1 -> registrarUsuario();
                case 2 -> iniciarSesion();
                case 3 -> {
                    System.out.println("Gracias por usar el sistema.");
                    return;
                }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    static void registrarUsuario() throws IOException {
        System.out.print("Ingrese nuevo usuario: ");
        String usuario = sc.nextLine().trim();
        System.out.print("Ingrese contraseña: ");
        String pass = sc.nextLine().trim();

        System.out.print("Saldo inicial: ");
        double saldo = leerDouble();

        BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true));
        bw.write(usuario + "," + pass + "," + saldo + "\n");
        bw.close();
        System.out.println("Usuario registrado con éxito.");
    }

    static void iniciarSesion() throws IOException {
        boolean autenticado = false;

        while (!autenticado) {
            System.out.print("Usuario: ");
            String usuario = sc.nextLine().trim();
            System.out.print("Contraseña: ");
            String pass = sc.nextLine().trim();

            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO));
            String linea;
            boolean encontrado = false;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(usuario) && datos[1].equals(pass)) {
                    double saldo = Double.parseDouble(datos[2]);
                    System.out.println("Inicio de sesión exitoso.");
                    menuOperaciones(usuario, pass, saldo);
                    encontrado = true;
                    autenticado = true;
                    break;
                }
            }

            br.close();
            if (!encontrado) {
                System.out.println("Usuario o contraseña incorrectos. Intente de nuevo.");
            }
        }
    }

    static void menuOperaciones(String usuario, String pass, double saldo) throws IOException {
        int opcion;
        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Retirar dinero");
            System.out.println("3. Depositar dinero");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> System.out.println("Saldo actual: $" + saldo);
                case 2 -> {
                    System.out.print("Cantidad a retirar: ");
                    double retiro = leerDouble();
                    if (retiro <= saldo && retiro >= 0) {
                        saldo -= retiro;
                        System.out.println("Retiro exitoso. Nuevo saldo: $" + saldo);
                        actualizarSaldo(usuario, pass, saldo);
                    } else {
                        System.out.println("Saldo insuficiente o cantidad inválida.");
                    }
                }
                case 3 -> {
                    System.out.print("Cantidad a depositar: ");
                    double deposito = leerDouble();
                    if (deposito >= 0) {
                        saldo += deposito;
                        System.out.println("Depósito exitoso. Nuevo saldo: $" + saldo);
                        actualizarSaldo(usuario, pass, saldo);
                    } else {
                        System.out.println("Cantidad inválida.");
                    }
                }
                case 4 -> System.out.println("Sesión cerrada.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }




static void actualizarSaldo(String usuario, String pass, double nuevoSaldo) {
    List<String> lineasActualizadas = new ArrayList<>();
    boolean actualizado = false;

    try (BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO))) {
        String linea;
        while ((linea = lector.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos[0].equals(usuario) && datos[1].equals(pass)) {
                lineasActualizadas.add(usuario + "," + pass + "," + nuevoSaldo);
                actualizado = true;
            } else {
                lineasActualizadas.add(linea);
            }
        }
    } catch (IOException e) {
        System.out.println("⚠️ Error al leer el archivo: " + e.getMessage());
        return;
    }

    // Ahora sobrescribimos el archivo completo
    try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO, false))) {
        for (String nuevaLinea : lineasActualizadas) {
            escritor.write(nuevaLinea);
            escritor.newLine();
        }
        System.out.println("✅ Saldo actualizado correctamente.");
    } catch (IOException e) {
        System.out.println("⚠️ Error al escribir en el archivo: " + e.getMessage());
    }

    if (!actualizado) {
        System.out.println("⚠️ Usuario no encontrado para actualizar.");
    }
}





    static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    static double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un valor numérico válido: ");
            }
        }
    }
}

