package com.universidad.mapper;

import com.universidad.dto.CursoProfesorDTO;
import com.universidad.modelo.CursoProfesor;

public class CursoProfesorMapper {

    public static CursoProfesorDTO toDTO(CursoProfesor cursoProfesor) {
        if (cursoProfesor == null) return null;

        return CursoProfesorDTO.builder()
                .profesorId(cursoProfesor.getProfesor().getId())
                .profesorNombre(cursoProfesor.getProfesor().getNombres() + " " +
                        cursoProfesor.getProfesor().getApellidos())
                .cursoId(cursoProfesor.getCurso().getId())
                .cursoNombre(cursoProfesor.getCurso().getNombre())
                .anio(cursoProfesor.getAnio())
                .semestre(cursoProfesor.getSemestre())
                .build();
    }

    public static CursoProfesor toEntity(CursoProfesorDTO dto) {
        if (dto == null) return null;

        // Implementación básica - en un caso real necesitarías
        // cargar las entidades completas
        CursoProfesor cursoProfesor = new CursoProfesor();
        cursoProfesor.setAnio(dto.getAnio());
        cursoProfesor.setSemestre(dto.getSemestre());

        return cursoProfesor;
    }
}