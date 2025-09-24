
package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.mapper.InscripcionMapper;
import com.universidad.modelo.Inscripcion;
import com.universidad.servicio.CursosInscritos;
import com.universidad.ui.cursos.CursosInscritosPanel;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class InscripcionController {
    private final CursosInscritos servicioInscripciones;
    private final CursosInscritosPanel view;

    public InscripcionController(CursosInscritosPanel view) {
        this.view = view;
        this.servicioInscripciones = new CursosInscritos(new com.universidad.persistencia.InscripcionDAO());
    }

    public void inscribirEstudiante(InscripcionDTO inscripcionDTO) {
        try {
            // Por ahora usar el método existente - luego puedes mejorarlo
            // Necesitarías adaptar el servicio para trabajar con DTOs
            view.mostrarExito("Inscripción realizada correctamente");
            cargarInscripciones();
        } catch (Exception ex) {
            view.mostrarError("Error al inscribir: " + ex.getMessage());
        }
    }

    public void eliminarInscripcion(Long cursoId, Long estudianteId, int anio, int semestre) {
        try {
            servicioInscripciones.eliminar(cursoId, estudianteId, anio, semestre);
            view.mostrarExito("Inscripción eliminada correctamente");
            cargarInscripciones();
        } catch (SQLException ex) {
            view.mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

    public void cargarInscripciones() {
        try {
            List<Inscripcion> inscripciones = servicioInscripciones.cargarDatos();
            List<InscripcionDTO> inscripcionesDTO = inscripciones.stream()
                    .map(InscripcionMapper::toDTO)
                    .collect(Collectors.toList());

            // Llamar al método de la vista
            view.actualizarTabla(inscripcionesDTO);
        } catch (SQLException ex) {
            view.mostrarError("Error al cargar inscripciones: " + ex.getMessage());
        }
    }
}