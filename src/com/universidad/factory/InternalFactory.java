package com.universidad.factory;

import com.universidad.factory.DAOFactory.TipoDAO;
import com.universidad.factory.ServiceFactory.TipoServicio;
import com.universidad.persistencia.CursoDAO;
import com.universidad.persistencia.CursoProfesorDAO;
import com.universidad.persistencia.EstudianteDAO;
import com.universidad.persistencia.InscripcionDAO;
import com.universidad.persistencia.PersonaDAO;
import com.universidad.persistencia.ProfesorDAO;
import com.universidad.servicio.CursosInscritos;
import com.universidad.servicio.CursosProfesores;
import com.universidad.servicio.DatabaseService;
import com.universidad.servicio.InscripcionesPersonas;

public class InternalFactory {

    public static class Services {
        public static CursosInscritos crearCursosInscritos() {
            return ServiceFactory.crearServicio(TipoServicio.CURSOS_INSCRITOS, CursosInscritos.class);
        }

        public static CursosProfesores crearCursosProfesores() {
            return ServiceFactory.crearServicio(TipoServicio.CURSOS_PROFESORES, CursosProfesores.class);
        }

        public static DatabaseService crearDatabaseService() {
        return ServiceFactory.crearServicio(TipoServicio.DATABASE, DatabaseService.class);
    }

        public static InscripcionesPersonas crearInscripcionesPersonas() {
            return ServiceFactory.crearServicio(TipoServicio.INSCRIPCIONES_PERSONAS, InscripcionesPersonas.class);
        }
    }

    public static class DAOs {
        public static CursoDAO crearCursoDAO() {
            return DAOFactory.crearDAO(TipoDAO.CURSO, CursoDAO.class);
        }

        public static CursoProfesorDAO crearCursoProfesorDAO() {
            return DAOFactory.crearDAO(TipoDAO.CURSO_PROFESOR, CursoProfesorDAO.class);
        }

        public static EstudianteDAO crearEstudianteDAO() {
            return DAOFactory.crearDAO(TipoDAO.ESTUDIANTE, EstudianteDAO.class);
        }

        public static InscripcionDAO crearInscripcionDAO() {
            return DAOFactory.crearDAO(TipoDAO.INSCRIPCION, InscripcionDAO.class);
        }

        public static PersonaDAO crearPersonaDAO() {
            return DAOFactory.crearDAO(TipoDAO.PERSONA, PersonaDAO.class);
        }

        public static ProfesorDAO crearProfesorDAO() {
            return DAOFactory.crearDAO(TipoDAO.PROFESOR, ProfesorDAO.class);
        }
    }
}
