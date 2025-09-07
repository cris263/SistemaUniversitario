package com.universidad.servicio;

import com.universidad.modelo.*;
import com.universidad.persistencia.InscripcionDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CursosInscritos {

    private List<Inscripcion> listado;
    private InscripcionDAO inscripcionDAO;

    public CursosInscritos(InscripcionDAO inscripcionDAO) {
        this.listado = new ArrayList<>();
        this.inscripcionDAO = inscripcionDAO;
    }

    // Inscribir un curso
    public void inscribirCurso(Inscripcion inscripcion) throws SQLException {
        if (inscripcion != null) {
            inscripcionDAO.inscribirCurso(inscripcion);
            listado.add(inscripcion);
            System.out.println("Inscripción agregada: " + inscripcion);
        }
    }

    // Eliminar una inscripción por objeto
    public void eliminar(Inscripcion inscripcion) throws SQLException {
        if (inscripcion != null) {
            inscripcionDAO.eliminar(inscripcion);
            listado.remove(inscripcion);
            System.out.println("Inscripción eliminada: " + inscripcion);
        }
    }

    // Eliminar por PK
    public void eliminar(Long cursoId, Long estudianteId, int anio, int semestre) throws SQLException {
        Inscripcion inscripcionAEliminar = null;
        for (Inscripcion ins : listado) {
            if (Objects.equals(ins.getCurso().getId(), cursoId) &&
                    Objects.equals(ins.getEstudiante().getId(), estudianteId) &&
                    ins.getAnio() == anio &&
                    ins.getSemestre() == semestre) {
                inscripcionAEliminar = ins;
                break;
            }
        }
        if (inscripcionAEliminar != null) {
            inscripcionDAO.eliminar(inscripcionAEliminar);
            listado.remove(inscripcionAEliminar);
            System.out.println("Inscripción eliminada: " + inscripcionAEliminar);
        } else {
            System.out.println("Inscripción no encontrada en la lista");
        }
    }

    // Actualizar inscripción
    public void actualizar(Inscripcion inscripcion, Inscripcion nuevaInfo) throws SQLException {
        inscripcionDAO.actualizar(inscripcion, nuevaInfo);
        System.out.println("Actualizando inscripción: " + inscripcion);
        for (int i = 0; i < listado.size(); i++) {
            Inscripcion ins = listado.get(i);
            if (Objects.equals(ins.getCurso().getId(), inscripcion.getCurso().getId()) &&
                    Objects.equals(ins.getEstudiante().getId(), inscripcion.getEstudiante().getId()) &&
                    ins.getAnio() == inscripcion.getAnio() &&
                    ins.getSemestre() == inscripcion.getSemestre()) {
                listado.set(i, nuevaInfo);
                System.out.println("Inscripción actualizada: " + nuevaInfo);
                return;
            }
        }
        System.out.println("Inscripción no encontrada para actualizar");
    }

    // Guardar información (insertar o actualizar)
    public void guardarInformacion(Inscripcion inscripcion) throws SQLException {
        boolean existe = false;
        for (Inscripcion ins : listado) {
            if (Objects.equals(ins.getCurso().getId(), inscripcion.getCurso().getId()) &&
                    Objects.equals(ins.getEstudiante().getId(), inscripcion.getEstudiante().getId()) &&
                    ins.getAnio() == inscripcion.getAnio() &&
                    ins.getSemestre() == inscripcion.getSemestre()) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            inscribirCurso(inscripcion);
        } else {
            System.out.println("La inscripción ya existe, se puede actualizar si es necesario.");
        }
    }

    // Cargar datos desde DAO
    public List<Inscripcion> cargarDatos() throws SQLException {
        System.out.println("Cargando inscripciones desde la base de datos...");
        listado = inscripcionDAO.cargarDatos();
        return listado;
    }

    // Mostrar inscripciones
    public void mostrarInscripciones() throws SQLException {
        if (listado.isEmpty()) {
            cargarDatos();
        }
        for (Inscripcion ins : listado) {
            System.out.println(ins);
        }
    }
}
