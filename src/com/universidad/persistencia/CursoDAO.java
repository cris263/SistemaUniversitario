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
        
        // SQL adaptado según la base de datos
        String sql = getSqlForListarCursos();

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
    
    /**
     * Obtiene la SQL correcta según la base de datos activa
     */
    private String getSqlForListarCursos() {
        DatabaseFactory.DatabaseType dbType = DatabaseFactory.detectActiveDatabase();
        
        switch (dbType) {
            case H2:
            case MYSQL:
                return "SELECT id, nombre, programa, activo FROM curso";
                
            case ORACLE:
                return "SELECT id, nombre, programa, activo FROM curso";
                
            default:
                return "SELECT id, nombre, programa, activo FROM curso";
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
}
