package com.universidad.persistencia;

import com.universidad.modelo.CursoProfesor;
import com.universidad.modelo.Profesor;
import com.universidad.modelo.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CursoProfesorDAO {

    public void guardarCursoProfesor(CursoProfesor cp) throws SQLException {
        String sql = "INSERT INTO curso_profesor (profesor_id, curso_id, anio, semestre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cp.getProfesor().getId());
            stmt.setLong(2, cp.getCurso().getId());
            stmt.setInt(3, cp.getAnio());
            stmt.setInt(4, cp.getSemestre());

            stmt.executeUpdate();
            System.out.println("CursoProfesor guardado: " + cp);
        }
    }

    public void eliminarCursoProfesor(CursoProfesor cp) throws SQLException {
        String sql = "DELETE FROM curso_profesor WHERE profesor_id = ? AND curso_id = ? AND anio = ? AND semestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cp.getProfesor().getId());
            stmt.setLong(2, cp.getCurso().getId());
            stmt.setInt(3, cp.getAnio());
            stmt.setInt(4, cp.getSemestre());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("CursoProfesor eliminado: " + cp);
            } else {
                System.out.println("No se encontró el CursoProfesor: " + cp);
            }
        }
    }

    public List<CursoProfesor> obtenerTodos(List<Profesor> profesores, List<Curso> cursos) throws SQLException {
        List<CursoProfesor> lista = new ArrayList<>();
        String sql = "SELECT * FROM curso_profesor";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long profesorId = rs.getLong("profesor_id");
                Integer cursoId = rs.getInt("curso_id");
                Integer anio = rs.getInt("anio");
                Integer semestre = rs.getInt("semestre");

                // Buscar profesor por ID (tipo Long)
                Profesor profesor = profesores.stream()
                        .filter(p -> Objects.equals(p.getId(), profesorId))
                        .findFirst()
                        .orElse(null);

                // Buscar curso por ID (tipo Integer)
                Curso curso = cursos.stream()
                        .filter(c -> Objects.equals(c.getId(), cursoId))
                        .findFirst()
                        .orElse(null);

                lista.add(new CursoProfesor(profesor, anio, semestre, curso));
            }
        }
        return lista;
    }

}
