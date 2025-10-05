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

/*
package com.universidad.modelo;

import java.util.ArrayList;
import java.util.List;

public class Curso implements SujetoCurso {

    private Long id;
    private String nombre;
    private Programa programa;
    private boolean activo;

    private List<ObservadorCurso> observadores = new ArrayList<>();

    // Constructor
    public Curso(Long id, String nombre, Programa programa, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    // Getters
    public Long getId() {return id;}

    public String getNombre() {return nombre;}

    public Programa getPrograma() {return programa;}

    public boolean isActivo() {return activo;}

    // Setters con notificación
    public void setNombre(String nombre) {
        String nombreAnterior = this.nombre;
        this.nombre = nombre;
        notificarObservadores("El curso ID: " + id + " cambió su nombre de '" +
                nombreAnterior + "' a '" + nombre + "'");
    }

    public void setPrograma(Programa programa) {
        String programaAnterior = this.programa != null ? this.programa.getNombre() : "ninguno";
        this.programa = programa;
        String programaNuevo = programa != null ? programa.getNombre() : "ninguno";
        notificarObservadores("El curso '" + nombre + "' cambió de programa: de '" +
                programaAnterior + "' a '" + programaNuevo + "'");
    }

    public void setActivo(boolean activo) {
        if (this.activo != activo) {
            this.activo = activo;
            String estado = activo ? "ACTIVO" : "INACTIVO";
            notificarObservadores("El curso '" + nombre + "' cambió su estado a " + estado);
        }
    }

    // Implementación de SujetoCurso
    @Override
    public void agregarObservador(ObservadorCurso observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(ObservadorCurso observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(String mensaje) {
        for (ObservadorCurso observador : observadores) {
            observador.actualizar(this, mensaje);
        }
    }

    @Override
    public String toString() {
        return "Curso{id=" + id + ", nombre='" + nombre + "', programa=" +
                (programa != null ? programa.getNombre() : "sin programa") +
                ", activo=" + activo + "}";
    }
}*/