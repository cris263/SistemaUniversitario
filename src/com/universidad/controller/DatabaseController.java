package com.universidad.controller;

import com.universidad.servicio.DatabaseService;

/**
 * Controlador simple para gestionar operaciones de base de datos
 * Solo dos métodos: cambiar BD y saber en cuál estamos
 */
public class DatabaseController {
    
    private final DatabaseService databaseService;
    
    /**
     * Constructor
     */
    public DatabaseController() {
        this.databaseService = new DatabaseService();
    }
    
    /**
     * Cambiar a una base de datos específica
     */
    public boolean cambiarBaseDatos(String nombreBaseDatos) {
        if (nombreBaseDatos == null || nombreBaseDatos.trim().isEmpty()) {
            return false;
        }
        
        return databaseService.cambiarBaseDatos(nombreBaseDatos.toLowerCase());
    }
    
    /**
     * Obtener el nombre de la base de datos actual
     */
    public String obtenerBaseDatosActual() {
        return databaseService.obtenerBaseDatosActual();
    }
}
