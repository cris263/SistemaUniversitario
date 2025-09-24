package com.universidad.controller;

import com.universidad.dto.EstudianteDTO;
import com.universidad.mapper.EstudianteMapper;
import com.universidad.modelo.Estudiante;
import com.universidad.servicio.InscripcionesPersonas;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EstudianteController {
    private InscripcionesPersonas servicioPersonas;

    public EstudianteController() {
        this.servicioPersonas = new InscripcionesPersonas();
    }

    // Crear nuevo estudiante
    public void crearEstudiante(EstudianteDTO estudianteDTO) throws SQLException {
        Estudiante estudiante = EstudianteMapper.toEntity(estudianteDTO);
        servicioPersonas.inscribir(estudiante);
    }

    // Actualizar estudiante
    public void actualizarEstudiante(EstudianteDTO estudianteDTO) throws SQLException {
        Estudiante estudiante = EstudianteMapper.toEntity(estudianteDTO);
        servicioPersonas.actualizar(estudiante);
    }

    // Eliminar estudiante
    public void eliminarEstudiante(Long id) throws SQLException {
        servicioPersonas.eliminar(id);
    }

    // Listar todos los estudiantes
    public List<EstudianteDTO> listarEstudiantes() throws SQLException {
        return servicioPersonas.cargarDatos().stream()
                .filter(p -> p instanceof Estudiante)
                .map(p -> EstudianteMapper.toDTO((Estudiante) p))
                .collect(Collectors.toList());
    }
}
