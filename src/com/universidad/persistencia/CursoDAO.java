package com.universidad.persistencia;

import com.universidad.modelo.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public List<Curso> listarCursos() throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id, nombre, programa, activo FROM curso";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        null,
                        rs.getBoolean("activo")
                );
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }
}
