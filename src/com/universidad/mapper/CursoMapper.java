package com.universidad.mapper;

import com.universidad.dto.CursoDTO;
import com.universidad.modelo.Curso;

public class CursoMapper {

    public static CursoDTO toDTO(Curso curso) {
        if (curso == null) return null;

        return CursoDTO.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .activo(curso.getActivo())
                .build();
    }

    public static Curso toEntity(CursoDTO dto) {
        if (dto == null) return null;

        Curso curso = new Curso();
        curso.setId(dto.getId());
        curso.setNombre(dto.getNombre());
        curso.setActivo(dto.getActivo());

        return curso;
    }
}