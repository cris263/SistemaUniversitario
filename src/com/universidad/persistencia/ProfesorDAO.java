package com.universidad.persistencia;

import com.universidad.modelo.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Profesor
 * Usa DB.java para conexiones dinámicas
 */
public class ProfesorDAO {

    public List<Profesor> obtenerTodos() throws SQLException {
        List<Profesor> profesores = new ArrayList<>();

        String sql = """
                    SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipo_contrato
                    FROM profesor pr
                    JOIN persona p ON pr.id = p.id
                """;

        try (Connection conn = DB.getConnection();
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
        String sql = """
                    SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipo_contrato
                    FROM profesor pr
                    JOIN persona p ON pr.id = p.id
                    WHERE p.id = ?
                """;

        try (Connection conn = DB.getConnection();
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
