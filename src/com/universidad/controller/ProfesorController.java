package com.universidad.controller;

import com.universidad.dto.ProfesorDTO;
import com.universidad.mapper.ProfesorMapper;
import com.universidad.modelo.Persona;
import com.universidad.modelo.Profesor;
import com.universidad.servicio.InscripcionesPersonas;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfesorController {
    private final InscripcionesPersonas servicioPersonas;

    public ProfesorController() {
        this.servicioPersonas = new InscripcionesPersonas();
    }

    // Crear nuevo profesor
    public void crearProfesor(ProfesorDTO profesorDTO) throws SQLException {
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);
        servicioPersonas.inscribir(profesor);
    }

    // Actualizar profesor
    public void actualizarProfesor(ProfesorDTO profesorDTO) throws SQLException {
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);
        servicioPersonas.actualizar(profesor);
    }

    // Eliminar profesor
    public void eliminarProfesor(Long id) throws SQLException {
        servicioPersonas.eliminar(id);
    }

    // Listar todos los profesores
    public List<ProfesorDTO> listarProfesores() throws SQLException {
        return servicioPersonas.cargarDatos().stream()
                .filter(p -> p instanceof Profesor)
                .map(p -> ProfesorMapper.toDTO((Profesor) p))
                .collect(Collectors.toList());
    }
}
