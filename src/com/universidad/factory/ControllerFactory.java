package com.universidad.factory;

import com.universidad.controller.*;


/**
 * Fábrica para crear y gestionar controladores
 * Implementa el patrón Factory siguiendo la misma estructura que ServiceFactory
 */
public class ControllerFactory {

    private DAOFactory daoFactory;
    private ServiceFactory serviceFactory;
    
    public ControllerFactory(){
        InternalFactory intFactory = InternalFactory.crearInternalFactory();
        this.daoFactory = intFactory.DAOs();
        this.serviceFactory = intFactory.services();
    }
    
    public CursoController crearCursoController() {
        return new CursoController(daoFactory.crearCursoDAO());
    }

    public EstudianteController crearEstudianteController(){
        return new EstudianteController(daoFactory.crearEstudianteDAO());
    }

    public ProfesorController crearProfesorController() {
        return new ProfesorController(serviceFactory.crearInscripcionesPersonas());
    }
    
    public DatabaseController crearDatabaseController() {
        return new DatabaseController(serviceFactory.crearDatabaseService());
    }

    public InscripcionController crearInscripcionController() {
        return new InscripcionController(serviceFactory.crearCursosInscritos());
    }
    
    public PersonaController crearPersonaController() {
        return new PersonaController(serviceFactory.crearInscripcionesPersonas());
    }

    public CursoProfesorController crearCursoProfesorController() {
        return new CursoProfesorController(serviceFactory.crearCursosProfesores());
    }
}
