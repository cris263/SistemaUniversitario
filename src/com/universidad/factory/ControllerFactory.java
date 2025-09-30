package com.universidad.factory;

import com.universidad.controller.*;

/**
 * Fábrica para crear y gestionar controladores
 * Implementa el patrón Factory siguiendo la misma estructura que ServiceFactory
 */
public class ControllerFactory implements GenericFactory<ControllerFactory.TipoController, Object> {
    
    // Enum para tipos de controlador
    public enum TipoController {
        CURSO,
        ESTUDIANTE,
        PROFESOR,
        DATABASE,
        // INSCRIPCION,
        // PERSONA,
        // CURSO_PROFESOR
    }
    
    @Override
    public <U> U crear(TipoController tipo, Class<U> claseEsperada) {
        
        Object controller = null;
        
        switch (tipo) {
            case CURSO:
                controller = new CursoController();
                break;
                
            case ESTUDIANTE:
                controller = new EstudianteController();
                break;
                
            case PROFESOR:
                controller = new ProfesorController();
                break;
                
            case DATABASE:
                controller = new DatabaseController();
                break;
                
            // case INSCRIPCION:
            //     controller = new InscripcionController();
            //     break;
                
            // case PERSONA:
            //     controller = new PersonaController();
            //     break;
                
            // case CURSO_PROFESOR:
            //     controller = new CursoProfesorController();
            //     break;
                
            default:
                throw new IllegalArgumentException("Tipo de controlador no soportado: " + tipo);
        }
        
        return claseEsperada.cast(controller);
    }

    
    
    /**
     * Obtiene los tipos de controlador soportados
     */
    @Override
    public TipoController[] getTiposSoportados() {
        return TipoController.values();
    }
    
    /**
     * Métodos de conveniencia para crear controladores específicos (estáticos)
     */
    public static <T> T crearController(TipoController tipo, Class<T> claseEsperada) {
        ControllerFactory factory = new ControllerFactory();
        return factory.crear(tipo, claseEsperada);
    }
    
    public static CursoController crearCursoController() {
        return crearController(TipoController.CURSO, CursoController.class);
    }
    
    public static EstudianteController crearEstudianteController() {
        return crearController(TipoController.ESTUDIANTE, EstudianteController.class);
    }
    
    public static ProfesorController crearProfesorController() {
        return crearController(TipoController.PROFESOR, ProfesorController.class);
    }
    
    // public static InscripcionController crearInscripcionController() {
    //     return crearController(TipoController.INSCRIPCION, InscripcionController.class);
    // }
    
    // public static PersonaController crearPersonaController() {
    //     return crearController(TipoController.PERSONA, PersonaController.class);
    // }
    
    // public static CursoProfesorController crearCursoProfesorController() {
    //     return crearController(TipoController.CURSO_PROFESOR, CursoProfesorController.class);
    // }
    
    /**
     * Método de conveniencia para mostrar todos los tipos soportados
     */
    public static void mostrarTiposSoportados() {
        System.out.println("📋 Tipos de controlador soportados:");
        for (TipoController tipo : TipoController.values()) {
            System.out.println("  - " + tipo.name());
        }
    }
}
