package com.universidad.mapper;

import com.universidad.dto.ProfesorDTO;
import com.universidad.modelo.Profesor;

public class ProfesorMapper {

    public static ProfesorDTO toDTO(Profesor profesor) {
        if (profesor == null) return null;

        return new ProfesorDTO(
                profesor.getId(),
                profesor.getNombres(),
                profesor.getApellidos(),
                profesor.getEmail(),
                profesor.getTipoContrato()
        );
    }

    public static Profesor toEntity(ProfesorDTO dto) {
        if (dto == null) return null;

        Profesor profesor = new Profesor();
        profesor.setId(dto.getId());
        profesor.setNombres(dto.getNombres());
        profesor.setApellidos(dto.getApellidos());
        profesor.setEmail(dto.getEmail());
        profesor.setTipoContrato(dto.getTipoContrato());
        return profesor;
    }
}
