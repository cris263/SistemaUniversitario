package com.universidad.persistencia;

import com.universidad.modelo.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {



    public void guardarPersona(Persona persona) throws SQLException {
        String sql = "INSERT INTO persona (nombres, apellidos, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, persona.getNombres());
            stmt.setString(2, persona.getApellidos());
            stmt.setString(3, persona.getEmail());
            stmt.executeUpdate();

            // función que trae el id de la h2
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    persona.setId(rs.getLong(1)); // asigna el id a persona
                }
            }

            System.out.println("Persona guardada correctamente con ID: " + persona.getId());
        }
    }

    public void actualizarPersona(Persona persona) throws SQLException {
    String sql = "UPDATE persona SET nombres = ?, apellidos = ?, email = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, persona.getNombres());
        stmt.setString(2, persona.getApellidos());
        stmt.setString(3, persona.getEmail());
        stmt.setLong(4, persona.getId());

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println("Persona actualizada correctamente");
        } else {
            System.out.println("No se encontró una persona con el ID: " + persona.getId());
        }
    }
}


    public List<Persona> obtenerTodasLasPersonas() throws SQLException {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM persona";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Persona persona = new Persona(
                    rs.getLong("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("email")
                ) {
                    // como es abstracta, hacemos una clase anónima
                };
                personas.add(persona);
            }
        }
        return personas;
    }

    public void eliminarPersona(Long id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Persona eliminada correctamente");
            } else {
                System.out.println("Persona no encontrada");
            }
        }
    }
}
