package com.universidad.ui;

import javax.swing.SwingUtilities;

public class InterfazEscritorio implements InterfazUsuario {
    private Menu menuPrincipal;
    
    @Override
    public void inicializar() {
        System.out.println(" Inicializando interfaz de escritorio...");
        SwingUtilities.invokeLater(() -> {
            menuPrincipal = new Menu();
            menuPrincipal.mainPanel();
            menuPrincipal.setTitle("Sistema Universitario - Modo Escritorio");
            menuPrincipal.setVisible(true);
        });
    }
    
    @Override
    public void mostrarMenu() {
        if (menuPrincipal != null) {
            SwingUtilities.invokeLater(() -> {
                menuPrincipal.setVisible(true);
                menuPrincipal.toFront();
            });
        }
    }
    
    @Override
    public void cerrar() {
        if (menuPrincipal != null) {
            SwingUtilities.invokeLater(() -> {
                menuPrincipal.dispose();
            });
        }
        System.out.println(" Interfaz de escritorio cerrada");
    }
    
    @Override
    public String getTipo() {
        return "ESCRITORIO";
    }
    
    @Override
    public void run() {
        System.out.println(" Hilo de interfaz de escritorio iniciado");
        inicializar();
        
        // Mantener el hilo vivo mientras la interfaz esté activa
        try {
            while (menuPrincipal == null || menuPrincipal.isDisplayable()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(" Hilo de escritorio interrumpido: " + e.getMessage());
        }
        
        System.out.println(" Hilo de interfaz de escritorio terminado");
    }
}