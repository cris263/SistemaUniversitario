package com.universidad.ui.cursos;

import com.universidad.modelo.Curso;
import com.universidad.modelo.ObservadorCurso;

public class CursoConsolaObserver implements ObservadorCurso {

    private final String nombreObserver;

    public CursoConsolaObserver(String nombreObserver) {
        this.nombreObserver = nombreObserver;
    }

    @Override
    public void actualizar(Curso curso) {
        System.out.println("[" + nombreObserver + "] El curso cambió: " + curso);
    }
}