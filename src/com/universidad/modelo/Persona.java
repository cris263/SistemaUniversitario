package com.universidad.modelo;

public abstract class Persona {
    protected Double id;
    protected String nombres;
    protected String apellidos;
    protected String email;
    
    public Persona() {}
    
    public Persona(Double id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }
    
    // Getters y Setters
    public Double getId() { return id; }
    public void setId(Double id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public String toString() {
        return String.format("ID: %.0f, Nombres: %s, Apellidos: %s, Email: %s", 
                           id, nombres, apellidos, email);
    }
}
