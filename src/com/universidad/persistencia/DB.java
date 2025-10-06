package com.universidad.persistencia;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para manejar conexiones a base de datos de forma dinámica
 * Usa db.properties para cambiar entre H2, MySQL y Oracle
 * Solo necesitas sobrescribir las propiedades db.* en el archivo
 */
public class DB {
    
    private static final String PROPERTIES_FILE = "src/db.properties";
    private static Properties properties;
    private static Connection connection;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
            System.out.println("✅ Propiedades de DB cargadas correctamente");
        } catch (IOException e) {
            System.err.println("❌ Error al cargar db.properties: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");
            
            // Validar que las propiedades estén configuradas
            if (url == null || url.trim().isEmpty()) {
                throw new SQLException("⚠️ db.url no está configurado. Use setDatabase() primero.");
            }
            
            // Cargar el driver
            Class.forName(driver);
            
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conectado a la base de datos");
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ Driver no encontrado: " + e.getMessage());
        }
    }
    
    /**
     * Cambiar a base de datos H2
     * Sobrescribe las propiedades db.* con las de h2.*
     */
    public static void setH2() {
        setDatabase("h2");
    }
    
    /**
     * Cambiar a base de datos MySQL
     * Sobrescribe las propiedades db.* con las de mysql.*
     */
    public static void setMySQL() {
        setDatabase("mysql");
    }
    
    /**
     * Cambiar a base de datos Oracle
     * Sobrescribe las propiedades db.* con las de oracle.*
     */
    public static void setOracle() {
        setDatabase("oracle");
    }
    
    public static void setDatabase(String dbType) {
        if (!dbType.equals("h2") && !dbType.equals("mysql") && !dbType.equals("oracle")) {
            System.err.println("❌ Tipo de base de datos no válido: " + dbType);
            return;
        }
        
        try {
            Properties props = new Properties();
            try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
                props.load(input);
            }
            
            props.setProperty("db.url", props.getProperty(dbType + ".url"));
            props.setProperty("db.user", props.getProperty(dbType + ".user"));
            props.setProperty("db.password", props.getProperty(dbType + ".password"));
            props.setProperty("db.driver", props.getProperty(dbType + ".driver"));
            
            try (OutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
                props.store(output, "Database Configuration - Active DB: " + dbType.toUpperCase());
            }
            
            properties = props;
            
            System.out.println("🔄 Base de datos cambiada a: " + dbType.toUpperCase());
            System.out.println("   URL: " + props.getProperty("db.url"));
            
        } catch (IOException e) {
            System.err.println("❌ Error al actualizar db.properties: " + e.getMessage());
        }
    }
    
    /**
     * Obtener el tipo de base de datos activa
     */
    public static String getActiveDatabaseType() {
        String url = properties.getProperty("db.url", "");
        
        if (url.contains("h2")) return "H2";
        if (url.contains("mysql")) return "MySQL";
        if (url.contains("oracle")) return "Oracle";
        
        return "H2";
    }

}
