package com.universidad.servicio;

import com.universidad.modelo.CursoProfesor;
import com.universidad.persistencia.CursoProfesorDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursosProfesores implements Servicios {
    protected List<CursoProfesor> listado;
    protected CursoProfesorDAO cursoProfesorDAO;

    public CursosProfesores() {
        this.listado = new ArrayList<>();
        this.cursoProfesorDAO = new CursoProfesorDAO();
    }

    public void inscribir(CursoProfesor cursoProfesor) throws SQLException {
        if (cursoProfesor != null) {
            cursoProfesorDAO.guardarCursoProfesor(cursoProfesor);
            listado.add(cursoProfesor);
            System.out.println("CursoProfesor inscrito: " + cursoProfesor);
        }
    }

    public void eliminar(CursoProfesor cursoProfesor) throws SQLException {
        if (cursoProfesor != null) {
            cursoProfesorDAO.guardarCursoProfesor(cursoProfesor);
            listado.add(cursoProfesor);
            System.out.println("CursoProfesor inscrito: " + cursoProfesor);
        }
    }

    public void guardarInformacion(CursoProfesor cursoProfesor) throws SQLException {
        if (!listado.contains(cursoProfesor)) {
            inscribir(cursoProfesor);
        } else {
            System.out.println("CursoProfesor ya existe: " + cursoProfesor);
        }
    }

    public List<CursoProfesor> cargarDatos() throws SQLException {
        System.out.println("Cargando datos de CursoProfesor...");
        listado = cursoProfesorDAO.obtenerTodos();
        return listado;
    }

    // Métodos de la interfaz Servicios
    @Override
    public String imprimirPosicion(String posicion) {
        try {
            int index = Integer.parseInt(posicion);
            if (index >= 0 && index < listado.size()) {
                return listado.get(index).toString();
            } else {
                return "Posición inválida";
            }
        } catch (NumberFormatException e) {
            return "Error: la posición debe ser un número";
        }
    }

    @Override
    public Integer cantidadActual() {
        return listado.size();
    }

    @Override
    public List<String> imprimirListado() {
        List<String> listadoStr = new ArrayList<>();
        for (CursoProfesor cp : listado) {
            listadoStr.add(cp.toString());
        }
        return listadoStr;
    }
}
