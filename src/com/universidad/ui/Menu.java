package com.universidad.ui;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    private Tabs tabs;

    public Menu() {
        this.tabs = new Tabs();
    }

    public void mainPanel(){
        setTitle("Sistema Universitario - Menú Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con margen
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Agregar componentes al panel principal
        mainPanel.add(this.tabbedPane(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JTabbedPane tabbedPane() {
        JTabbedPane tabbed = new JTabbedPane();
        tabbed.setFont(new Font("Dialog", Font.BOLD, 12));

        // Tab 1: Personas (por defecto)
        JPanel panelPersonas = tabs.crearPanelPersonas();
        tabbed.addTab("👤 Personas", panelPersonas);

        // Tab 2: Cursos
        JPanel panelCursos = tabs.crearPanelCursos();
        tabbed.addTab("📘 Cursos", panelCursos);

        // Tab 3: Profesores
        JPanel panelProfesores = tabs.crearPanelProfesores();
        tabbed.addTab("🎓 Profesores", panelProfesores);

        // Tab 4: Base de Datos
        JPanel panelBaseDatos = tabs.crearPanelBaseDatos();
        tabbed.addTab("🗄️ Base de Datos", panelBaseDatos);

        // Establecer el tab por defecto (Personas)
        tabbed.setSelectedIndex(0);
        
        return tabbed;
    }

}