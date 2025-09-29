package com.universidad.persistencia;

import java.sql.*;

/**
 * Gestor simple para Oracle Database
 * Implementa la interfaz DatabaseManager de forma sencilla
 */
public class OracleDatabaseManager implements DatabaseManager {
    
    private String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private String user = "universidad_user";
    private String pass = "universidad_pass";
    
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver Oracle no encontrado");
        }
    }
    
    @Override
    public boolean createTables() {
        try (Connection conn = getConnection()) {
            // Oracle necesita secuencias para AUTO_INCREMENT
            String createPersonaSeq = "CREATE SEQUENCE persona_seq START WITH 1 INCREMENT BY 1";
            String createFacultadSeq = "CREATE SEQUENCE facultad_seq START WITH 1 INCREMENT BY 1";
            String createProgramaSeq = "CREATE SEQUENCE programa_seq START WITH 1 INCREMENT BY 1";
            String createCursoSeq = "CREATE SEQUENCE curso_seq START WITH 1 INCREMENT BY 1";
            
            String createPersonaTable = """
                    CREATE TABLE persona (
                        id NUMBER PRIMARY KEY,
                        nombres VARCHAR2(100) NOT NULL,
                        apellidos VARCHAR2(100) NOT NULL,
                        email VARCHAR2(100) NOT NULL
                    )
                """;

            // Facultad
            String createFacultadTable = """
                    CREATE TABLE facultad (
                        id NUMBER PRIMARY KEY,
                        nombre VARCHAR2(100) NOT NULL,
                        decano NUMBER,
                        FOREIGN KEY (decano) REFERENCES persona(id)
                    )
                """;

            // Programa
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

            // Curso
            String createCursoTable = """
                    CREATE TABLE curso (
                        id NUMBER PRIMARY KEY,
                        nombre VARCHAR2(100) NOT NULL,
                        programa NUMBER,
                        activo NUMBER(1),
                        FOREIGN KEY (programa) REFERENCES programa(id)
                    )
                """;

            // Profesor (subclase de Persona)
            String createProfesorTable = """
                    CREATE TABLE profesor (
                        id NUMBER PRIMARY KEY,
                        tipo_contrato VARCHAR2(50),
                        FOREIGN KEY (id) REFERENCES persona(id)
                    )
                """;

            // Estudiante (subclase de Persona)
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

            // CursoProfesor
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

            // Inscripcion
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

            try (Statement stmt = conn.createStatement()) {
                createSequenceIfNotExists(stmt, "persona_seq", createPersonaSeq);
                createSequenceIfNotExists(stmt, "facultad_seq", createFacultadSeq);
                createSequenceIfNotExists(stmt, "programa_seq", createProgramaSeq);
                createSequenceIfNotExists(stmt, "curso_seq", createCursoSeq);

                // Crear tablas en orden (con validación)
                createTableIfNotExists(stmt, "persona", createPersonaTable);
                createTableIfNotExists(stmt, "facultad", createFacultadTable);
                createTableIfNotExists(stmt, "programa", createProgramaTable);
                createTableIfNotExists(stmt, "curso", createCursoTable);
                createTableIfNotExists(stmt, "profesor", createProfesorTable);
                createTableIfNotExists(stmt, "estudiante", createEstudianteTable);
                createTableIfNotExists(stmt, "curso_profesor", createCursoProfesorTable);
                createTableIfNotExists(stmt, "inscripcion", createInscripcionTable);
            }

            System.out.println("Base de datos Oracle inicializada correctamente (todas las tablas creadas)");
            return true;

        } catch (SQLException e) {
            System.err.println("Error inicializando la base de datos Oracle: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea una secuencia solo si no existe
     */
    private void createSequenceIfNotExists(Statement stmt, String sequenceName, String createSQL) {
        // Verificar si la secuencia existe
        try (ResultSet rs = stmt.executeQuery("SELECT sequence_name FROM user_sequences WHERE sequence_name = '"
                + sequenceName.toUpperCase() + "'")) {
            if (rs.next()) {
                System.out.println("ℹ️ Secuencia " + sequenceName + " ya existe");
                return;
            }
        } catch (SQLException e) {
            // Si hay error verificando, intentar crear de todos modos
        }

        // Crear la secuencia
        try {
            stmt.execute(createSQL);
            System.out.println("✅ Secuencia " + sequenceName + " creada");
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) { // ORA-00955: name is already used
                System.out.println("ℹ️ Secuencia " + sequenceName + " ya existía");
            } else {
                System.err.println("⚠️ Error creando secuencia " + sequenceName + ": " + e.getMessage());
            }
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        String checkTableSQL = "SELECT COUNT(*) FROM user_tables WHERE table_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(checkTableSQL)) {
            stmt.setString(1, tableName.toUpperCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Crea una tabla solo si no existe
     */
    private void createTableIfNotExists(Statement stmt, String tableName, String createSQL) throws SQLException {
        try {
            stmt.execute(createSQL);
            System.out.println("✅ Tabla " + tableName + " creada");
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) { // ORA-00955: name is already used
                System.out.println("ℹ️ Tabla " + tableName + " ya existía");
            } else {
                throw e; // Re-lanzar otros errores
            }
        }
    }
    
    @Override
    public boolean insertTestData() {
        try (Connection conn = getConnection()) {
            // Verificar si ya hay datos
            PreparedStatement checkData = conn.prepareStatement("SELECT COUNT(*) FROM persona");
            ResultSet rs = checkData.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Los datos de prueba ya existen.");
                return true;
            }
            
            conn.setAutoCommit(false);
            
            // 1. Insertar Personas (base para profesores y estudiantes)
            String insertPersonas = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (persona_seq.NEXTVAL, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertPersonas)) {
                // Profesores/Decanos
                stmt.setString(1, "Carlos Alberto"); stmt.setString(2, "González Pérez"); stmt.setString(3, "carlos.gonzalez@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "María Elena"); stmt.setString(2, "Rodríguez López"); stmt.setString(3, "maria.rodriguez@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "José Luis"); stmt.setString(2, "Martínez Silva"); stmt.setString(3, "jose.martinez@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "Ana Patricia"); stmt.setString(2, "Fernández Torres"); stmt.setString(3, "ana.fernandez@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "Roberto"); stmt.setString(2, "Jiménez Castro"); stmt.setString(3, "roberto.jimenez@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "Laura Cristina"); stmt.setString(2, "Vargas Herrera"); stmt.setString(3, "laura.vargas@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "Miguel Ángel"); stmt.setString(2, "Ramírez Gómez"); stmt.setString(3, "miguel.ramirez@universidad.edu"); stmt.addBatch();
                stmt.setString(1, "Diana Carolina"); stmt.setString(2, "Morales Díaz"); stmt.setString(3, "diana.morales@universidad.edu"); stmt.addBatch();
                
                // Estudiantes
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
            
            // 2. Insertar Facultades (sin decano primero)
            String insertFacultades = "INSERT INTO facultad (id, nombre, decano) VALUES (facultad_seq.NEXTVAL, ?, NULL)";
            try (PreparedStatement stmt = conn.prepareStatement(insertFacultades)) {
                stmt.setString(1, "Facultad de Ingeniería"); stmt.addBatch();
                stmt.setString(1, "Facultad de Ciencias Económicas"); stmt.addBatch();
                stmt.setString(1, "Facultad de Medicina"); stmt.addBatch();
                stmt.setString(1, "Facultad de Derecho"); stmt.addBatch();
                stmt.executeBatch();
            }
            
            // 3. Actualizar decanos de facultades
            String updateDecanos = "UPDATE facultad SET decano = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateDecanos)) {
                stmt.setLong(1, 1); stmt.setLong(2, 1); stmt.addBatch(); // Carlos González - Ingeniería
                stmt.setLong(1, 2); stmt.setLong(2, 2); stmt.addBatch(); // María Rodríguez - Económicas
                stmt.setLong(1, 3); stmt.setLong(2, 3); stmt.addBatch(); // José Martínez - Medicina
                stmt.setLong(1, 4); stmt.setLong(2, 4); stmt.addBatch(); // Ana Fernández - Derecho
                stmt.executeBatch();
            }
            
            // 4. Insertar Programas (Oracle usa DATE '2020-01-15' formato)
            String insertProgramas = "INSERT INTO programa (id, nombre, duracion, registro, facultad) VALUES (programa_seq.NEXTVAL, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertProgramas)) {
                stmt.setString(1, "Ingeniería de Sistemas"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2020-01-15")); stmt.setLong(4, 1); stmt.addBatch();
                stmt.setString(1, "Ingeniería Civil"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2019-08-20")); stmt.setLong(4, 1); stmt.addBatch();
                stmt.setString(1, "Administración de Empresas"); stmt.setDouble(2, 4.0); stmt.setDate(3, Date.valueOf("2021-02-10")); stmt.setLong(4, 2); stmt.addBatch();
                stmt.setString(1, "Contaduría Pública"); stmt.setDouble(2, 4.5); stmt.setDate(3, Date.valueOf("2020-07-05")); stmt.setLong(4, 2); stmt.addBatch();
                stmt.setString(1, "Medicina"); stmt.setDouble(2, 6.0); stmt.setDate(3, Date.valueOf("2018-03-12")); stmt.setLong(4, 3); stmt.addBatch();
                stmt.setString(1, "Derecho"); stmt.setDouble(2, 5.0); stmt.setDate(3, Date.valueOf("2019-09-18")); stmt.setLong(4, 4); stmt.addBatch();
                stmt.executeBatch();
            }
            
            // 5. Insertar Cursos (Oracle usa 1/0 para boolean)
            String insertCursos = "INSERT INTO curso (id, nombre, programa, activo) VALUES (curso_seq.NEXTVAL, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCursos)) {
                // Ingeniería de Sistemas
                stmt.setString(1, "Programación I"); stmt.setLong(2, 1); stmt.setInt(3, 1); stmt.addBatch();
                stmt.setString(1, "Base de Datos"); stmt.setLong(2, 1); stmt.setInt(3, 1); stmt.addBatch();
                stmt.setString(1, "Estructuras de Datos"); stmt.setLong(2, 1); stmt.setInt(3, 1); stmt.addBatch();
                // Ingeniería Civil
                stmt.setString(1, "Cálculo I"); stmt.setLong(2, 2); stmt.setInt(3, 1); stmt.addBatch();
                stmt.setString(1, "Física I"); stmt.setLong(2, 2); stmt.setInt(3, 1); stmt.addBatch();
                // Administración
                stmt.setString(1, "Contabilidad General"); stmt.setLong(2, 3); stmt.setInt(3, 1); stmt.addBatch();
                stmt.setString(1, "Marketing"); stmt.setLong(2, 3); stmt.setInt(3, 1); stmt.addBatch();
                // Contaduría
                stmt.setString(1, "Auditoría"); stmt.setLong(2, 4); stmt.setInt(3, 1); stmt.addBatch();
                stmt.setString(1, "Costos"); stmt.setLong(2, 4); stmt.setInt(3, 1); stmt.addBatch();
                // Medicina
                stmt.setString(1, "Anatomía"); stmt.setLong(2, 5); stmt.setInt(3, 1); stmt.addBatch();
                stmt.setString(1, "Fisiología"); stmt.setLong(2, 5); stmt.setInt(3, 1); stmt.addBatch();
                // Derecho
                stmt.setString(1, "Derecho Constitucional"); stmt.setLong(2, 6); stmt.setInt(3, 1); stmt.addBatch();
                stmt.executeBatch();
            }
            
            // 6. Insertar Profesores
            String insertProfesores = "INSERT INTO profesor (id, tipo_contrato) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertProfesores)) {
                stmt.setLong(1, 1); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
                stmt.setLong(1, 2); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
                stmt.setLong(1, 3); stmt.setString(2, "Medio Tiempo"); stmt.addBatch();
                stmt.setLong(1, 4); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
                stmt.setLong(1, 5); stmt.setString(2, "Cátedra"); stmt.addBatch();
                stmt.setLong(1, 6); stmt.setString(2, "Tiempo Completo"); stmt.addBatch();
                stmt.setLong(1, 7); stmt.setString(2, "Medio Tiempo"); stmt.addBatch();
                stmt.setLong(1, 8); stmt.setString(2, "Cátedra"); stmt.addBatch();
                stmt.executeBatch();
            }
            
            // 7. Insertar Estudiantes
            String insertEstudiantes = "INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertEstudiantes)) {
                stmt.setLong(1, 9); stmt.setLong(2, 20241001); stmt.setLong(3, 1); stmt.setInt(4, 1); stmt.setDouble(5, 4.2); stmt.addBatch();
                stmt.setLong(1, 10); stmt.setLong(2, 20241002); stmt.setLong(3, 1); stmt.setInt(4, 1); stmt.setDouble(5, 3.8); stmt.addBatch();
                stmt.setLong(1, 11); stmt.setLong(2, 20241003); stmt.setLong(3, 2); stmt.setInt(4, 1); stmt.setDouble(5, 4.0); stmt.addBatch();
                stmt.setLong(1, 12); stmt.setLong(2, 20241004); stmt.setLong(3, 3); stmt.setInt(4, 1); stmt.setDouble(5, 4.5); stmt.addBatch();
                stmt.setLong(1, 13); stmt.setLong(2, 20241005); stmt.setLong(3, 3); stmt.setInt(4, 1); stmt.setDouble(5, 3.9); stmt.addBatch();
                stmt.setLong(1, 14); stmt.setLong(2, 20241006); stmt.setLong(3, 4); stmt.setInt(4, 1); stmt.setDouble(5, 4.1); stmt.addBatch();
                stmt.setLong(1, 15); stmt.setLong(2, 20241007); stmt.setLong(3, 5); stmt.setInt(4, 1); stmt.setDouble(5, 4.3); stmt.addBatch();
                stmt.setLong(1, 16); stmt.setLong(2, 20241008); stmt.setLong(3, 6); stmt.setInt(4, 1); stmt.setDouble(5, 3.7); stmt.addBatch();
                stmt.setLong(1, 17); stmt.setLong(2, 20241009); stmt.setLong(3, 1); stmt.setInt(4, 1); stmt.setDouble(5, 4.4); stmt.addBatch();
                stmt.setLong(1, 18); stmt.setLong(2, 20241010); stmt.setLong(3, 2); stmt.setInt(4, 1); stmt.setDouble(5, 3.6); stmt.addBatch();
                stmt.executeBatch();
            }
            
            // 8. Insertar asignaciones Curso-Profesor
            String insertCursoProfesor = "INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCursoProfesor)) {
                stmt.setLong(1, 1); stmt.setLong(2, 1); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Carlos - Programación I
                stmt.setLong(1, 2); stmt.setLong(2, 2); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // María - Base de Datos
                stmt.setLong(1, 3); stmt.setLong(2, 4); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // José - Cálculo I
                stmt.setLong(1, 4); stmt.setLong(2, 12); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Ana - Derecho Constitucional
                stmt.setLong(1, 5); stmt.setLong(2, 6); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Roberto - Contabilidad
                stmt.setLong(1, 6); stmt.setLong(2, 10); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Laura - Anatomía
                stmt.executeBatch();
            }
            
            // 9. Insertar Inscripciones
            String insertInscripciones = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertInscripciones)) {
                // Estudiantes de Sistemas
                stmt.setLong(1, 1); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Andrés - Programación I
                stmt.setLong(1, 2); stmt.setLong(2, 9); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Andrés - Base de Datos
                stmt.setLong(1, 1); stmt.setLong(2, 10); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Camila - Programación I
                stmt.setLong(1, 1); stmt.setLong(2, 17); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Mateo - Programación I
                
                // Estudiantes de Civil
                stmt.setLong(1, 4); stmt.setLong(2, 11); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Daniel - Cálculo I
                stmt.setLong(1, 4); stmt.setLong(2, 18); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Alejandra - Cálculo I
                
                // Estudiantes de Administración
                stmt.setLong(1, 6); stmt.setLong(2, 12); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Valentina - Contabilidad
                stmt.setLong(1, 6); stmt.setLong(2, 13); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Santiago - Contabilidad
                
                // Estudiantes de otros programas
                stmt.setLong(1, 12); stmt.setLong(2, 16); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Sofía - Derecho Constitucional
                stmt.setLong(1, 10); stmt.setLong(2, 15); stmt.setInt(3, 2024); stmt.setInt(4, 2); stmt.addBatch(); // Juan Pablo - Anatomía
                stmt.executeBatch();
            }
            
            conn.commit();
            System.out.println("Datos de prueba insertados correctamente:");
            System.out.println("   - 18 personas (8 profesores + 10 estudiantes)");
            System.out.println("   - 4 facultades con decanos asignados");
            System.out.println("   - 6 programas académicos");
            System.out.println("   - 12 cursos activos");
            System.out.println("   - 8 profesores con diferentes tipos de contrato");
            System.out.println("   - 10 estudiantes activos con promedios");
            System.out.println("   - 6 asignaciones profesor-curso");
            System.out.println("   - 10 inscripciones de estudiantes");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error insertando datos de prueba: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean initialize() {
        System.out.println("🔄 Inicializando Oracle Database...");
        
        if (!createTables()) {
            return false;
        }
        
        if (!insertTestData()) {
            return false;
        }
        
        System.out.println("✅ Oracle Database inicializada correctamente");
        return true;
    }
    
    @Override
    public String getName() {
        return "Oracle Database (Docker)";
    }
}