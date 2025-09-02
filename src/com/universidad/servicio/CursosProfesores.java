package com.universidad.servicio;

import com.universidad.modelo.CursoProfesor;

import java.util.ArrayList;
import java.util.List;

public class CursosProfesores implements Servicios {
    private List<CursoProfesor> listado;
    
    public CursosProfesores() {
        this.listado = new ArrayList<>();
    }

    // Inscribir un curso-profesor nuevo
    public void inscribir(CursoProfesor cursoProfesor) {
        listado.add(cursoProfesor);
    }

    // Guardar o actualizar la información
    public void guardarInformacion(CursoProfesor cursoProfesor) {
        boolean encontrado = false;
        for (int i = 0; i < listado.size(); i++) {
            CursoProfesor actual = listado.get(i);

            // Comparamos: mismo profesor, mismo curso, mismo año y semestre
            if (actual.getProfesor() != null && cursoProfesor.getProfesor() != null &&
                actual.getCurso() != null && cursoProfesor.getCurso() != null &&
                // actual.getProfesor().getId().equals(cursoProfesor.getProfesor().getId()) &&
                actual.getCurso().getId().equals(cursoProfesor.getCurso().getId()) &&
                actual.getAnio().equals(cursoProfesor.getAnio()) &&
                actual.getSemestre().equals(cursoProfesor.getSemestre())) {
                
                listado.set(i, cursoProfesor); // reemplaza con la versión nueva
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            inscribir(cursoProfesor);
        }
    }

    // Cargar datos (en este caso, simplemente devolver el listado)
    public List<CursoProfesor> cargarDatos() {
        return listado;
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
        for (CursoProfesor cp : listado) {
            resultado.add(cp.toString());
        }
        return resultado;
    }
}
