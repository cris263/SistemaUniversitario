package com.universidad.controller;

import com.universidad.dto.CursoProfesorDTO;
import com.universidad.mapper.CursoProfesorMapper;
import com.universidad.modelo.CursoProfesor;
import com.universidad.modelo.Profesor;
import com.universidad.modelo.Curso;
import com.universidad.servicio.CursosProfesores;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CursoProfesorController {
    private final CursosProfesores servicioCursoProfesor;

    public CursoProfesorController(CursosProfesores cursosProfesores) {
        this.servicioCursoProfesor = cursosProfesores;
    }

    public boolean asignarProfesor(CursoProfesorDTO cursoProfesorDTO) throws SQLException {
        // Convertir DTO a entidad y usar el servicio existente
        CursoProfesor cursoProfesor = CursoProfesorMapper.toEntity(cursoProfesorDTO);
        servicioCursoProfesor.inscribir(cursoProfesor);
        return true;
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
    public boolean eliminarAsignacion(Long profesorId, Long cursoId, int anio, int semestre) throws SQLException {
        // Crear objetos relacionados
        Profesor profesor = new Profesor();
        profesor.setId(profesorId);

        Curso curso = new Curso();
        curso.setId(cursoId);

        // Usar el constructor directamente
        CursoProfesor cursoProfesor = new CursoProfesor(profesor, anio, semestre, curso);

        // Eliminar
        servicioCursoProfesor.eliminar(cursoProfesor);
        return true;
    }

    public boolean actualizarAsignacion(Long profesorId, Long cursoId, int anio, int semestre,
                                     int nuevoAnio, int nuevoSemestre) throws SQLException {
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
        return true;
    }



    public List<CursoProfesorDTO> cargarAsignaciones() throws SQLException {
        List<CursoProfesor> asignaciones = servicioCursoProfesor.cargarDatos();
        return asignaciones.stream()
                .map(CursoProfesorMapper::toDTO)
                .collect(Collectors.toList());
    }
}