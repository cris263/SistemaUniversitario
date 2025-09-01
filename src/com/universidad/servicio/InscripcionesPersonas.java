package com.universidad.servicio;


import com.universidad.modelo.Persona;
import com.universidad.persistencia.PersonaDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscripcionesPersonas {
    private List<Persona> listado;
    private PersonaDAO personaDAO;
    
    public InscripcionesPersonas() {
        this.listado = new ArrayList<>();
        this.personaDAO = new PersonaDAO();
    }
    
    public void inscribir(Persona persona) throws SQLException {
        if (persona != null) {
            personaDAO.guardarPersona(persona);
            listado.add(persona);
            System.out.println("Persona inscrita: " + persona.getNombres() + " " + persona.getApellidos());
        }
    }
    
   /* public void eliminar(Persona persona) throws SQLException {
        if (persona != null) {
            personaDAO.eliminarPersona(persona.getId());
            listado.remove(persona);
            System.out.println("Persona eliminada: " + persona.getNombres() + " " + persona.getApellidos());

        }
        else{
            System.out.println("Persona no encontrada en la lista");
        }
       /* if (listado.remove(persona)) {
            System.out.println("Persona eliminada: " + persona.getNombres() + " " + persona.getApellidos());
        } else {
            System.out.println("Persona no encontrada en la lista");
        }*/
    //}

    public void eliminar(Long id) throws SQLException {
        if (id != null) {
            // 1. Buscar la persona en el listado por id
            Persona personaAEliminar = null;
            for (Persona p : listado) {
                if (p.getId() == id) {
                    personaAEliminar = p;
                    break;
                }
            }
            if (personaAEliminar != null) {
                personaDAO.eliminarPersona(id);
                listado.remove(personaAEliminar); // elimina en memoria
                System.out.println("Persona eliminada: " +
                        personaAEliminar.getNombres() + " " + personaAEliminar.getApellidos());
            } else {
                System.out.println("Persona no encontrada en la lista");
            }
        }
    }



    public void actualizar(Persona persona) throws SQLException {
        personaDAO.eliminarPersona(persona.getId());
        personaDAO.guardarPersona(persona);
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId() == persona.getId()) {
                listado.set(i, persona);
                System.out.println("Persona actualizada: " + persona.getNombres() + " " + persona.getApellidos());
                return;
            }
        }
        System.out.println("Persona no encontrada para actualizar");
    }
    
    public void guardarInformacion(Persona persona) throws SQLException {
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