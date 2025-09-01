package com.universidad.persistencia;


import com.universidad.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {
    
    public void guardarInscripcion(Inscripcion inscripcion) throws SQLException {
        String sql = """
            INSERT INTO inscripcion 
            (curso_id, curso_nombre, anio, semestre, estudiante_id, estudiante_nombres, 
             estudiante_apellidos, estudiante_email, estudiante_codigo, 
             estudiante_promedio, estudiante_activo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, inscripcion.getCurso().getId());
            stmt.setString(2, inscripcion.getCurso().getNombre());
            stmt.setInt(3, inscripcion.getAnio());
            stmt.setInt(4, inscripcion.getSemestre());
            stmt.setDouble(5, inscripcion.getEstudiante().getId());
            stmt.setString(6, inscripcion.getEstudiante().getNombres());
            stmt.setString(7, inscripcion.getEstudiante().getApellidos());
            stmt.setString(8, inscripcion.getEstudiante().getEmail());
            stmt.setDouble(9, inscripcion.getEstudiante().getCodigo());
            stmt.setDouble(10, inscripcion.getEstudiante().getPromedio());
            stmt.setBoolean(11, inscripcion.getEstudiante().getActivo());
            
            stmt.executeUpdate();
            System.out.println("Inscripción guardada correctamente");
        }
    }
    
    public List<Inscripcion> obtenerTodasLasInscripciones() throws SQLException {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT * FROM inscripcion";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Crear curso
                Curso curso = new Curso();
                curso.setId(rs.getInt("curso_id"));
                curso.setNombre(rs.getString("curso_nombre"));
                
                // Crear estudiante
                Estudiante estudiante = new Estudiante();
                estudiante.setId(rs.getLong("estudiante_id"));
                estudiante.setNombres(rs.getString("estudiante_nombres"));
                estudiante.setApellidos(rs.getString("estudiante_apellidos"));
                estudiante.setEmail(rs.getString("estudiante_email"));
                estudiante.setCodigo(rs.getDouble("estudiante_codigo"));
                estudiante.setPromedio(rs.getDouble("estudiante_promedio"));
                estudiante.setActivo(rs.getBoolean("estudiante_activo"));
                
                // Crear inscripción
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setCurso(curso);
                inscripcion.setAnio(rs.getInt("anio"));
                inscripcion.setSemestre(rs.getInt("semestre"));
                inscripcion.setEstudiante(estudiante);
                
                inscripciones.add(inscripcion);
            }
        }
        
        return inscripciones;
    }
    
    public void eliminarInscripcion(int cursoId, double estudianteId, int anio, int semestre) throws SQLException {
        String sql = "DELETE FROM inscripcion WHERE curso_id = ? AND estudiante_id = ? AND anio = ? AND semestre = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cursoId);
            stmt.setDouble(2, estudianteId);
            stmt.setInt(3, anio);
            stmt.setInt(4, semestre);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inscripción eliminada correctamente");
            } else {
                System.out.println("No se encontró la inscripción para eliminar");
            }
        }
    }
}