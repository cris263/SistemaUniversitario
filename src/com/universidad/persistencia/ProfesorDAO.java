package com.universidad.persistencia;

import com.universidad.modelo.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    public List<Profesor> obtenerTodos() throws SQLException {
        List<Profesor> profesores = new ArrayList<>();
        
        // Detectar qué base de datos usar
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }

        String sql = """
                    SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipo_contrato
                    FROM profesor pr
                    JOIN persona p ON pr.id = p.id
                """;

        try (Connection conn = dbManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Profesor profesor = new Profesor();
                profesor.setId(rs.getLong("id"));
                profesor.setNombres(rs.getString("nombres"));
                profesor.setApellidos(rs.getString("apellidos"));
                profesor.setEmail(rs.getString("email"));
                profesor.setTipoContrato(rs.getString("tipo_contrato"));

                profesores.add(profesor);
            }
        }

        return profesores;
    }

    public Profesor buscarPorId(Long id) throws SQLException {
        // Detectar qué base de datos usar
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        if (dbManager == null) {
            throw new SQLException("No hay ninguna base de datos disponible");
        }
        
        String sql = """
                    SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipo_contrato
                    FROM profesor pr
                    JOIN persona p ON pr.id = p.id
                    WHERE p.id = ?
                """;

        try (Connection conn = dbManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profesor profesor = new Profesor();
                    profesor.setId(rs.getLong("id"));
                    profesor.setNombres(rs.getString("nombres"));
                    profesor.setApellidos(rs.getString("apellidos"));
                    profesor.setEmail(rs.getString("email"));
                    profesor.setTipoContrato(rs.getString("tipo_contrato"));

                    return profesor;
                }
            }
        }
        return null;
    }
}
