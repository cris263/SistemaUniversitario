package com.universidad.factory;

import com.universidad.persistencia.*;

/**
 * Fábrica para crear y gestionar DAOs (Data Access Objects)
 * Implementa el patrón Singleton para cada DAO
 */
public class DAOFactory implements InterfazFactory<DAOFactory.TipoDAO, Object> {
    
    // Enum para tipos de DAO
    public enum TipoDAO {
        CURSO,
        CURSO_PROFESOR,
        ESTUDIANTE,
        INSCRIPCION,
        PERSONA,
        PROFESOR
    }
    
    /**
     * Crea una instancia del DAO solicitado
     * @param tipo Tipo de DAO a crear
     * @param claseEsperada Clase esperada del DAO
     * @return Instancia del DAO
     */
    @Override
    public <U> U crear(TipoDAO tipo, Class<U> claseEsperada) {
        
        Object dao = null;
        
        switch (tipo) {
            case CURSO:
                dao = new CursoDAO();
                break;
                
            case CURSO_PROFESOR:
                dao = new CursoProfesorDAO();
                break;
                
            case ESTUDIANTE:
                dao = new EstudianteDAO();
                break;
                
            case INSCRIPCION:
                dao = new InscripcionDAO();
                break;
                
            case PERSONA:
                dao = new PersonaDAO();
                break;
                
            case PROFESOR:
                dao = new ProfesorDAO();
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de DAO no soportado: " + tipo);
        }
        
        // Verificar que sea del tipo esperado y hacer cast seguro
        return claseEsperada.cast(dao);
    }
    
    /**
     * Obtiene los tipos de DAO soportados
     */
    @Override
    public TipoDAO[] getTiposSoportados() {
        return TipoDAO.values();
    }
    
    /**
     * Métodos de conveniencia para crear DAOs específicos (estáticos)
     */
    public static <T> T crearDAO(TipoDAO tipo, Class<T> claseEsperada) {
        DAOFactory factory = new DAOFactory();
        return factory.crear(tipo, claseEsperada);
    }
    
    public static CursoDAO crearCursoDAO() {
        return crearDAO(TipoDAO.CURSO, CursoDAO.class);
    }
    
    public static PersonaDAO crearPersonaDAO() {
        return crearDAO(TipoDAO.PERSONA, PersonaDAO.class);
    }
    
    public static EstudianteDAO crearEstudianteDAO() {
        return crearDAO(TipoDAO.ESTUDIANTE, EstudianteDAO.class);
    }
    
    public static ProfesorDAO crearProfesorDAO() {
        return crearDAO(TipoDAO.PROFESOR, ProfesorDAO.class);
    }
}