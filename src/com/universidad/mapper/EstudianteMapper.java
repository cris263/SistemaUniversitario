package com.universidad.mapper;

import com.universidad.dto.EstudianteDTO;
import com.universidad.modelo.Estudiante;

public class EstudianteMapper {

    // Convierte de Entidad -> DTO
    public static EstudianteDTO toDTO(Estudiante estudiante) {
        if (estudiante == null) return null;

        return new EstudianteDTO(
                estudiante.getId(),
                estudiante.getNombres(),
                estudiante.getApellidos(),
                estudiante.getEmail(),
                estudiante.getCodigo(),
                estudiante.getPrograma(),
                estudiante.getActivo(),
                estudiante.getPromedio()
        );
    }

    // Convierte de DTO -> Entidad
    public static Estudiante toEntity(EstudianteDTO dto) {
        if (dto == null) return null;

        Estudiante estudiante = new Estudiante();
        estudiante.setId(dto.getId());
        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setEmail(dto.getEmail());
        estudiante.setCodigo(dto.getCodigo());
        estudiante.setPrograma(dto.getPrograma());
        estudiante.setActivo(dto.getActivo());
        estudiante.setPromedio(dto.getPromedio());

        return estudiante;
    }
}
