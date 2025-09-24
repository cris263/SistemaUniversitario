package com.universidad.modelo;

public class Curso {
    private Long id;
    private String nombre;
    private Programa programa;
    private Boolean activo;
    
    public Curso() {}
    
    public Curso(Long id, String nombre, Programa programa, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Programa getPrograma() { return programa; }
    public void setPrograma(Programa programa) { this.programa = programa; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    @Override
    public String toString() {
        return String.format("Curso - ID: %d, Nombre: %s, Programa: %s, Activo: %s",
                           id, nombre, 
                           programa != null ? programa.getNombre() : "N/A", 
                           activo);
    }
}