package com.mycompany.cajeroautomatico;

import java.io.*;
import java.util.*;

public class GestorUsuarios {
    private final String archivoCSV = "usuarios.csv";

    // Carga usuarios desde el archivo CSV
    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 7) {
                    String usuario = partes[0];
                    String contrasena = partes[1];
                    double saldo = Double.parseDouble(partes[2]);
                    String nombreCompleto = partes[3];
                    String fechaNacimiento = partes[4];
                    String telefono = partes[5];
                    String correo = partes[6];
                    usuarios.add(new Usuario(usuario, contrasena, saldo,
                            nombreCompleto, fechaNacimiento, telefono, correo));
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo CSV.");
        }
        return usuarios;
    }

    public void guardarUsuarios(List<Usuario> usuarios) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoCSV))) {
            for (Usuario u : usuarios) {
                pw.println(u.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar en el archivo CSV.");
        }
    }

    public Usuario autenticar(String usuario, String contrasena, List<Usuario> usuarios) {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                return u;
            }
        }
        return null;
    }
}


