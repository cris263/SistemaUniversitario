

package com.universidad.persistencia;

import com.universidad.modelo.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Curso
 * Usa DB.java para conexiones dinámicas
 */
public class CursoDAO {

    public List<Curso> listarCursos() throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id, nombre, programa, activo FROM curso";

        try (Connection conn = DB.getConnection();
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
        String sql = "DELETE FROM curso WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Obtiene el valor booleano de 'activo' según la base de datos activa
     */
    private boolean getActivoValue(ResultSet rs) throws SQLException {
        String dbType = DB.getActiveDatabaseType();

        switch (dbType) {
            case "H2":
            case "MySQL":
                return rs.getBoolean("activo");

            case "Oracle":
                // Oracle usa NUMBER(1) donde 1=true, 0=false
                return rs.getInt("activo") == 1;

            default:
                return rs.getBoolean("activo");
        }
    }
    
    public Curso crearCurso(Curso curso) throws SQLException {
        String sql = "INSERT INTO curso (nombre, programa, activo) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
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
