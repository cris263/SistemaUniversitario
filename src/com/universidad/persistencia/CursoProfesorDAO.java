package com.universidad.persistencia;

import com.universidad.modelo.CursoProfesor;
import com.universidad.modelo.Profesor;
import com.universidad.modelo.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoProfesorDAO {

    public void guardarCursoProfesor(CursoProfesor cp) throws SQLException {
        // ✅ Corregido: usar 'profesor' y 'curso' (sin _id)
        String sql = "INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cp.getProfesor().getId());
            stmt.setLong(2, cp.getCurso().getId());
            stmt.setInt(3, cp.getAnio());
            stmt.setInt(4, cp.getSemestre());

            stmt.executeUpdate();
            System.out.println("✅ CursoProfesor guardado: " + cp);
        }
    }

    public void eliminarCursoProfesor(CursoProfesor cp) throws SQLException {
        // ✅ Corregido: usar 'profesor' y 'curso' (sin _id)
        String sql = "DELETE FROM curso_profesor WHERE profesor = ? AND curso = ? AND anio = ? AND semestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cp.getProfesor().getId());
            stmt.setLong(2, cp.getCurso().getId());
            stmt.setInt(3, cp.getAnio());
            stmt.setInt(4, cp.getSemestre());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("🗑️ CursoProfesor eliminado: " + cp);
            } else {
                System.out.println("⚠️ No se encontró el CursoProfesor: " + cp);
            }
        }
    }

    public void actualizarCursoProfesor(CursoProfesor original, CursoProfesor actualizado) throws SQLException {
        // ✅ Corregido: usar 'profesor' y 'curso' (sin _id)
        String sql = "UPDATE curso_profesor SET anio = ?, semestre = ? " +
                "WHERE profesor = ? AND curso = ? AND anio = ? AND semestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, actualizado.getAnio());
            stmt.setInt(2, actualizado.getSemestre());
            stmt.setLong(3, original.getProfesor().getId());
            stmt.setLong(4, original.getCurso().getId());
            stmt.setInt(5, original.getAnio());
            stmt.setInt(6, original.getSemestre());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✏️ CursoProfesor actualizado: " + actualizado);
            } else {
                System.out.println("⚠️ No se encontró el CursoProfesor a actualizar.");
            }
        }
    }

    public List<CursoProfesor> obtenerTodos() throws SQLException {
        List<CursoProfesor> lista = new ArrayList<>();
        // ✅ Corregido: JOIN usando los nombres correctos de columnas y tablas
        String sql = "SELECT cp.profesor, cp.curso, cp.anio, cp.semestre, " +
                "p.nombres AS profesor_nombres, p.apellidos AS profesor_apellidos, " +
                "p.email AS profesor_email, pr.tipo_contrato, " +
                "c.nombre AS curso_nombre, c.activo AS curso_activo, c.programa AS curso_programa " +
                "FROM curso_profesor cp " +
                "JOIN profesor pr ON cp.profesor = pr.id " +
                "JOIN persona p ON pr.id = p.id " +
                "JOIN curso c ON cp.curso = c.id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Construir Profesor
                Profesor profesor = new Profesor(
                        rs.getLong("profesor"),
                        rs.getString("profesor_nombres"),
                        rs.getString("profesor_apellidos"),
                        rs.getString("profesor_email"),
                        rs.getString("tipo_contrato"));

                // Construir Curso
                Curso curso = new Curso(
                        rs.getLong("curso"),
                        rs.getString("curso_nombre"),
                        null,
                        rs.getBoolean("curso_activo"));

                // Construir CursoProfesor
                CursoProfesor cp = new CursoProfesor(
                        profesor,
                        rs.getInt("anio"),
                        rs.getInt("semestre"),
                        curso);

                lista.add(cp);
            }
        }
        return lista;
    }

    // ✅ Métodos adicionales útiles
    public List<CursoProfesor> obtenerPorProfesor(Long profesorId) throws SQLException {
        List<CursoProfesor> lista = new ArrayList<>();
        String sql = "SELECT cp.profesor, cp.curso, cp.anio, cp.semestre, " +
                "p.nombres AS profesor_nombres, p.apellidos AS profesor_apellidos, " +
                "p.email AS profesor_email, pr.tipo_contrato, " +
                "c.nombre AS curso_nombre, c.activo AS curso_activo, c.programa AS curso_programa " +
                "FROM curso_profesor cp " +
                "JOIN profesor pr ON cp.profesor = pr.id " +
                "JOIN persona p ON pr.id = p.id " +
                "JOIN curso c ON cp.curso = c.id " +
                "WHERE cp.profesor = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, profesorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Profesor profesor = new Profesor(
                            rs.getLong("profesor"),
                            rs.getString("profesor_nombres"),
                            rs.getString("profesor_apellidos"),
                            rs.getString("profesor_email"),
                            rs.getString("tipo_contrato"));

                    Curso curso = new Curso(
                            rs.getLong("curso"),
                            rs.getString("curso_nombre"),
                            null,
                            rs.getBoolean("curso_activo"));

                    CursoProfesor cp = new CursoProfesor(
                            profesor,
                            rs.getInt("anio"),
                            rs.getInt("semestre"),
                            curso);

                    lista.add(cp);
                }
            }
        }
        return lista;
    }

    public List<CursoProfesor> obtenerPorCurso(Long cursoId) throws SQLException {
        List<CursoProfesor> lista = new ArrayList<>();
        String sql = "SELECT cp.profesor, cp.curso, cp.anio, cp.semestre, " +
                "p.nombres AS profesor_nombres, p.apellidos AS profesor_apellidos, " +
                "p.email AS profesor_email, pr.tipo_contrato, " +
                "c.nombre AS curso_nombre, c.activo AS curso_activo, c.programa AS curso_programa " +
                "FROM curso_profesor cp " +
                "JOIN profesor pr ON cp.profesor = pr.id " +
                "JOIN persona p ON pr.id = p.id " +
                "JOIN curso c ON cp.curso = c.id " +
                "WHERE cp.curso = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cursoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Profesor profesor = new Profesor(
                            rs.getLong("profesor"),
                            rs.getString("profesor_nombres"),
                            rs.getString("profesor_apellidos"),
                            rs.getString("profesor_email"),
                            rs.getString("tipo_contrato"));

                    Curso curso = new Curso(
                            rs.getLong("curso"),
                            rs.getString("curso_nombre"),
                            null,
                            rs.getBoolean("curso_activo"));

                    CursoProfesor cp = new CursoProfesor(
                            profesor,
                            rs.getInt("anio"),
                            rs.getInt("semestre"),
                            curso);

                    lista.add(cp);
                }
            }
        }
        return lista;
    }

    public boolean existe(CursoProfesor cp) throws SQLException {
        String sql = "SELECT COUNT(*) FROM curso_profesor " +
                "WHERE profesor = ? AND curso = ? AND anio = ? AND semestre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cp.getProfesor().getId());
            stmt.setLong(2, cp.getCurso().getId());
            stmt.setInt(3, cp.getAnio());
            stmt.setInt(4, cp.getSemestre());

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}