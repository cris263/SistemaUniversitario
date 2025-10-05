package com.universidad.ui.cursos;

import com.universidad.modelo.ObservadorCurso;

public class CursoConsolaObserver implements ObservadorCurso {

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[Notificación Curso] " + mensaje);
    }
}
