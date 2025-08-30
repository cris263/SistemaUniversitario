package com.universidad.servicio;

import com.universidad.modelo.CursoProfesor;

import java.util.ArrayList;
import java.util.List;


public class CursosProfesores implements Servicios {
    private List<CursoProfesor> listado;
    
    public CursosProfesores() {
        this.listado = new ArrayList<>();
    }
    
    public void inscribir(CursoProfesor cursoProfesor) {
        
    }
    
    public void guardarInformacion(CursoProfesor cursoProfesor) {
        // boolean encontrado = false;
        // for (Inscripcion actual : listado) {
        //     if (actual.getCurso().getId().equals(inscripcion.getCurso().getId()) &&
        //         actual.getEstudiante().getId().equals(inscripcion.getEstudiante().getId())) {
        //         actualizar(inscripcion);
        //         encontrado = true;
        //         break;
        //     }
        // }
        // if (!encontrado) {
        //     inscribirCurso(inscripcion);
        // }
    }
    
    public void cargarDatos() {
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
       
        return resultado;
    }
    
}