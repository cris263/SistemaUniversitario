package com.universidad.persistencia;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz simple para gestores de base de datos
 * Solo los métodos más esenciales
 */
public interface DatabaseManager {
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection activa
     * @throws SQLException si hay error en la conexión
     */
    Connection getConnection() throws SQLException;
    
    /**
     * Crea las tablas necesarias si no existen
     * @return true si las tablas fueron creadas exitosamente
     */
    boolean createTables();
    
    /**
     * Llena las tablas con datos de prueba
     * @return true si los datos fueron insertados exitosamente
     */
    boolean insertTestData();
    
    /**
     * Inicializa todo: conecta, crea tablas, llena datos
     * @return true si la inicialización fue exitosa
     */
    boolean initialize();
    
    /**
     * Obtiene el nombre descriptivo de la base de datos
     * @return String con el nombre de la base de datos
     */
    String getName();
}