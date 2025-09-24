package com.universidad.modelo;

public abstract class Persona {
    protected Long id;
    protected String nombres;
    protected String apellidos;
    protected String email;
    
    public Persona() {}
    
    public Persona(Long id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(long id) { this.id = id; }
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
