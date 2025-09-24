package com.universidad.mapper;

import com.universidad.dto.PersonaDTO;
import com.universidad.modelo.Persona;

public class PersonaMapper {

    // Convierte de Entidad -> DTO
    public static PersonaDTO toDTO(Persona persona) {
        if (persona == null) return null;

        PersonaDTO dto = new PersonaDTO();
        dto.setId(persona.getId());
        dto.setNombres(persona.getNombres());
        dto.setApellidos(persona.getApellidos());
        dto.setEmail(persona.getEmail());

        return dto;
    }

    // Convierte de DTO -> Entidad
    public static Persona toEntity(PersonaDTO dto) {
        if (dto == null) return null;

        // Como Persona es abstracta, creamos una implementación anónima
        return new Persona(dto.getId(), dto.getNombres(), dto.getApellidos(), dto.getEmail()) {};
    }
}