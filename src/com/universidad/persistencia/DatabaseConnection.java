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
       /*String createInscripcionTable = """
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

        // Nueva tabla de personas
        String createPersonaTable = """
            CREATE TABLE IF NOT EXISTS persona (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nombres VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL
            )
        """;*/
        String createPersonaTable = """
                CREATE TABLE IF NOT EXISTS persona (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombres VARCHAR(100) NOT NULL,
                    apellidos VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL
                )
            """;

        // Facultad
        String createFacultadTable = """
                CREATE TABLE IF NOT EXISTS facultad (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    decano BIGINT,
                    FOREIGN KEY (decano) REFERENCES persona(id)
                )
            """;

        // Programa
        String createProgramaTable = """
                CREATE TABLE IF NOT EXISTS programa (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    duracion DOUBLE,
                    registro DATE,
                    facultad BIGINT,
                    FOREIGN KEY (facultad) REFERENCES facultad(id)
                )
            """;

        // Curso
        String createCursoTable = """
                CREATE TABLE IF NOT EXISTS curso (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    programa BIGINT,
                    activo BOOLEAN,
                    FOREIGN KEY (programa) REFERENCES programa(id)
                )
            """;

        // Profesor (subclase de Persona)
        String createProfesorTable = """
                CREATE TABLE IF NOT EXISTS profesor (
                    id BIGINT PRIMARY KEY,
                    tipo_contrato VARCHAR(50),
                    FOREIGN KEY (id) REFERENCES persona(id)
                )
            """;

        // Estudiante (subclase de Persona)
        String createEstudianteTable = """
                CREATE TABLE IF NOT EXISTS estudiante (
                    id BIGINT PRIMARY KEY,
                    codigo BIGINT,
                    programa BIGINT,
                    activo BOOLEAN,
                    promedio DOUBLE,
                    FOREIGN KEY (id) REFERENCES persona(id),
                    FOREIGN KEY (programa) REFERENCES programa(id)
                )
            """;

        // CursoProfesor no verifica
        String createCursoProfesorTable = """
                CREATE TABLE IF NOT EXISTS curso_profesor (
                    profesor BIGINT,
                    curso BIGINT,
                    anio INT,
                    semestre INT,
                    PRIMARY KEY (profesor, curso, anio, semestre),
                    FOREIGN KEY (profesor) REFERENCES profesor(id),
                    FOREIGN KEY (curso) REFERENCES curso(id)
                )
            """;

        // Inscripcion
        String createInscripcionTable = """
                CREATE TABLE IF NOT EXISTS inscripcion (
                    curso BIGINT,
                    estudiante BIGINT,
                    anio INT,
                    semestre INT,
                    PRIMARY KEY (curso, estudiante, anio, semestre),
                    FOREIGN KEY (curso) REFERENCES curso(id),
                    FOREIGN KEY (estudiante) REFERENCES estudiante(id)
                )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createPersonaTable);
            stmt.execute(createFacultadTable);
            stmt.execute(createProgramaTable);
            stmt.execute(createCursoTable);
            stmt.execute(createProfesorTable);
            stmt.execute(createEstudianteTable);
            stmt.execute(createCursoProfesorTable);
            stmt.execute(createInscripcionTable);
        }

        System.out.println(" Base de datos inicializada correctamente (todas las tablas creadas)");


    } catch (SQLException e) {
        System.err.println("Error inicializando la base de datos: " + e.getMessage());
    }
}
}