package com.universidad.factory;

import java.sql.Connection;
import java.sql.SQLException;

import com.universidad.persistencia.*;

/**
 * Fábrica para crear y gestionar DAOs (Data Access Objects)
 * Implementa el patrón Singleton para cada DAO
 */
public class DAOFactory {

    private static Connection connection;
    
    public DAOFactory() {
        refreshConnection();
    }
    
    /**
     * Refrescar la conexión (llamar después de cambiar la BD)
     */
    public void refreshConnection() {
        try {
            // Cerrar conexión anterior si existe
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            // Obtener nueva conexión
            connection = DB.getConnection();
            System.out.println("🔄 Conexión refrescada a: " + DB.getActiveDatabaseType());
        } catch (SQLException e) {
            System.err.println("❌ Error al refrescar conexión: " + e.getMessage());
        }
    }
    
    public CursoDAO crearCursoDAO() {
        return new CursoDAO();
    }

    public CursoProfesorDAO crearCursoProfesorDAO() {
        return new CursoProfesorDAO();
    }
    
    public PersonaDAO crearPersonaDAO() {
        return new PersonaDAO();
    }
    
    public EstudianteDAO crearEstudianteDAO() {
        return new EstudianteDAO();
    }
    
    public ProfesorDAO crearProfesorDAO() {
        return new ProfesorDAO();
    }
    public InscripcionDAO crearInscripcionDAO() {
        return new InscripcionDAO();
    }

    public DateDAO crearDateDAO(){
        return new DateDAO();
    }
}