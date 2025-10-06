package com.universidad.servicio;

import com.universidad.modelo.ObservadorCurso;

public interface ObservableCurso {
    void attach(ObservadorCurso obs);
    void detach(ObservadorCurso obs);
    void notificar(String mensaje);
}
