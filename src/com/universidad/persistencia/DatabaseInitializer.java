package com.universidad.persistencia;

import java.sql.*;

/**
 * Clase para inicializar tablas y datos de prueba
 * Usa DB.java para conexiones y detecta automáticamente la base de datos activa
 */
public class DatabaseInitializer {
    
    /**
     * Inicializa la base de datos activa (crea tablas e inserta datos)
     */
    public static boolean initialize() {
        System.out.println("🔄 Inicializando base de datos...");
        
        String dbType = DB.getActiveDatabaseType();
        System.out.println("📊 Base de datos activa: " + dbType);
        
        if (dbType.equals("Ninguna")) {
            System.err.println("❌ No hay base de datos configurada");
            return false;
        }
        
        if (!createTables()) {
            return false;
        }
        
        if (!insertTestData()) {
            return false;
        }
        
        System.out.println("✅ Base de datos inicializada correctamente");
        return true;
    }
    
    /**
     * Crea las tablas según la base de datos activa
     */
    public static boolean createTables() {
        String dbType = DB.getActiveDatabaseType();
        
        try (Connection conn = DB.getConnection()) {
            System.out.println("📝 Creando tablas para " + dbType + "...");
            
            switch (dbType) {
                case "H2":
                case "MySQL":
                    return createTablesH2MySQL(conn);
                case "Oracle":
                    return createTablesOracle(conn);
                default:
                    System.err.println("❌ Base de datos no soportada: " + dbType);
                    return false;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error creando tablas: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Crea tablas para H2 y MySQL (sintaxis similar)
     */
    private static boolean createTablesH2MySQL(Connection conn) throws SQLException {
        String createPersonaTable = """
                CREATE TABLE IF NOT EXISTS persona (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombres VARCHAR(100) NOT NULL,
                    apellidos VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL
                )
            """;

        String createFacultadTable = """
                CREATE TABLE IF NOT EXISTS facultad (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    decano BIGINT,
                    FOREIGN KEY (decano) REFERENCES persona(id)
                )
            """;

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

        String createCursoTable = """
                CREATE TABLE IF NOT EXISTS curso (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    programa BIGINT,
                    activo BOOLEAN,
                    FOREIGN KEY (programa) REFERENCES programa(id)
                )
            """;

        String createProfesorTable = """
                CREATE TABLE IF NOT EXISTS profesor (
                    id BIGINT PRIMARY KEY,
                    tipo_contrato VARCHAR(50),
                    FOREIGN KEY (id) REFERENCES persona(id)
                )
            """;

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

        System.out.println("✅ Tablas creadas correctamente");
        return true;
    }
    
    /**
     * Crea tablas para Oracle (sintaxis específica)
     */
    private static boolean createTablesOracle(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Crear secuencias
            createSequenceIfNotExists(stmt, "persona_seq", "CREATE SEQUENCE persona_seq START WITH 1 INCREMENT BY 1");
            createSequenceIfNotExists(stmt, "facultad_seq", "CREATE SEQUENCE facultad_seq START WITH 1 INCREMENT BY 1");
            createSequenceIfNotExists(stmt, "programa_seq", "CREATE SEQUENCE programa_seq START WITH 1 INCREMENT BY 1");
            createSequenceIfNotExists(stmt, "curso_seq", "CREATE SEQUENCE curso_seq START WITH 1 INCREMENT BY 1");
            
            // Crear tablas
            String createPersonaTable = """
                    CREATE TABLE persona (
                        id NUMBER PRIMARY KEY,
                        nombres VARCHAR2(100) NOT NULL,
                        apellidos VARCHAR2(100) NOT NULL,
                        email VARCHAR2(100) NOT NULL
                    )
                """;

            String createFacultadTable = """
                    CREATE TABLE facultad (
                        id NUMBER PRIMARY KEY,
                        nombre VARCHAR2(100) NOT NULL,
                        decano NUMBER,
                        FOREIGN KEY (decano) REFERENCES persona(id)
                    )
                """;

            String createProgramaTable = """
                    CREATE TABLE programa (
                        id NUMBER PRIMARY KEY,
                        nombre VARCHAR2(100) NOT NULL,
                        duracion NUMBER,
                        registro DATE,
                        facultad NUMBER,
                        FOREIGN KEY (facultad) REFERENCES facultad(id)
                    )
                """;

            String createCursoTable = """
                    CREATE TABLE curso (
                        id NUMBER PRIMARY KEY,
                        nombre VARCHAR2(100) NOT NULL,
                        programa NUMBER,
                        activo NUMBER(1),
                        FOREIGN KEY (programa) REFERENCES programa(id)
                    )
                """;

            String createProfesorTable = """
                    CREATE TABLE profesor (
                        id NUMBER PRIMARY KEY,
                        tipo_contrato VARCHAR2(50),
                        FOREIGN KEY (id) REFERENCES persona(id)
                    )
                """;

            String createEstudianteTable = """
                    CREATE TABLE estudiante (
                        id NUMBER PRIMARY KEY,
                        codigo NUMBER,
                        programa NUMBER,
                        activo NUMBER(1),
                        promedio NUMBER,
                        FOREIGN KEY (id) REFERENCES persona(id),
                        FOREIGN KEY (programa) REFERENCES programa(id)
                    )
                """;

            String createCursoProfesorTable = """
                    CREATE TABLE curso_profesor (
                        profesor NUMBER,
                        curso NUMBER,
                        anio NUMBER,
                        semestre NUMBER,
                        PRIMARY KEY (profesor, curso, anio, semestre),
                        FOREIGN KEY (profesor) REFERENCES profesor(id),
                        FOREIGN KEY (curso) REFERENCES curso(id)
                    )
                """;

            String createInscripcionTable = """
                    CREATE TABLE inscripcion (
                        curso NUMBER,
                        estudiante NUMBER,
                        anio NUMBER,
                        semestre NUMBER,
                        PRIMARY KEY (curso, estudiante, anio, semestre),
                        FOREIGN KEY (curso) REFERENCES curso(id),
                        FOREIGN KEY (estudiante) REFERENCES estudiante(id)
                    )
                """;

            createTableIfNotExists(stmt, "persona", createPersonaTable);
            createTableIfNotExists(stmt, "facultad", createFacultadTable);
            createTableIfNotExists(stmt, "programa", createProgramaTable);
            createTableIfNotExists(stmt, "curso", createCursoTable);
            createTableIfNotExists(stmt, "profesor", createProfesorTable);
            createTableIfNotExists(stmt, "estudiante", createEstudianteTable);
            createTableIfNotExists(stmt, "curso_profesor", createCursoProfesorTable);
            createTableIfNotExists(stmt, "inscripcion", createInscripcionTable);
        }

        System.out.println("✅ Tablas Oracle creadas correctamente");
        return true;
    }
    
    /**
     * Inserta datos de prueba según la base de datos activa
     */
    public static boolean insertTestData() {
        String dbType = DB.getActiveDatabaseType();
        
        try (Connection conn = DB.getConnection()) {
            // Verificar si ya hay datos
            PreparedStatement checkData = conn.prepareStatement("SELECT COUNT(*) FROM persona");
            ResultSet rs = checkData.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("ℹ️ Los datos de prueba ya existen.");
                return true;
            }
            
            System.out.println("📝 Insertando datos de prueba para " + dbType + "...");
            
            conn.setAutoCommit(false);
            
            boolean success;
            if (dbType.equals("Oracle")) {
                success = insertTestDataOracle(conn);
            } else {
                success = insertTestDataH2MySQL(conn);
            }
            
            if (success) {
                conn.commit();
                System.out.println("✅ Datos de prueba insertados correctamente:");
                System.out.println("   - 18 personas (8 profesores + 10 estudiantes)");
                System.out.println("   - 4 facultades con decanos asignados");
                System.out.println("   - 6 programas académicos");
                System.out.println("   - 12 cursos activos");
                System.out.println("   - 8 profesores con diferentes tipos de contrato");
                System.out.println("   - 10 estudiantes activos con promedios");
                System.out.println("   - 6 asignaciones profesor-curso");
                System.out.println("   - 10 inscripciones de estudiantes");
            } else {
                conn.rollback();
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("❌ Error insertando datos de prueba: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Inserta datos para H2 y MySQL
     */
    private static boolean insertTestDataH2MySQL(Connection conn) throws SQLException {
        // 1. Insertar Personas
        String insertPersonas = "INSERT INTO persona (nombres, apellidos, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertPersonas)) {
            stmt.setString(1, "Carlos Alberto"); stmt.setString(2, "González Pérez"); stmt.setString(3, "carlos.gonzalez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "María Elena"); stmt.setString(2, "Rodríguez López"); stmt.setString(3, "maria.rodriguez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "José Luis"); stmt.setString(2, "Martínez Silva"); stmt.setString(3, "jose.martinez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Ana Patricia"); stmt.setString(2, "Fernández Torres"); stmt.setString(3, "ana.fernandez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Roberto"); stmt.setString(2, "Jiménez Castro"); stmt.setString(3, "roberto.jimenez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Laura Cristina"); stmt.setString(2, "Vargas Herrera"); stmt.setString(3, "laura.vargas@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Miguel Ángel"); stmt.setString(2, "Ramírez Gómez"); stmt.setString(3, "miguel.ramirez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Diana Carolina"); stmt.setString(2, "Morales Díaz"); stmt.setString(3, "diana.morales@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Andrés Felipe"); stmt.setString(2, "Sánchez Ruiz"); stmt.setString(3, "andres.sanchez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Camila"); stmt.setString(2, "Torres Medina"); stmt.setString(3, "camila.torres@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Daniel"); stmt.setString(2, "Ospina Ríos"); stmt.setString(3, "daniel.ospina@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Valentina"); stmt.setString(2, "Castro Mejía"); stmt.setString(3, "valentina.castro@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Santiago"); stmt.setString(2, "Herrera Vega"); stmt.setString(3, "santiago.herrera@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Isabella"); stmt.setString(2, "Ramírez Cortés"); stmt.setString(3, "isabella.ramirez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Juan Pablo"); stmt.setString(2, "Mendoza León"); stmt.setString(3, "juan.mendoza@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Sofía"); stmt.setString(2, "Gutiérrez Rojas"); stmt.setString(3, "sofia.gutierrez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Mateo"); stmt.setString(2, "Pérez Cardona"); stmt.setString(3, "mateo.perez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Alejandra"); stmt.setString(2, "Muñoz Aguilar"); stmt.setString(3, "alejandra.munoz@estudiante.edu"); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 2. Insertar Facultades
        String insertFacultades = "INSERT INTO facultad (nombre, decano) VALUES (?, NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(insertFacultades)) {
            stmt.setString(1, "Facultad de Ingeniería"); stmt.addBatch();
            stmt.setString(1, "Facultad de Ciencias Económicas"); stmt.addBatch();
            stmt.setString(1, "Facultad de Medicina"); stmt.addBatch();
            stmt.setString(1, "Facultad de Derecho"); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 3. Actualizar decanos
        String updateDecanos = "UPDATE facultad SET decano = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateDecanos)) {
            stmt.setLong(1, 1); stmt.setLong(2, 1); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setLong(2, 2); stmt.addBatch();
            stmt.setLong(1, 3); stmt.setLong(2, 3); stmt.addBatch();
            stmt.setLong(1, 4); stmt.setLong(2, 4); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 4. Insertar Programas
        String insertProgramas = "INSERT INTO programa (nombre, duracion, registro, facultad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertProgramas)) {
            stmt.setString(1, "Ingeniería de Sistemas"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2020-01-15")); stmt.setLong(4, 1); stmt.addBatch();
            stmt.setString(1, "Ingeniería Civil"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2019-08-20")); stmt.setLong(4, 1); stmt.addBatch();
            stmt.setString(1, "Administración de Empresas"); stmt.setDouble(2, 4.0); stmt.setDate(3, Date.valueOf("2018-02-10")); stmt.setLong(4, 2); stmt.addBatch();
            stmt.setString(1, "Contaduría Pública"); stmt.setDouble(2, 4.5); stmt.setDate(3, Date.valueOf("2021-01-05")); stmt.setLong(4, 2); stmt.addBatch();
            stmt.setString(1, "Medicina"); stmt.setDouble(2, 6.0); stmt.setDate(3, Date.valueOf("2017-07-15")); stmt.setLong(4, 3); stmt.addBatch();
            stmt.setString(1, "Derecho"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2016-09-01")); stmt.setLong(4, 4); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 5. Insertar Cursos
        String insertCursos = "INSERT INTO curso (nombre, programa, activo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertCursos)) {
            stmt.setString(1, "Programación I"); stmt.setLong(2, 1); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Bases de Datos"); stmt.setLong(2, 1); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Estructuras de Datos"); stmt.setLong(2, 1); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Mecánica de Fluidos"); stmt.setLong(2, 2); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Resistencia de Materiales"); stmt.setLong(2, 2); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Contabilidad General"); stmt.setLong(2, 3); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Marketing Estratégico"); stmt.setLong(2, 3); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Auditoría"); stmt.setLong(2, 4); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Anatomía Humana"); stmt.setLong(2, 5); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Fisiología"); stmt.setLong(2, 5); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Derecho Constitucional"); stmt.setLong(2, 6); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.setString(1, "Derecho Civil"); stmt.setLong(2, 6); stmt.setBoolean(3, true); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 6. Insertar Profesores
        String insertProfesores = "INSERT INTO profesor (id, tipo_contrato) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertProfesores)) {
            stmt.setLong(1, 1); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 3); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 4); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 5); stmt.setString(2, "Medio Tiempo"); stmt.addBatch();
            stmt.setLong(1, 6); stmt.setString(2, "Medio Tiempo"); stmt.addBatch();
            stmt.setLong(1, 7); stmt.setString(2, "Cátedra"); stmt.addBatch();
            stmt.setLong(1, 8); stmt.setString(2, "Cátedra"); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 7. Insertar Estudiantes
        String insertEstudiantes = "INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertEstudiantes)) {
            stmt.setLong(1, 9); stmt.setDouble(2, 202001); stmt.setLong(3, 1); stmt.setBoolean(4, true); stmt.setDouble(5, 4.2); stmt.addBatch();
            stmt.setLong(1, 10); stmt.setDouble(2, 202002); stmt.setLong(3, 1); stmt.setBoolean(4, true); stmt.setDouble(5, 3.8); stmt.addBatch();
            stmt.setLong(1, 11); stmt.setDouble(2, 202003); stmt.setLong(3, 2); stmt.setBoolean(4, true); stmt.setDouble(5, 4.5); stmt.addBatch();
            stmt.setLong(1, 12); stmt.setDouble(2, 202004); stmt.setLong(3, 3); stmt.setBoolean(4, true); stmt.setDouble(5, 3.9); stmt.addBatch();
            stmt.setLong(1, 13); stmt.setDouble(2, 202005); stmt.setLong(3, 3); stmt.setBoolean(4, true); stmt.setDouble(5, 4.1); stmt.addBatch();
            stmt.setLong(1, 14); stmt.setDouble(2, 202006); stmt.setLong(3, 4); stmt.setBoolean(4, true); stmt.setDouble(5, 4.3); stmt.addBatch();
            stmt.setLong(1, 15); stmt.setDouble(2, 202007); stmt.setLong(3, 5); stmt.setBoolean(4, true); stmt.setDouble(5, 4.7); stmt.addBatch();
            stmt.setLong(1, 16); stmt.setDouble(2, 202008); stmt.setLong(3, 5); stmt.setBoolean(4, true); stmt.setDouble(5, 4.0); stmt.addBatch();
            stmt.setLong(1, 17); stmt.setDouble(2, 202009); stmt.setLong(3, 6); stmt.setBoolean(4, true); stmt.setDouble(5, 3.7); stmt.addBatch();
            stmt.setLong(1, 18); stmt.setDouble(2, 202010); stmt.setLong(3, 6); stmt.setBoolean(4, true); stmt.setDouble(5, 4.4); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 8. Insertar Curso-Profesor
        String insertCursoProfesor = "INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertCursoProfesor)) {
            stmt.setLong(1, 1); stmt.setLong(2, 1); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setLong(2, 2); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 5); stmt.setLong(2, 6); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 6); stmt.setLong(2, 7); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 7); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 8); stmt.setLong(2, 11); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 9. Insertar Inscripciones
        String insertInscripciones = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertInscripciones)) {
            stmt.setLong(1, 1); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 1); stmt.setLong(2, 10); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 4); stmt.setLong(2, 11); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 6); stmt.setLong(2, 12); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 7); stmt.setLong(2, 13); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 8); stmt.setLong(2, 14); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 9); stmt.setLong(2, 15); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 10); stmt.setLong(2, 16); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 11); stmt.setLong(2, 17); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.executeBatch();
        }
        
        return true;
    }
    
    /**
     * Inserta datos para Oracle (usa secuencias)
     */
    private static boolean insertTestDataOracle(Connection conn) throws SQLException {
        // 1. Insertar Personas
        String insertPersonas = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (persona_seq.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertPersonas)) {
            stmt.setString(1, "Carlos Alberto"); stmt.setString(2, "González Pérez"); stmt.setString(3, "carlos.gonzalez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "María Elena"); stmt.setString(2, "Rodríguez López"); stmt.setString(3, "maria.rodriguez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "José Luis"); stmt.setString(2, "Martínez Silva"); stmt.setString(3, "jose.martinez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Ana Patricia"); stmt.setString(2, "Fernández Torres"); stmt.setString(3, "ana.fernandez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Roberto"); stmt.setString(2, "Jiménez Castro"); stmt.setString(3, "roberto.jimenez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Laura Cristina"); stmt.setString(2, "Vargas Herrera"); stmt.setString(3, "laura.vargas@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Miguel Ángel"); stmt.setString(2, "Ramírez Gómez"); stmt.setString(3, "miguel.ramirez@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Diana Carolina"); stmt.setString(2, "Morales Díaz"); stmt.setString(3, "diana.morales@universidad.edu"); stmt.addBatch();
            stmt.setString(1, "Andrés Felipe"); stmt.setString(2, "Sánchez Ruiz"); stmt.setString(3, "andres.sanchez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Camila"); stmt.setString(2, "Torres Medina"); stmt.setString(3, "camila.torres@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Daniel"); stmt.setString(2, "Ospina Ríos"); stmt.setString(3, "daniel.ospina@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Valentina"); stmt.setString(2, "Castro Mejía"); stmt.setString(3, "valentina.castro@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Santiago"); stmt.setString(2, "Herrera Vega"); stmt.setString(3, "santiago.herrera@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Isabella"); stmt.setString(2, "Ramírez Cortés"); stmt.setString(3, "isabella.ramirez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Juan Pablo"); stmt.setString(2, "Mendoza León"); stmt.setString(3, "juan.mendoza@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Sofía"); stmt.setString(2, "Gutiérrez Rojas"); stmt.setString(3, "sofia.gutierrez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Mateo"); stmt.setString(2, "Pérez Cardona"); stmt.setString(3, "mateo.perez@estudiante.edu"); stmt.addBatch();
            stmt.setString(1, "Alejandra"); stmt.setString(2, "Muñoz Aguilar"); stmt.setString(3, "alejandra.munoz@estudiante.edu"); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 2. Insertar Facultades
        String insertFacultades = "INSERT INTO facultad (id, nombre, decano) VALUES (facultad_seq.NEXTVAL, ?, NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(insertFacultades)) {
            stmt.setString(1, "Facultad de Ingeniería"); stmt.addBatch();
            stmt.setString(1, "Facultad de Ciencias Económicas"); stmt.addBatch();
            stmt.setString(1, "Facultad de Medicina"); stmt.addBatch();
            stmt.setString(1, "Facultad de Derecho"); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 3. Actualizar decanos
        String updateDecanos = "UPDATE facultad SET decano = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateDecanos)) {
            stmt.setLong(1, 1); stmt.setLong(2, 1); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setLong(2, 2); stmt.addBatch();
            stmt.setLong(1, 3); stmt.setLong(2, 3); stmt.addBatch();
            stmt.setLong(1, 4); stmt.setLong(2, 4); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 4. Insertar Programas
        String insertProgramas = "INSERT INTO programa (id, nombre, duracion, registro, facultad) VALUES (programa_seq.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertProgramas)) {
            stmt.setString(1, "Ingeniería de Sistemas"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2020-01-15")); stmt.setLong(4, 1); stmt.addBatch();
            stmt.setString(1, "Ingeniería Civil"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2019-08-20")); stmt.setLong(4, 1); stmt.addBatch();
            stmt.setString(1, "Administración de Empresas"); stmt.setDouble(2, 4.0); stmt.setDate(3, Date.valueOf("2018-02-10")); stmt.setLong(4, 2); stmt.addBatch();
            stmt.setString(1, "Contaduría Pública"); stmt.setDouble(2, 4.5); stmt.setDate(3, Date.valueOf("2021-01-05")); stmt.setLong(4, 2); stmt.addBatch();
            stmt.setString(1, "Medicina"); stmt.setDouble(2, 6.0); stmt.setDate(3, Date.valueOf("2017-07-15")); stmt.setLong(4, 3); stmt.addBatch();
            stmt.setString(1, "Derecho"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2016-09-01")); stmt.setLong(4, 4); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 5. Insertar Cursos (Oracle usa 1/0 para boolean)
        String insertCursos = "INSERT INTO curso (id, nombre, programa, activo) VALUES (curso_seq.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertCursos)) {
            stmt.setString(1, "Programación I"); stmt.setLong(2, 1); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Bases de Datos"); stmt.setLong(2, 1); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Estructuras de Datos"); stmt.setLong(2, 1); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Mecánica de Fluidos"); stmt.setLong(2, 2); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Resistencia de Materiales"); stmt.setLong(2, 2); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Contabilidad General"); stmt.setLong(2, 3); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Marketing Estratégico"); stmt.setLong(2, 3); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Auditoría"); stmt.setLong(2, 4); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Anatomía Humana"); stmt.setLong(2, 5); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Fisiología"); stmt.setLong(2, 5); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Derecho Constitucional"); stmt.setLong(2, 6); stmt.setInt(3, 1); stmt.addBatch();
            stmt.setString(1, "Derecho Civil"); stmt.setLong(2, 6); stmt.setInt(3, 1); stmt.addBatch();
            stmt.executeBatch();
        }
        
        // 6-9. Resto de inserts igual que H2/MySQL pero usando 1/0 para booleans
        String insertProfesores = "INSERT INTO profesor (id, tipo_contrato) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertProfesores)) {
            stmt.setLong(1, 1); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 3); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 4); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
            stmt.setLong(1, 5); stmt.setString(2, "Medio Tiempo"); stmt.addBatch();
            stmt.setLong(1, 6); stmt.setString(2, "Medio Tiempo"); stmt.addBatch();
            stmt.setLong(1, 7); stmt.setString(2, "Cátedra"); stmt.addBatch();
            stmt.setLong(1, 8); stmt.setString(2, "Cátedra"); stmt.addBatch();
            stmt.executeBatch();
        }
        
        String insertEstudiantes = "INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertEstudiantes)) {
            stmt.setLong(1, 9); stmt.setDouble(2, 202001); stmt.setLong(3, 1); stmt.setInt(4, 1); stmt.setDouble(5, 4.2); stmt.addBatch();
            stmt.setLong(1, 10); stmt.setDouble(2, 202002); stmt.setLong(3, 1); stmt.setInt(4, 1); stmt.setDouble(5, 3.8); stmt.addBatch();
            stmt.setLong(1, 11); stmt.setDouble(2, 202003); stmt.setLong(3, 2); stmt.setInt(4, 1); stmt.setDouble(5, 4.5); stmt.addBatch();
            stmt.setLong(1, 12); stmt.setDouble(2, 202004); stmt.setLong(3, 3); stmt.setInt(4, 1); stmt.setDouble(5, 3.9); stmt.addBatch();
            stmt.setLong(1, 13); stmt.setDouble(2, 202005); stmt.setLong(3, 3); stmt.setInt(4, 1); stmt.setDouble(5, 4.1); stmt.addBatch();
            stmt.setLong(1, 14); stmt.setDouble(2, 202006); stmt.setLong(3, 4); stmt.setInt(4, 1); stmt.setDouble(5, 4.3); stmt.addBatch();
            stmt.setLong(1, 15); stmt.setDouble(2, 202007); stmt.setLong(3, 5); stmt.setInt(4, 1); stmt.setDouble(5, 4.7); stmt.addBatch();
            stmt.setLong(1, 16); stmt.setDouble(2, 202008); stmt.setLong(3, 5); stmt.setInt(4, 1); stmt.setDouble(5, 4.0); stmt.addBatch();
            stmt.setLong(1, 17); stmt.setDouble(2, 202009); stmt.setLong(3, 6); stmt.setInt(4, 1); stmt.setDouble(5, 3.7); stmt.addBatch();
            stmt.setLong(1, 18); stmt.setDouble(2, 202010); stmt.setLong(3, 6); stmt.setInt(4, 1); stmt.setDouble(5, 4.4); stmt.addBatch();
            stmt.executeBatch();
        }
        
        String insertCursoProfesor = "INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertCursoProfesor)) {
            stmt.setLong(1, 1); stmt.setLong(2, 1); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setLong(2, 2); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 5); stmt.setLong(2, 6); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 6); stmt.setLong(2, 7); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 7); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 8); stmt.setLong(2, 11); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.executeBatch();
        }
        
        String insertInscripciones = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertInscripciones)) {
            stmt.setLong(1, 1); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 2); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 1); stmt.setLong(2, 10); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 4); stmt.setLong(2, 11); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 6); stmt.setLong(2, 12); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 7); stmt.setLong(2, 13); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 8); stmt.setLong(2, 14); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 9); stmt.setLong(2, 15); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 10); stmt.setLong(2, 16); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.setLong(1, 11); stmt.setLong(2, 17); stmt.setInt(3, 2024); stmt.setInt(4, 1); stmt.addBatch();
            stmt.executeBatch();
        }
        
        return true;
    }
    
    // ===== Métodos auxiliares para Oracle =====
    
    private static void createSequenceIfNotExists(Statement stmt, String sequenceName, String createSQL) {
        try (ResultSet rs = stmt.executeQuery("SELECT sequence_name FROM user_sequences WHERE sequence_name = '"
                + sequenceName.toUpperCase() + "'")) {
            if (rs.next()) {
                System.out.println("ℹ️ Secuencia " + sequenceName + " ya existe");
                return;
            }
        } catch (SQLException e) {
            // Ignorar error
        }

        try {
            stmt.execute(createSQL);
            System.out.println("✅ Secuencia " + sequenceName + " creada");
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) {
                System.out.println("ℹ️ Secuencia " + sequenceName + " ya existía");
            } else {
                System.err.println("⚠️ Error creando secuencia " + sequenceName + ": " + e.getMessage());
            }
        }
    }

    private static void createTableIfNotExists(Statement stmt, String tableName, String createSQL) throws SQLException {
        try {
            stmt.execute(createSQL);
            System.out.println("✅ Tabla " + tableName + " creada");
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) {
                System.out.println("ℹ️ Tabla " + tableName + " ya existía");
            } else {
                throw e;
            }
        }
    }
}
