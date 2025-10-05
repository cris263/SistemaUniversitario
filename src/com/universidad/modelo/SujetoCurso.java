package com.universidad.modelo;

public interface SujetoCurso {
    void agregarObservador(ObservadorCurso observador);
    void eliminarObservador(ObservadorCurso observador);
    void notificarObservadores(String mensaje);

}
