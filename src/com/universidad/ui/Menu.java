package com.universidad.ui;

import javax.swing.*;

import com.universidad.ui.cursos.CursosInscritosPanel;
import com.universidad.ui.personas.PersonaPanel;
import com.universidad.ui.profesores.CursosProfesoresPanel;

import java.awt.*;

public class Menu extends JFrame {

    public Menu() {
        setTitle("Sistema Universitario - Menú Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con margen
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Crear el JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Dialog", Font.BOLD, 12));

        // Tab 1: Personas (por defecto)
        JPanel panelPersonas = crearPanelPersonas();
        tabbedPane.addTab("👤 Personas", panelPersonas);

        // Tab 2: Cursos
        JPanel panelCursos = crearPanelCursos();
        tabbedPane.addTab("📘 Cursos", panelCursos);

        // Tab 3: Profesores
        JPanel panelProfesores = crearPanelProfesores();
        tabbedPane.addTab("🎓 Profesores", panelProfesores);

        // Establecer el tab por defecto (Personas)
        tabbedPane.setSelectedIndex(0);

        // Agregar componentes al panel principal
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel crearPanelPersonas() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(245, 245, 245));
    
    // Aquí integramos el PersonaPanel en la pestaña
    PersonaPanel personaPanel = new PersonaPanel();
    panel.add(personaPanel, BorderLayout.CENTER);
    
    return panel;
}

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        CursosInscritosPanel cursosInscritosPanel = new CursosInscritosPanel();
        panel.add(cursosInscritosPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelProfesores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        CursosProfesoresPanel cursosProfesoresPanel = new CursosProfesoresPanel();
        
        panel.add(cursosProfesoresPanel, BorderLayout.CENTER);
        return panel;
    }
}