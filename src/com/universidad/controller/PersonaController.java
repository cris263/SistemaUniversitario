package com.universidad.controller;

import com.universidad.dto.PersonaDTO;
import com.universidad.mapper.PersonaMapper;
import com.universidad.modelo.Persona;
import com.universidad.servicio.InscripcionesPersonas;
import com.universidad.ui.personas.PersonaPanel;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PersonaController {
    private final InscripcionesPersonas servicioPersonas;
    private final PersonaPanel view;

    public PersonaController(PersonaPanel view) {
        this.view = view;
        this.servicioPersonas = new InscripcionesPersonas();
    }

    // Guardar nueva persona
    public void guardarPersona(PersonaDTO personaDTO) {
        try {
            // Crear una implementación concreta de Persona
            Persona persona = new Persona(
                    personaDTO.getId(),
                    personaDTO.getNombres(),
                    personaDTO.getApellidos(),
                    personaDTO.getEmail()
            ) {};

            servicioPersonas.guardarInformacion(persona);
            view.mostrarExito("Persona guardada correctamente");
            view.limpiarCampos();
            cargarPersonas();
        } catch (SQLException ex) {
            view.mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    // Actualizar persona existente
    public void actualizarPersona(PersonaDTO personaDTO) {
        try {
            Persona persona = new Persona(
                    personaDTO.getId(),
                    personaDTO.getNombres(),
                    personaDTO.getApellidos(),
                    personaDTO.getEmail()
            ) {};

            servicioPersonas.actualizar(persona);
            view.mostrarExito("Persona actualizada correctamente");
            view.limpiarCampos();
            cargarPersonas();
        } catch (SQLException ex) {
            view.mostrarError("Error al actualizar: " + ex.getMessage());
        }
    }

    // Eliminar persona
    public void eliminarPersona(Long id) {
        try {
            servicioPersonas.eliminar(id);
            view.mostrarExito("Persona eliminada correctamente");
            view.limpiarCampos();
            cargarPersonas();
        } catch (SQLException ex) {
            view.mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

    // Cargar todas las personas
    public void cargarPersonas() {
        try {
            List<Persona> personas = servicioPersonas.cargarDatos();
            List<PersonaDTO> personasDTO = personas.stream()
                    .map(PersonaMapper::toDTO)
                    .collect(Collectors.toList());

            view.actualizarTabla(personasDTO);
        } catch (SQLException ex) {
            view.mostrarError("Error al cargar datos: " + ex.getMessage());
        }
    }
}