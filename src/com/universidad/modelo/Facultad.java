package com.universidad.modelo;

public class Facultad {
    private Double id;
    private String nombre;
    private Persona decano;
    
    public Facultad() {}
    
    public Facultad(Double id, String nombre, Persona decano) {
        this.id = id;
        this.nombre = nombre;
        this.decano = decano;
    }
    
    // Getters y Setters
    public Double getId() { return id; }
    public void setId(Double id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Persona getDecano() { return decano; }
    public void setDecano(Persona decano) { this.decano = decano; }
    
    @Override
    public String toString() {
        return String.format("Facultad - ID: %.0f, Nombre: %s, Decano: %s",
                           id, nombre, decano != null ? decano.getNombres() : "N/A");
    }
}