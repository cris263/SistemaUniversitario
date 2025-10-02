package com.universidad.persistencia;

/**
 * Factory simple para crear DatabaseManager
 * Con cache para evitar reinicializaciones
 */
public class DatabaseFactory {

    // Cache de instancias inicializadas
    private static DatabaseManager cachedH2 = null;
    private static DatabaseManager cachedMySQL = null;
    private static DatabaseManager cachedOracle = null;

    // Instancia activa detectada automáticamente
    private static DatabaseManager activeDatabaseManager = null;
    private static DatabaseType currentDatabase = null;

    /**
     * Tipos de base de datos soportados
     */
    public enum DatabaseType {
        H2, MYSQL, ORACLE
    }

    /**
     * Crea o retorna instancia cacheada (Singleton por tipo)
     */
    public static DatabaseManager createDatabase(DatabaseType type) {
        switch (type) {
            case H2:
                if (cachedH2 == null) {
                    cachedH2 = new H2DatabaseManager();
                }
                activeDatabaseManager = cachedH2;
                currentDatabase = DatabaseType.H2;
                return cachedH2;

            case MYSQL:
                if (cachedMySQL == null) {
                    cachedMySQL = new MySQLDatabaseManager();
                }
                currentDatabase = DatabaseType.MYSQL;
                activeDatabaseManager = cachedMySQL;
                return cachedMySQL;

            case ORACLE:
                if (cachedOracle == null) {
                    cachedOracle = new OracleDatabaseManager();
                }
                currentDatabase = DatabaseType.ORACLE;
                activeDatabaseManager = cachedOracle;
                return cachedOracle;

            default:
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + type);
        }
    }

    public static DatabaseManager createDatabase(String typeName) {
        switch (typeName.toLowerCase()) {
            case "h2":
                return createDatabase(DatabaseType.H2);
            case "mysql":
                return createDatabase(DatabaseType.MYSQL);
            case "oracle":
                return createDatabase(DatabaseType.ORACLE);
            default:
                throw new IllegalArgumentException("Tipo de base de datos no reconocido: " + typeName);
        }
    }

    /**
     * Detecta qué base de datos está disponible (solo la primera vez)
     */
    public static DatabaseType detectActiveDatabase() {
        // Probar H2 primero (más rápido)
        return currentDatabase;
    }

    public static DatabaseManager getActiveDatabaseManager() {
        return activeDatabaseManager;
    }

    /**
     * Obtiene el nombre de la base de datos activa
     */
    public static String getActiveDatabaseName() {
        if (currentDatabase != null) {
            if (currentDatabase == DatabaseType.H2)
                return "H2";
            if (currentDatabase == DatabaseType.MYSQL)
                return "MYSQL";
            if (currentDatabase == DatabaseType.ORACLE)
                return "ORACLE";
        }
        return "NONE";
    }
}