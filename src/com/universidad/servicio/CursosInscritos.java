package com.universidad.servicio;

import com.universidad.modelo.Inscripcion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.universidad.persistencia.InscripcionDAO;

public class CursosInscritos implements Servicios {
    private List<Inscripcion> listado;
    private InscripcionDAO inscripcionDAO;
    
    public CursosInscritos() {
        this.listado = new ArrayList<>();
        this.inscripcionDAO = new InscripcionDAO();
    }
    
    public void inscribirCurso(Inscripcion inscripcion) {
        if (inscripcion != null) {
            listado.add(inscripcion);
            try {
                inscripcionDAO.guardarInscripcion(inscripcion);
                System.out.println("Curso inscrito correctamente: " + inscripcion.getCurso().getNombre());
            } catch (SQLException e) {
                System.err.println("Error guardando inscripcion: " + e.getMessage());
                listado.remove(inscripcion); // Rollback
            }
        }
    }
    
    public void eliminar(Inscripcion inscripcion) {
        if (listado.remove(inscripcion)) {
            try {
                inscripcionDAO.eliminarInscripcion(
                    inscripcion.getCurso().getId(),
                    inscripcion.getEstudiante().getId(),
                    inscripcion.getAnio(),
                    inscripcion.getSemestre()
                );
                System.out.println("Inscripción eliminada correctamente");
            } catch (SQLException e) {
                System.err.println("Error eliminando inscripción: " + e.getMessage());
                listado.add(inscripcion); // Rollback
            }
        }
    }
    
    public void actualizar(Inscripcion inscripcion) {
        for (int i = 0; i < listado.size(); i++) {
            Inscripcion actual = listado.get(i);
            if (actual.getCurso().getId() == inscripcion.getCurso().getId() &&
                    actual.getEstudiante().getId() == inscripcion.getEstudiante().getId()) {
                listado.set(i, inscripcion);
                System.out.println("Inscripción actualizada correctamente");
                return;
            }
        }
        System.out.println("Inscripción no encontrada para actualizar");
    }
    
    public void guardarInformacion(Inscripcion inscripcion) {
        boolean encontrado = false;
        for (Inscripcion actual : listado) {
            if (actual.getCurso().getId() == inscripcion.getCurso().getId() &&
                    actual.getEstudiante().getId() == inscripcion.getEstudiante().getId()) {
                actualizar(inscripcion);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            inscribirCurso(inscripcion);
        }
    }
    
    public void cargarDatos() {
        try {
            System.out.println("Cargando inscripciones desde la base de datos...");
            listado = inscripcionDAO.obtenerTodasLasInscripciones();
            System.out.println("Cargadas " + listado.size() + " inscripciones");
        } catch (SQLException e) {
            System.err.println("Error cargando datos: " + e.getMessage());
        }
    }
    
    @Override
    public String imprimirPosicion(String posicion) {
        try {
            int pos = Integer.parseInt(posicion);
            if (pos >= 0 && pos < listado.size()) {
                return listado.get(pos).toString();
            }
        } catch (NumberFormatException e) {
            return "Posición inválida";
        }
        return "Posición fuera de rango";
    }
    
    @Override
    public Integer cantidadActual() {
        return listado.size();
    }
    
    @Override
    public List<String> imprimirListado() {
        List<String> resultado = new ArrayList<>();
        for (Inscripcion inscripcion : listado) {
            resultado.add(inscripcion.toString());
        }
        return resultado;
    }
    
    public List<Inscripcion> getListado() {
        return new ArrayList<>(listado);
    }
}