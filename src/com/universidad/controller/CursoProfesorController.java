package com.universidad.controller;

import com.universidad.modelo.CursoProfesor;
import com.universidad.servicio.CursosProfesores;

import java.sql.SQLException;
import java.util.List;

public class CursoProfesorController {
    private final CursosProfesores servicioCursoProfesor;

    public CursoProfesorController() {
        this.servicioCursoProfesor = new CursosProfesores();
    }

    // Asignar curso a profesor
    public void asignarCurso(CursoProfesor cursoProfesor) throws SQLException {
        servicioCursoProfesor.inscribir(cursoProfesor);
    }

    // Eliminar asignación
    public void eliminarAsignacion(CursoProfesor cursoProfesor) throws SQLException {
        servicioCursoProfesor.eliminar(cursoProfesor);
    }

    // Guardar información (insertar o actualizar)
    public void guardarInformacion(CursoProfesor cursoProfesor) throws SQLException {
        servicioCursoProfesor.guardarInformacion(cursoProfesor);
    }

    // Listar todos
    public List<CursoProfesor> listar() throws SQLException {
        return servicioCursoProfesor.cargarDatos();
    }

    // Obtener cantidad de asignaciones
    public int cantidad() {
        return servicioCursoProfesor.cantidadActual();
    }
}
