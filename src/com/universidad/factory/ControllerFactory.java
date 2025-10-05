package com.universidad.factory;

import com.universidad.controller.*;
import com.universidad.servicio.CursoManager;
import com.universidad.ui.cursos.CursoConsolaObserver; // Ajusta si tu observer está en otro paquete

/**
 * Fábrica para crear y gestionar controladores
 * Implementa el patrón Factory siguiendo la misma estructura que ServiceFactory
 */
public class ControllerFactory {

    private DAOFactory daoFactory;
    private ServiceFactory serviceFactory;

    public ControllerFactory() {
        InternalFactory intFactory = InternalFactory.crearInternalFactory();
        this.daoFactory = intFactory.DAOs();
        this.serviceFactory = intFactory.services();
    }

    // --- CursoController usando CursoManager simplificado ---
    public CursoController crearCursoController() {
        CursoManager cursoManager = new CursoManager();
        // Registrar observer opcional (consola)
        cursoManager.attach(new CursoConsolaObserver());
        return new CursoController(cursoManager);
    }

    // --- Otros controladores se mantienen igual ---
    public EstudianteController crearEstudianteController() {
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
