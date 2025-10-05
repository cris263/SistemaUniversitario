//package com.universidad.persistencia;
//
//import com.universidad.modelo.Curso;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CursoDAO {
//
//    public List<Curso> listarCursos() throws SQLException {
//        List<Curso> cursos = new ArrayList<>();
//
//        // Detectar qué base de datos usar
//        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
//
//        if (dbManager == null) {
//            throw new SQLException("No hay ninguna base de datos disponible");
//        }
//
//        String sql = "SELECT id, nombre, programa, activo FROM curso";
//
//        try (Connection conn = dbManager.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                boolean activo = getActivoValue(rs);
//
//                Curso curso = new Curso(
//                        rs.getLong("id"),
//                        rs.getString("nombre"),
//                        null,
//                        activo
//                );
//                cursos.add(curso);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//        return cursos;
//    }
//
//
//    /**
//     * Obtiene el valor booleano de 'activo' según la base de datos
//     */
//    private boolean getActivoValue(ResultSet rs) throws SQLException {
//        DatabaseFactory.DatabaseType dbType = DatabaseFactory.detectActiveDatabase();
//
//        switch (dbType) {
//            case H2:
//            case MYSQL:
//                return rs.getBoolean("activo");
//
//            case ORACLE:
//                // Oracle usa NUMBER(1) donde 1=true, 0=false
//                return rs.getInt("activo") == 1;
//
//            default:
//                return rs.getBoolean("activo");
//        }
//    }
//}


package com.universidad.persistencia;

import com.universidad.modelo.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public List<Curso> listarCursos() throws SQLException {
        List<Curso> cursos = new ArrayList<>();

        // Detectar qué base de datos usar
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();

        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }

        String sql = "SELECT id, nombre, programa, activo FROM curso";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                boolean activo = getActivoValue(rs);

                Curso curso = new Curso(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        null,
                        activo
                );
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return cursos;
    }
    public void eliminarCurso(Long id) throws SQLException {
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) throw new SQLException("No hay ninguna base de datos disponible");

        String sql = "DELETE FROM curso WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }



    /**
     * Obtiene el valor booleano de 'activo' según la base de datos
     */
    private boolean getActivoValue(ResultSet rs) throws SQLException {
        DatabaseFactory.DatabaseType dbType = DatabaseFactory.detectActiveDatabase();

        switch (dbType) {
            case H2:
            case MYSQL:
                return rs.getBoolean("activo");

            case ORACLE:
                // Oracle usa NUMBER(1) donde 1=true, 0=false
                return rs.getInt("activo") == 1;

            default:
                return rs.getBoolean("activo");
        }
    }
    public Curso crearCurso(Curso curso) throws SQLException {
        String sql = "INSERT INTO curso (nombre, programa, activo) VALUES (?, ?, ?)";

        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, curso.getNombre());
            if (curso.getPrograma() != null) {
                ps.setLong(2, curso.getPrograma().getId().longValue());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setBoolean(3, curso.getActivo() != null && curso.getActivo());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Crear curso falló, no se insertó ninguna fila.");
            }

            // Obtener ID generado
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    curso.setId(generatedKeys.getLong(1));
                }
            }
        }

        return curso;


    }
}
