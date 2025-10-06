package com.universidad.persistencia;

import com.universidad.modelo.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Persona
 * Usa DB.getConnection() que maneja la base de datos activa
 */
public class PersonaDAO {

    public void guardarPersona(Persona persona) throws SQLException {
        String dbType = DB.getActiveDatabaseType();
        
        try (Connection conn = DB.getConnection()) {
            // Para Oracle, usamos secuencia
            if (dbType.equals("Oracle")) {
                // 1. Obtener el ID de la secuencia
                try (PreparedStatement seqStmt = conn.prepareStatement("SELECT persona_seq.NEXTVAL FROM dual")) {
                    ResultSet seqRs = seqStmt.executeQuery();
                    if (seqRs.next()) {
                        long newId = seqRs.getLong(1);
                        persona.setId(newId);
                        
                        // 2. Insertar con el ID obtenido
                        String sql = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                            insertStmt.setLong(1, newId);
                            insertStmt.setString(2, persona.getNombres());
                            insertStmt.setString(3, persona.getApellidos());
                            insertStmt.setString(4, persona.getEmail());
                            insertStmt.executeUpdate();
                        }
                    }
                }
            } 
            // Para MySQL y H2, auto-increment
            else {
                String sql = "INSERT INTO persona (nombres, apellidos, email) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, persona.getNombres());
                    stmt.setString(2, persona.getApellidos());
                    stmt.setString(3, persona.getEmail());
                    stmt.executeUpdate();
                    
                    // Obtener ID generado
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            persona.setId(rs.getLong(1));
                        }
                    }
                }
            }
            
            System.out.println("Persona guardada correctamente con ID: " + persona.getId());
        }
    }

    public void actualizarPersona(Persona persona) throws SQLException {
        String sql = "UPDATE persona SET nombres = ?, apellidos = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
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
        
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Persona persona = new Persona(
                    rs.getLong("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("email")
                ) {};
                personas.add(persona);
            }
        }
        return personas;
    }

    public void eliminarPersona(Long id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
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
