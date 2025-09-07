package com.universidad.persistencia;

import com.universidad.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    private Connection connection;

    public InscripcionDAO(Connection connection) {
        this.connection = connection;
    }

    // 1️⃣ Guardar la inscripción (equivalente a "InscribirCurso" / "guardarInformacion")
    public void inscribirCurso(Inscripcion inscripcion) throws SQLException {
        String sql = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, inscripcion.getCurso().getId());
            ps.setLong(2, inscripcion.getEstudiante().getId());
            ps.setInt(3, inscripcion.getAnio());
            ps.setInt(4, inscripcion.getSemestre());
            ps.executeUpdate();
        }
    }

    // 2️⃣ Eliminar una inscripción
    public void eliminar(Inscripcion inscripcion) throws SQLException {
        String sql = "DELETE FROM inscripcion WHERE curso = ? AND estudiante = ? AND anio = ? AND semestre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, inscripcion.getCurso().getId());
            ps.setLong(2, inscripcion.getEstudiante().getId());
            ps.setInt(3, inscripcion.getAnio());
            ps.setInt(4, inscripcion.getSemestre());
            ps.executeUpdate();
        }
    }

    // 3️⃣ Actualizar una inscripción
    public void actualizar(Inscripcion inscripcion, Inscripcion nuevaInfo) throws SQLException {
        String sql = "UPDATE inscripcion SET curso = ?, estudiante = ?, anio = ?, semestre = ? " +
                "WHERE curso = ? AND estudiante = ? AND anio = ? AND semestre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Nuevos valores
            ps.setLong(1, nuevaInfo.getCurso().getId());
            ps.setLong(2, nuevaInfo.getEstudiante().getId());
            ps.setInt(3, nuevaInfo.getAnio());
            ps.setInt(4, nuevaInfo.getSemestre());
            // Condición WHERE
            ps.setLong(5, inscripcion.getCurso().getId());
            ps.setLong(6, inscripcion.getEstudiante().getId());
            ps.setInt(7, inscripcion.getAnio());
            ps.setInt(8, inscripcion.getSemestre());
            ps.executeUpdate();
        }
    }

    // 4️⃣ Cargar todos los datos de inscripciones
    public List<Inscripcion> cargarDatos() throws SQLException {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT i.curso, i.estudiante, i.anio, i.semestre, " +
                "c.nombre AS curso_nombre, " +
                "p.nombres, p.apellidos, p.email " +
                "FROM inscripcion i " +
                "JOIN curso c ON i.curso = c.id " +
                "JOIN estudiante e ON i.estudiante = e.id " +
                "JOIN persona p ON e.id = p.id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
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

    // 5️⃣ Método para mostrar las inscripciones como texto (toString)
    public void mostrarInscripciones() throws SQLException {
        List<Inscripcion> inscripciones = cargarDatos();
        for (Inscripcion inscripcion : inscripciones) {
            System.out.println(inscripcion);
        }
    }
}
