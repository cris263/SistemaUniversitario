package com.universidad.ui;

/**
 * Interfaz común para diferentes tipos de interfaces de usuario
 */
public interface InterfazUsuario extends Runnable {
    void inicializar();
    void mostrarMenu();
    void cerrar();
    String getTipo();
}