package com.universidad.modelo;

public class CursoProfesor {
    private Profesor profesor;
    private Integer anio;
    private Integer semestre;
    private Curso curso;
    
    public CursoProfesor() {}
    
    public CursoProfesor(Profesor profesor, Integer anio, Integer semestre, Curso curso) {
        this.profesor = profesor;
        this.anio = anio;
        this.semestre = semestre;
        this.curso = curso;
    }
    
    // Getters y Setters
    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    
    @Override
    public String toString() {
        return String.format("CursoProfesor - Profesor: %s, Anio: %d, Semestre: %d, Curso: %s",
                           profesor != null ? profesor.getNombres() : "N/A",
                           anio, semestre,
                           curso != null ? curso.getNombre() : "N/A");
    }
}