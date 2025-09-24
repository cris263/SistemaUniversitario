package com.universidad.mapper;

import com.universidad.dto.CursoDTO;
import com.universidad.modelo.Curso;

public class CursoMapper {

    public static CursoDTO toDTO(Curso curso) {
        if (curso == null) return null;

        return CursoDTO.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .programa(curso.getPrograma())
                .activo(curso.getActivo())
                .build();
    }

    public static Curso toEntity(CursoDTO dto) {
        if (dto == null) return null;

        return new Curso(
                dto.getId(),
                dto.getNombre(),
                dto.getPrograma(),
                dto.getActivo()
        );
    }
}
