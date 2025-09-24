package com.universidad.persistencia;

import com.universidad.modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    public List<Estudiante> listarEstudiantes() throws SQLException {
    List<Estudiante> estudiantes = new ArrayList<>();
    String sql = """
        SELECT e.id, p.nombres, p.apellidos, p.email,
               e.codigo, e.activo, e.promedio
        FROM estudiante e
        JOIN persona p ON e.id = p.id
    """;
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Estudiante e = new Estudiante(
                rs.getLong("id"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("email"),
                rs.getDouble("codigo"),
                null, // ignoramos programa
                rs.getBoolean("activo"),
                rs.getDouble("promedio")
            );
            estudiantes.add(e);
        }
    }
    return estudiantes;
}

}