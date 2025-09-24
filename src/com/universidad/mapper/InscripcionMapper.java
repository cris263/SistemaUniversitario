package com.universidad.mapper;

import com.universidad.dto.InscripcionDTO;
import com.universidad.modelo.Inscripcion;

public class InscripcionMapper {

    public static InscripcionDTO toDTO(Inscripcion inscripcion) {
        if (inscripcion == null) return null;

        return InscripcionDTO.builder()
                .estudianteId(inscripcion.getEstudiante().getId())
                .estudianteNombre(inscripcion.getEstudiante().getNombres() + " " +
                        inscripcion.getEstudiante().getApellidos())
                .cursoId(inscripcion.getCurso().getId())
                .cursoNombre(inscripcion.getCurso().getNombre())
                .anio(inscripcion.getAnio())
                .semestre(inscripcion.getSemestre())
                .build();
    }

    // Para toEntity necesitarías cargar las entidades completas de Estudiante y Curso
    // que es más complejo y generalmente se hace en el servicio
    public static Inscripcion toEntity(InscripcionDTO dto) {
        if (dto == null) return null;

        // Esta implementación es básica - en un caso real necesitarías
        // inyectar los servicios para cargar estudiante y curso completos
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setAnio(dto.getAnio());
        inscripcion.setSemestre(dto.getSemestre());

        return inscripcion;
    }
}