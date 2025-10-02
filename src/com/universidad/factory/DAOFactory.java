package com.universidad.factory;

import com.universidad.persistencia.*;

/**
 * Fábrica para crear y gestionar DAOs (Data Access Objects)
 * Implementa el patrón Singleton para cada DAO
 */
public class DAOFactory {
    
    public DAOFactory(){}
    
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