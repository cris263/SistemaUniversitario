package com.universidad.ui;

import javax.swing.JPanel;

import com.universidad.factory.ControllerFactory;
import com.universidad.factory.ExternalFactory;
import com.universidad.ui.cursos.CursosPanel;
import com.universidad.ui.database.DatabasePanel;
import com.universidad.ui.personas.PersonaPanel;
import com.universidad.ui.profesores.CursosProfesoresPanel;

import java.awt.*;

public class Tabs {
    
    private ControllerFactory controllerFactory;

    public Tabs() {
        ExternalFactory ef = ExternalFactory.craeExternalFactory();
        this.controllerFactory = ef.crearControllerFactory();
    }

    public JPanel crearPanelPersonas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Aquí integramos el PersonaPanel en la pestaña
        PersonaPanel personaPanel = new PersonaPanel(controllerFactory.crearPersonaController());
        panel.add(personaPanel, BorderLayout.CENTER);

        return panel;
    }

    public JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        CursosPanel cursosPanel = new CursosPanel(controllerFactory.crearCursoController());
        panel.add(cursosPanel, BorderLayout.CENTER);
        return panel;
    }

    public JPanel crearPanelProfesores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        CursosProfesoresPanel cursosProfesoresPanel = new CursosProfesoresPanel(controllerFactory.crearCursoProfesorController());

        panel.add(cursosProfesoresPanel, BorderLayout.CENTER);
        return panel;
    }

    public JPanel crearPanelBaseDatos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        DatabasePanel databasePanel = new DatabasePanel(controllerFactory.crearDatabaseController());
        panel.add(databasePanel, BorderLayout.CENTER);

        return panel;
    }
}
