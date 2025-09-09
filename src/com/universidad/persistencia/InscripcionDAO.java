package com.universidad.persistencia;

import com.universidad.modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    // 🔹 Constructor vacío (no necesitas pasar connection)
    public InscripcionDAO() {}

    // 1️⃣ Guardar la inscripción (INSERT)
    public void inscribirCurso(Inscripcion inscripcion) throws SQLException {
        String sql = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, inscripcion.getCurso().getId());
            ps.setLong(2, inscripcion.getEstudiante().getId());
            ps.setInt(3, inscripcion.getAnio());
            ps.setInt(4, inscripcion.getSemestre());
            ps.executeUpdate();
        }
    }

    // 2️⃣ Eliminar inscripción
    public void eliminar(Inscripcion inscripcion) throws SQLException {
        String sql = "DELETE FROM inscripcion WHERE curso = ? AND estudiante = ? AND anio = ? AND semestre = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, inscripcion.getCurso().getId());
            ps.setLong(2, inscripcion.getEstudiante().getId());
            ps.setInt(3, inscripcion.getAnio());
            ps.setInt(4, inscripcion.getSemestre());
            ps.executeUpdate();
        }
    }

    // 3️⃣ Actualizar inscripción
    public void actualizar(Inscripcion inscripcion, int nuevoAnio, int nuevoSemestre) throws SQLException {
    String sql = "UPDATE inscripcion SET anio = ?, semestre = ? " +
                 "WHERE curso = ? AND estudiante = ? AND anio = ? AND semestre = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        // Nuevos valores
        ps.setInt(1, nuevoAnio);
        ps.setInt(2, nuevoSemestre);

        // Condiciones (clave primaria actual)
        ps.setLong(3, inscripcion.getCurso().getId());
        ps.setLong(4, inscripcion.getEstudiante().getId());
        ps.setInt(5, inscripcion.getAnio());
        ps.setInt(6, inscripcion.getSemestre());

        ps.executeUpdate();
    }
}

    // 4️⃣ Cargar todas las inscripciones
    public List<Inscripcion> cargarDatos() throws SQLException {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = """
            SELECT i.curso, i.estudiante, i.anio, i.semestre,
                   c.nombre AS curso_nombre,
                   p.nombres, p.apellidos, p.email
            FROM inscripcion i
            JOIN curso c ON i.curso = c.id
            JOIN estudiante e ON i.estudiante = e.id
            JOIN persona p ON e.id = p.id
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getLong("curso"));
                curso.setNombre(rs.getString("curso_nombre"));

                Estudiante estudiante = new Estudiante();
                estudiante.setId(rs.getLong("estudiante"));
                estudiante.setNombres(rs.getString("nombres"));
                estudiante.setApellidos(rs.getString("apellidos"));
                estudiante.setEmail(rs.getString("email"));

                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setCurso(curso);
                inscripcion.setEstudiante(estudiante);
                inscripcion.setAnio(rs.getInt("anio"));
                inscripcion.setSemestre(rs.getInt("semestre"));

                inscripciones.add(inscripcion);
            }
        }
        return inscripciones;
    }

    // 5️⃣ Mostrar inscripciones en consola
    public void mostrarInscripciones() throws SQLException {
        for (Inscripcion inscripcion : cargarDatos()) {
            System.out.println(inscripcion);
        }
    }
}
