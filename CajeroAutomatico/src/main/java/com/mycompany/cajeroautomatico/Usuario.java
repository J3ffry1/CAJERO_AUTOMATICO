package com.mycompany.cajeroautomatico;

public class Usuario {
    private String usuario;
    private String contrasena;
    private double saldo;

    private String nombreCompleto;
    private String fechaNacimiento;
    private String telefono;
    private String correo;

    public Usuario(String usuario, String contrasena, double saldo,
                   String nombreCompleto, String fechaNacimiento,
                   String telefono, String correo) {
        setUsuario(usuario);
        setContrasena(contrasena);
        setSaldo(saldo);
        setNombreCompleto(nombreCompleto);
        setFechaNacimiento(fechaNacimiento);
        setTelefono(telefono);
        setCorreo(correo);
    }

    // SETTERS CON VALIDACIÓN
    public void setUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacio.");
        }
        this.usuario = usuario;
    }

    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacia.");
        }
        this.contrasena = contrasena;
    }

    public void setSaldo(double saldo) {
        if (saldo < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo.");
        }
        this.saldo = saldo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (!nombreCompleto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El nombre completo no debe contener numeros.");
        }
        this.nombreCompleto = nombreCompleto;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento.trim();
    }

    public void setTelefono(String telefono) {
        if (!telefono.matches("\\d{7,}")) {
            throw new IllegalArgumentException("El telefono debe tener al menos 7 dígitos y solo numeros.");
        }
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        if (!correo.contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'.");
        }
        this.correo = correo;
    }

    // GETTERS
    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String toCSV() {
        return usuario + "," + contrasena + "," + saldo + "," +
               nombreCompleto + "," + fechaNacimiento + "," +
               telefono + "," + correo;
    }
}
