package com.universidad.persistencia;

import com.universidad.modelo.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {



    public void guardarPersona(Persona persona) throws SQLException {
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }
        
        DatabaseFactory.DatabaseType dbType = DatabaseFactory.detectActiveDatabase();
        String sql = getSqlForInsertPersona();
        
        try (Connection conn = dbManager.getConnection()) {
            // Para Oracle, usamos un enfoque diferente
            if (dbType == DatabaseFactory.DatabaseType.ORACLE) {
                // 1. Para Oracle: primero obtener el ID de la secuencia
                try (PreparedStatement seqStmt = conn.prepareStatement("SELECT persona_seq.NEXTVAL FROM dual")) {
                    ResultSet seqRs = seqStmt.executeQuery();
                    if (seqRs.next()) {
                        long newId = seqRs.getLong(1);
                        persona.setId(newId); // Guardar ID
                        
                        // 2. Usar el ID obtenido en el INSERT
                        try (PreparedStatement insertStmt = conn.prepareStatement(
                                "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)")) {
                            insertStmt.setLong(1, newId);
                            insertStmt.setString(2, persona.getNombres());
                            insertStmt.setString(3, persona.getApellidos());
                            insertStmt.setString(4, persona.getEmail());
                            insertStmt.executeUpdate();
                        }
                    }
                }
            } 
            // Para MySQL y H2, seguimos con el enfoque actual
            else {
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    setInsertParameters(stmt, persona);
                    stmt.executeUpdate();
                    
                    // Obtener ID generado (solo funciona en MySQL/H2)
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
        // Detectar qué base de datos usar
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }
        
        String sql = "UPDATE persona SET nombres = ?, apellidos = ?, email = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
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
        
        // Detectar qué base de datos usar
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }
        
        String sql = "SELECT * FROM persona";
        try (Connection conn = dbManager.getConnection();
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
        // Detectar qué base de datos usar
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }
        
        String sql = "DELETE FROM persona WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
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
    
    /**
     * Obtiene la SQL correcta para insertar persona según la base de datos
     */
    private String getSqlForInsertPersona() {
        DatabaseFactory.DatabaseType dbType = DatabaseFactory.detectActiveDatabase();
        
        switch (dbType) {
            case H2:
            case MYSQL:
                return "INSERT INTO persona (nombres, apellidos, email) VALUES (?, ?, ?)";
                
            case ORACLE:
                return "INSERT INTO persona (id, nombres, apellidos, email) VALUES (persona_seq.NEXTVAL, ?, ?, ?)";
                
            default:
                return "INSERT INTO persona (nombres, apellidos, email) VALUES (?, ?, ?)";
        }
    }
    
    /**
     * Establece los parámetros para el INSERT según la base de datos
     */
    private void setInsertParameters(PreparedStatement stmt, Persona persona) throws SQLException {
        DatabaseFactory.DatabaseType dbType = DatabaseFactory.detectActiveDatabase();
        
        switch (dbType) {
            case H2:
            case MYSQL:
                stmt.setString(1, persona.getNombres());
                stmt.setString(2, persona.getApellidos());
                stmt.setString(3, persona.getEmail());
                break;
                
            case ORACLE:
                // Oracle usa secuencia, por eso los parámetros empiezan en 1
                stmt.setString(1, persona.getNombres());
                stmt.setString(2, persona.getApellidos());
                stmt.setString(3, persona.getEmail());
                break;
                
            default:
                stmt.setString(1, persona.getNombres());
                stmt.setString(2, persona.getApellidos());
                stmt.setString(3, persona.getEmail());
                break;
        }
    }
}
