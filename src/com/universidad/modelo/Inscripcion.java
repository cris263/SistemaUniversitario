package com.universidad.modelo;

public class Inscripcion {
    private Curso curso;
    private Integer anio;
    private Integer semestre;
    private Estudiante estudiante;
    
    public Inscripcion() {}
    
    public Inscripcion(Curso curso, Integer anio, Integer semestre, Estudiante estudiante) {
        this.curso = curso;
        this.anio = anio;
        this.semestre = semestre;
        this.estudiante = estudiante;
    }
    
    // Getters y Setters
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    
    @Override
    public String toString() {
        return String.format("Inscripción - Curso: %s, Anio: %d, Semestre: %d, Estudiante: %s",
                           curso != null ? curso.getNombre() : "N/A",
                           anio, semestre,
                           estudiante != null ? estudiante.getNombres() : "N/A");
    }
}