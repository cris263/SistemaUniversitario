package com.universidad.ui.cursos;

import com.universidad.modelo.ObservadorCurso;

public class CursoUIAdapter implements ObservadorCurso {

    private UIObserverCurso uiObserver;

    public CursoUIAdapter(UIObserverCurso uiObserver) {
        this.uiObserver = uiObserver;
    }

    @Override
    public void actualizar(String mensaje) {
        uiObserver.actualizar(mensaje);
    }
}
