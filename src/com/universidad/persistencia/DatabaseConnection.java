package com.universidad.persistencia;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:h2:mem:universidad;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
    
    public static void initializeDatabase() {
    try (Connection conn = getConnection()) {
        // Tabla de inscripciones (ya la tienes)
        String createInscripcionTable = """
            CREATE TABLE IF NOT EXISTS inscripcion (
                id INT AUTO_INCREMENT PRIMARY KEY,
                curso_id INT NOT NULL,
                curso_nombre VARCHAR(200) NOT NULL,
                anio INT NOT NULL,
                semestre INT NOT NULL,
                estudiante_id DOUBLE NOT NULL,
                estudiante_nombres VARCHAR(100) NOT NULL,
                estudiante_apellidos VARCHAR(100) NOT NULL,
                estudiante_email VARCHAR(100) NOT NULL,
                estudiante_codigo DOUBLE NOT NULL,
                estudiante_promedio DOUBLE NOT NULL,
                estudiante_activo BOOLEAN NOT NULL
            )
        """;

        // 🔹 Nueva tabla de personas
        String createPersonaTable = """
            CREATE TABLE IF NOT EXISTS persona (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nombres VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL
            )
        """;

        try (PreparedStatement stmt1 = conn.prepareStatement(createInscripcionTable);
             PreparedStatement stmt2 = conn.prepareStatement(createPersonaTable)) {
            stmt1.executeUpdate();
            stmt2.executeUpdate();
            System.out.println("Base de datos inicializada correctamente (tablas creadas)");
        }

    } catch (SQLException e) {
        System.err.println("Error inicializando la base de datos: " + e.getMessage());
    }
}
}