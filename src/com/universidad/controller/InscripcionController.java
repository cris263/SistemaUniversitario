
package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.mapper.InscripcionMapper;
import com.universidad.modelo.Inscripcion;
import com.universidad.servicio.CursosInscritos;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class InscripcionController {
    private final CursosInscritos servicioInscripciones;

    public InscripcionController(CursosInscritos cursosInscritos) {
        this.servicioInscripciones = cursosInscritos;
    }

    public boolean inscribirEstudiante(InscripcionDTO inscripcionDTO) throws SQLException {
        Inscripcion inscripcion = InscripcionMapper.toEntity(inscripcionDTO);
        servicioInscripciones.inscribirCurso(inscripcion);
        return true;
    }

    public boolean eliminarInscripcion(Long cursoId, Long estudianteId, int anio, int semestre) throws SQLException {
        servicioInscripciones.eliminar(cursoId, estudianteId, anio, semestre);
        return true;
    }

    public List<InscripcionDTO> cargarInscripciones() throws SQLException {
        List<Inscripcion> inscripciones = servicioInscripciones.cargarDatos();
        return inscripciones.stream()
                .map(InscripcionMapper::toDTO)
                .collect(Collectors.toList());
    }
}