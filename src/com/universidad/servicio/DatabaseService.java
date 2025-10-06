package com.universidad.servicio;

import com.universidad.persistencia.DB;
import com.universidad.persistencia.DatabaseInitializer;
import com.universidad.persistencia.DateDAO;

/**
 * Servicio para gestionar operaciones de base de datos
 * Usa DB.java para conexiones y DatabaseInitializer para inicialización
 */
public class DatabaseService {
    
    private String currentDatabaseName;
    private DateDAO dateDAO;
    
    /**
     * Constructor
     */
    public DatabaseService(DateDAO dateDAO) {
        this.dateDAO = dateDAO;
    }
    
    /**
     * Cambiar a una base de datos específica e inicializarla
     */
    public boolean cambiarBaseDatos(String nombreBaseDatos) {
        try {
            // Cambiar la base de datos activa
            if (nombreBaseDatos.equalsIgnoreCase("mysql")) {
                DB.setMySQL();
            } else if (nombreBaseDatos.equalsIgnoreCase("h2")) {
                DB.setH2();
            } else if (nombreBaseDatos.equalsIgnoreCase("oracle")) {
                DB.setOracle();
            } else {
                System.err.println("❌ Base de datos no soportada: " + nombreBaseDatos);
                return false;
            }
            
            // Inicializar tablas y datos
            boolean initialized = DatabaseInitializer.initialize();
            
            if (initialized) {
                this.currentDatabaseName = DB.getActiveDatabaseType();
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error cambiando base de datos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtener el nombre de la base de datos actual
     */
    public String obtenerBaseDatosActual() {
        if (currentDatabaseName == null) {
            currentDatabaseName = DB.getActiveDatabaseType();
        }
        return currentDatabaseName;
    }
    
    public String obtenerFechaActual() {
        return dateDAO.getCurrentDate();
    }
}