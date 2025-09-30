package com.universidad.persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase utilitaria para cargar la configuración de bases de datos
 * desde el archivo database.properties
 */
public class DatabaseConfig {
    
    private static Properties properties;
    private static final String CONFIG_FILE = "/database.properties";
    
    static {
        loadProperties();
    }
    /**
     * Cargar propiedades desde el archivo
     */
    private static void loadProperties() {
        properties = new Properties();
        
        try (InputStream inputStream = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                // Si no está en el classpath, intentar desde la raíz del proyecto
                try (InputStream fileStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
                    if (fileStream != null) {
                        properties.load(fileStream);
                        System.out.println("✅ Configuración cargada desde classpath");
                    }
                }
            } else {
                properties.load(inputStream);
                System.out.println("✅ Configuración cargada desde resources");
            }
        } catch (IOException e) {
            System.err.println("❌ Error cargando database.properties: " + e.getMessage());
        }
    }
    
    /**
     * Obtener propiedad por clave
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    // Métodos de conveniencia para H2
    public static String getH2Url() { return getProperty("h2.url"); }
    public static String getH2User() { return getProperty("h2.user"); }
    public static String getH2Password() { return getProperty("h2.password"); }
    public static String getH2Driver() { return getProperty("h2.driver"); }
    
    // Métodos de conveniencia para MySQL
    public static String getMySQLUrl() { return getProperty("mysql.url"); }
    public static String getMySQLUser() { return getProperty("mysql.user"); }
    public static String getMySQLPassword() { return getProperty("mysql.password"); }
    public static String getMySQLDriver() { return getProperty("mysql.driver"); }
    
    // Métodos de conveniencia para Oracle
    public static String getOracleUrl() { return getProperty("oracle.url"); }
    public static String getOracleUser() { return getProperty("oracle.user"); }
    public static String getOraclePassword() { return getProperty("oracle.password"); }
    public static String getOracleDriver() { return getProperty("oracle.driver"); }
    
}