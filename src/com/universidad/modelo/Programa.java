package com.universidad.modelo;

import java.util.Date;

public class Programa {
    private Double id;
    private String nombre;
    private Double duracion;
    private Date registro;
    private Facultad facultad;
    
    public Programa() {}
    
    public Programa(Double id, String nombre, Double duracion, Date registro, Facultad facultad) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.registro = registro;
        this.facultad = facultad;
    }
    
    // Getters y Setters
    public Double getId() { return id; }
    public void setId(Double id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getDuracion() { return duracion; }
    public void setDuracion(Double duracion) { this.duracion = duracion; }
    public Date getRegistro() { return registro; }
    public void setRegistro(Date registro) { this.registro = registro; }
    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
    
    @Override
    public String toString() {
        return String.format("Programa - ID: %.0f, Nombre: %s, Duración: %.0f semestres, Facultad: %s",
                           id, nombre, duracion, 
                           facultad != null ? facultad.getNombre() : "N/A");
    }
}