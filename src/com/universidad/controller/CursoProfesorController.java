package com.universidad.controller;

import com.universidad.dto.CursoProfesorDTO;
import com.universidad.mapper.CursoProfesorMapper;
import com.universidad.modelo.CursoProfesor;
import com.universidad.modelo.Profesor;
import com.universidad.modelo.Curso;
import com.universidad.servicio.CursosProfesores;
import com.universidad.ui.profesores.CursosProfesoresPanel;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CursoProfesorController {
    private final CursosProfesores servicioCursoProfesor;
    private final CursosProfesoresPanel view;

    public CursoProfesorController(CursosProfesoresPanel view) {
        this.view = view;
        this.servicioCursoProfesor = new CursosProfesores();
    }

    public void asignarProfesor(CursoProfesorDTO cursoProfesorDTO) {
        try {
            // Convertir DTO a entidad y usar el servicio existente
            CursoProfesor cursoProfesor = CursoProfesorMapper.toEntity(cursoProfesorDTO);
            servicioCursoProfesor.inscribir(cursoProfesor);

            view.mostrarExito("Profesor asignado correctamente");
            cargarAsignaciones();
        } catch (SQLException ex) {
            view.mostrarError("Error al asignar: " + ex.getMessage());
        }
    }



    /*public void eliminarAsignacion(Long profesorId, Long cursoId, int anio, int semestre) {
        try {
            // Crear un CursoProfesor con los datos necesarios para eliminación
            CursoProfesor cursoProfesor = new CursoProfesor();
            // Necesitarías setear los objetos Profesor y Curso completos
            // Por ahora usar el metodo existente

            // Si tu servicio tiene un metodo para eliminar
            servicioCursoProfesor.eliminar(cursoProfesor );

            view.mostrarExito("Asignación eliminada correctamente");
            cargarAsignaciones();
        } catch (SQLException ex) {
            view.mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

     */
    public void eliminarAsignacion(Long profesorId, Long cursoId, int anio, int semestre) {
        try {
            // Crear objetos relacionados
            Profesor profesor = new Profesor();
            profesor.setId(profesorId);

            Curso curso = new Curso();
            curso.setId(cursoId);

            // Usar el constructor directamente
            CursoProfesor cursoProfesor = new CursoProfesor(profesor, anio, semestre, curso);

            // Eliminar
            servicioCursoProfesor.eliminar(cursoProfesor);

            view.mostrarExito("Asignación eliminada correctamente");
            cargarAsignaciones();
        } catch (SQLException ex) {
            view.mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

    public void actualizarAsignacion(Long profesorId, Long cursoId, int anio, int semestre,
                                     int nuevoAnio, int nuevoSemestre) {
        try {
            // Crear los objetos relacionados
            Profesor profesor = new Profesor();
            profesor.setId(profesorId);

            Curso curso = new Curso();
            curso.setId(cursoId);

            // Construir el objeto original (lo que identifica el registro actual)
            CursoProfesor original = new CursoProfesor(profesor, anio, semestre, curso);

            // Construir el objeto actualizado (mismos profesor y curso, pero con los nuevos valores)
            CursoProfesor actualizado = new CursoProfesor(profesor, nuevoAnio, nuevoSemestre, curso);

            // Llamar al servicio
            servicioCursoProfesor.actualizar(original, actualizado);

            // Notificar a la vista
            view.mostrarExito("✅ Asignación actualizada correctamente.");
            cargarAsignaciones();

        } catch (SQLException e) {
            view.mostrarError("⚠️ Error al actualizar asignación: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void cargarAsignaciones() {
        try {
            List<CursoProfesor> asignaciones = servicioCursoProfesor.cargarDatos();
            List<CursoProfesorDTO> asignacionesDTO = asignaciones.stream()
                    .map(CursoProfesorMapper::toDTO)
                    .collect(Collectors.toList());

            view.actualizarTabla(asignacionesDTO);
        } catch (SQLException ex) {
            view.mostrarError("Error al cargar asignaciones: " + ex.getMessage());
        }
    }
}