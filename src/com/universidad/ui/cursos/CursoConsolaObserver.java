package com.universidad.ui.cursos;

public class CursoConsolaObserver implements UIObserverCurso {

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[Notificación Curso] " + mensaje);
    }
}
