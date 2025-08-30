package com.universidad.servicio;

import com.universidad.modelo.Persona;
import java.util.ArrayList;
import java.util.List;

public class InscripcionesPersonas {
    private List<Persona> listado;
    
    public InscripcionesPersonas() {
        this.listado = new ArrayList<>();
    }
    
    public void inscribir(Persona persona) {
        if (persona != null) {
            listado.add(persona);
            System.out.println("Persona inscrita: " + persona.getNombres() + " " + persona.getApellidos());
        }
    }
    
    public void eliminar(Persona persona) {
        if (listado.remove(persona)) {
            System.out.println("Persona eliminada: " + persona.getNombres() + " " + persona.getApellidos());
        } else {
            System.out.println("Persona no encontrada en la lista");
        }
    }
    
    public void actualizar(Persona persona) {
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId().equals(persona.getId())) {
                listado.set(i, persona);
                System.out.println("Persona actualizada: " + persona.getNombres() + " " + persona.getApellidos());
                return;
            }
        }
        System.out.println("Persona no encontrada para actualizar");
    }
    
    public void guardarInformacion(Persona persona) {
        if (!listado.contains(persona)) {
            inscribir(persona);
        } else {
            actualizar(persona);
        }
    }
    
    public void cargarDatos() {
        System.out.println("Cargando datos de personas...");
        // Aquí se podrían cargar datos desde BD o archivo
    }
    
    public List<Persona> getListado() {
        return new ArrayList<>(listado);
    }
}