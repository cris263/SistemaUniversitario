package com.universidad.servicio;

import com.universidad.persistencia.DatabaseFactory;
import com.universidad.persistencia.DatabaseManager;

/**
 * Servicio simple para gestionar operaciones de base de datos
 * Solo dos métodos: cambiar BD y saber cuál estamos usando
 */
public class DatabaseService {
    
    private String currentDatabaseName;
    
    /**
     * Cambiar a una base de datos específica
     */
    public boolean cambiarBaseDatos(String nombreBaseDatos) {
        try {
            DatabaseManager db = DatabaseFactory.createDatabase(nombreBaseDatos);
            boolean success = db.initialize();
            
            if (success) {
                this.currentDatabaseName = nombreBaseDatos.toUpperCase();
                return true;
            }
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtener el nombre de la base de datos actual
     */
    public String obtenerBaseDatosActual() {
        return DatabaseFactory.getActiveDatabaseName();
    }
}