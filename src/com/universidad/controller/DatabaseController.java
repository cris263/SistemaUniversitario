package com.universidad.controller;

import com.universidad.servicio.DatabaseService;

/**
 * Controlador para gestionar operaciones de base de datos
 * Incluye consulta de fecha a través de la cadena de servicios
 */
public class DatabaseController {
    
    private final DatabaseService databaseService;
    
    /**
     * Constructor
     */
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
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
    
    /**
     * Obtener la fecha actual según la base de datos activa
     * Este método consulta al DatabaseService, que a su vez consulta al DateDAO
     * @return String con la fecha formateada como YYYY-MM-DD
     */
    public String obtenerFechaActual() {
        return databaseService.obtenerFechaActual();
    }
}
