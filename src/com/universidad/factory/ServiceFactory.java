package com.universidad.factory;

import com.universidad.servicio.*;

/**
 * Fábrica para crear y gestionar servicios de negocio
 * Implementa el patrón Singleton para cada servicio
 */
public class ServiceFactory implements GenericFactory<ServiceFactory.TipoServicio, Object> {
    
    // Enum para tipos de servicio
    public enum TipoServicio {
        CURSOS_INSCRITOS,
        CURSOS_PROFESORES, 
        INSCRIPCIONES_PERSONAS,
        DATABASE
    }
    
    @Override
    public <U> U crear(TipoServicio tipo, Class<U> claseEsperada) {
        
        Object servicio = null;
        
        switch (tipo) {
            case CURSOS_INSCRITOS:
                servicio = new CursosInscritos();
                break;
                
            case CURSOS_PROFESORES:
                servicio = new CursosProfesores();
                break;
                
            case INSCRIPCIONES_PERSONAS:
                servicio = new InscripcionesPersonas();
                break;
           
            case DATABASE:
                servicio = new DatabaseService();
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de servicio no soportado: " + tipo);
        }
        
        return claseEsperada.cast(servicio);
    }
    
    /**
     * Obtiene los tipos de servicio soportados
     */
    @Override
    public TipoServicio[] getTiposSoportados() {
        return TipoServicio.values();
    }
    
    /**
     * Métodos de conveniencia para crear servicios específicos (estáticos)
     */
    public static <T> T crearServicio(TipoServicio tipo, Class<T> claseEsperada) {
        ServiceFactory factory = new ServiceFactory();
        return factory.crear(tipo, claseEsperada);
    }
    
    public static DatabaseService crearDatabaseService() {
        return crearServicio(TipoServicio.DATABASE, DatabaseService.class);
    }
    public static CursosInscritos crearCursosInscritos() {
        return crearServicio(TipoServicio.CURSOS_INSCRITOS, CursosInscritos.class);
    }
    
    public static CursosProfesores crearCursosProfesores() {
        return crearServicio(TipoServicio.CURSOS_PROFESORES, CursosProfesores.class);
    }
    
    public static InscripcionesPersonas crearInscripcionesPersonas() {
        return crearServicio(TipoServicio.INSCRIPCIONES_PERSONAS, InscripcionesPersonas.class);
    }
}