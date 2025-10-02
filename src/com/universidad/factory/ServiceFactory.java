package com.universidad.factory;

import com.universidad.servicio.*;

/**
 * Fábrica para crear y gestionar servicios de negocio
 * Implementa el patrón Singleton para cada servicio
 */
public class ServiceFactory {

    private DAOFactory daoFactory;
    
    public ServiceFactory(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public DatabaseService crearDatabaseService() {
        return new DatabaseService(daoFactory.crearDateDAO());
    }
    
    public  CursosInscritos crearCursosInscritos() {
        return new CursosInscritos(daoFactory.crearInscripcionDAO());
    }
    
    public CursosProfesores crearCursosProfesores() {
        return new CursosProfesores(daoFactory.crearCursoProfesorDAO());
    }
    
    public InscripcionesPersonas crearInscripcionesPersonas() {
        return new InscripcionesPersonas(daoFactory.crearPersonaDAO());
    }
}