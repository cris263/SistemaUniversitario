package com.universidad.controller;

import com.universidad.dto.PersonaDTO;
import com.universidad.mapper.PersonaMapper;
import com.universidad.modelo.Persona;
import com.universidad.servicio.InscripcionesPersonas;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PersonaController {
    private final InscripcionesPersonas servicioPersonas;

    public PersonaController(InscripcionesPersonas inscripcionesPersonas) {
        this.servicioPersonas = inscripcionesPersonas;
    }

    // Guardar nueva persona
    public boolean guardarPersona(PersonaDTO personaDTO) throws SQLException {
        // Crear una implementación concreta de Persona
        Persona persona = new Persona(
                personaDTO.getId(),
                personaDTO.getNombres(),
                personaDTO.getApellidos(),
                personaDTO.getEmail()
        ) {};

        servicioPersonas.guardarInformacion(persona);
        return true;
    }

    // Actualizar persona existente
    public boolean actualizarPersona(PersonaDTO personaDTO) throws SQLException {
        Persona persona = new Persona(
                personaDTO.getId(),
                personaDTO.getNombres(),
                personaDTO.getApellidos(),
                personaDTO.getEmail()
        ) {};

        servicioPersonas.actualizar(persona);
        return true;
    }

    // Eliminar persona
    public boolean eliminarPersona(Long id) throws SQLException {
        servicioPersonas.eliminar(id);
        return true;
    }

    // Cargar todas las personas
    public List<PersonaDTO> cargarPersonas() throws SQLException {
        List<Persona> personas = servicioPersonas.cargarDatos();
        return personas.stream()
                .map(PersonaMapper::toDTO)
                .collect(Collectors.toList());
    }
}