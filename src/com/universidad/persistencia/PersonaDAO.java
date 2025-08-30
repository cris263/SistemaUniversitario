package com.universidad.persistencia;

import com.universidad.modelo.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    public void guardarPersona(Persona persona) throws SQLException {
        String sql = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, persona.getId());
            stmt.setString(2, persona.getNombres());
            stmt.setString(3, persona.getApellidos());
            stmt.setString(4, persona.getEmail());
            stmt.executeUpdate();
            System.out.println("Persona guardada correctamente");
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
                    rs.getDouble("id"),
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

    public void eliminarPersona(Double id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Persona eliminada correctamente");
            } else {
                System.out.println("Persona no encontrada");
            }
        }
    }
}
