package com.universidad.persistencia;

import com.universidad.modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Estudiante
 * Usa DB.java para conexiones dinámicas
 */
public class EstudianteDAO {
    
    public List<Estudiante> listarEstudiantes() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        
        String sql = """
            SELECT e.id, p.nombres, p.apellidos, p.email,
                   e.codigo, e.activo, e.promedio
            FROM estudiante e
            JOIN persona p ON e.id = p.id
        """;
        
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Manejar diferencias de tipos booleanos
                boolean activo = getActivoValue(rs, "activo");
                
                Estudiante e = new Estudiante(
                    rs.getLong("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("email"),
                    rs.getDouble("codigo"),
                    null, // ignoramos programa
                    activo,
                    rs.getDouble("promedio")
                );
                estudiantes.add(e);
            }
        }
        return estudiantes;
    }
    
    /**
     * Obtiene el valor booleano según la base de datos activa
     * H2/MySQL usan BOOLEAN, Oracle usa NUMBER(1)
     */
    private boolean getActivoValue(ResultSet rs, String columnName) throws SQLException {
        String dbType = DB.getActiveDatabaseType();
        
        switch (dbType) {
            case "H2":
            case "MySQL":
                return rs.getBoolean(columnName);
                
            case "Oracle":
                // Oracle usa NUMBER(1) donde 1=true, 0=false
                return rs.getInt(columnName) == 1;
                
            default:
                return rs.getBoolean(columnName);
        }
    }
}