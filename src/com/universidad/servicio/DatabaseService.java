package com.universidad.servicio;

import com.universidad.persistencia.DatabaseFactory;
import com.universidad.persistencia.DatabaseManager;
import com.universidad.persistencia.DateDAO;

/**
 * Servicio simple para gestionar operaciones de base de datos
 * Ahora incluye consulta de fecha actual
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
    
    /**
     * Obtener la fecha actual de la base de datos
     * @return String con la fecha formateada como YYYY-MM-DD
     */
    public String obtenerFechaActual() {
        return dateDAO.getCurrentDate();
    }
}