package com.universidad.modelo;

import java.util.ArrayList;
import java.util.List;

public class Curso implements SujetoCurso {
    private int id;
    private String nombre;
    private Programa programa;
    private boolean activo;

    private List<ObservadorCurso> observadores = new ArrayList<>();

    public Curso(int id, String nombre, Programa programa, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Programa getPrograma() { return programa; }
    public boolean isActivo() { return activo; }

    public void setActivo(boolean activo) {
        this.activo = activo;
        notificarObservadores("El curso cambió su estado a " + (activo ? "ACTIVO" : "INACTIVO"));
    }

    @Override
    public void agregarObservador(ObservadorCurso observador) {
        observadores.add(observador);
    }

    @Override
    public void eliminarObservador(ObservadorCurso observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(String mensaje) {
        for (ObservadorCurso obs : observadores) {
            obs.actualizar(this);
        }
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}
