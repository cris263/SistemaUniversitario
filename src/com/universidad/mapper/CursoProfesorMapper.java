package com.universidad.mapper;

import com.universidad.dto.CursoProfesorDTO;
import com.universidad.modelo.CursoProfesor;
import com.universidad.modelo.Profesor;
import com.universidad.modelo.Curso;

public class CursoProfesorMapper {

    public static CursoProfesor toEntity(CursoProfesorDTO dto) {
        if (dto == null) return null;

        CursoProfesor cp = new CursoProfesor();
        cp.setAnio(dto.getAnio());
        cp.setSemestre(dto.getSemestre());

        // Inicializamos Profesor con ID (nunca null)
        Profesor profesor = new Profesor();
        profesor.setId(dto.getProfesorId());
        cp.setProfesor(profesor);

        // Inicializamos Curso con ID (nunca null)
        Curso curso = new Curso();
        curso.setId(dto.getCursoId());
        cp.setCurso(curso);

        return cp;
    }

    // Convierte de Entity a DTO
    public static CursoProfesorDTO toDTO(CursoProfesor entity) {
        if (entity == null) return null;

        Long profesorId = entity.getProfesor() != null ? entity.getProfesor().getId() : null;
        String profesorNombre = entity.getProfesor() != null
                ? entity.getProfesor().getNombres() + " " + entity.getProfesor().getApellidos()
                : null;

        Long cursoId = entity.getCurso() != null ? entity.getCurso().getId() : null;
        String cursoNombre = entity.getCurso() != null ? entity.getCurso().getNombre() : null;

        return CursoProfesorDTO.builder()
                .profesorId(profesorId)
                .profesorNombre(profesorNombre)
                .cursoId(cursoId)
                .cursoNombre(cursoNombre)
                .anio(entity.getAnio())
                .semestre(entity.getSemestre())
                .build();
    }




}
