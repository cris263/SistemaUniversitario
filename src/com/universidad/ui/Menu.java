package com.universidad.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Menu extends JFrame {

   /* public Menu() {
        setTitle("Sistema Universitario - Menú Principal");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnPersonas = new JButton("Gestionar Personas");
        btnPersonas.addActionListener(e -> new PersonaFrame().setVisible(true));

        setLayout(new FlowLayout());
        add(btnPersonas);

    }*/
    public Menu() {
        setTitle("Menú Principal - Sistema Universitario");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con margen
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Caja gris con borde y título
        JPanel panelMenu = new JPanel(new BorderLayout(10, 10));
        panelMenu.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Menú del Sistema",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)
        ));
        panelMenu.setBackground(new Color(245, 245, 245)); // gris claro

        // Mensaje de bienvenida
        JLabel lblBienvenido = new JLabel("BIENVENIDO AL SISTEMA UNIVERSITARIO", SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Dialog", Font.BOLD, 16));
        lblBienvenido.setForeground(new Color(50, 50, 50));

        panelMenu.add(lblBienvenido, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelBotones.setOpaque(false);

        JButton btnPersonas = new JButton("👤 Gestionar Personas");
        JButton btnCursos = new JButton("📘 Gestionar Cursos");
        JButton btnProfesores = new JButton("🎓 Gestionar Profesores");

        Dimension buttonSize = new Dimension(200, 40);
        for (JButton btn : new JButton[]{btnPersonas, btnCursos, btnProfesores}) {
            btn.setPreferredSize(buttonSize);
            panelBotones.add(btn);
        }

        panelMenu.add(panelBotones, BorderLayout.CENTER);

        // Agregar la caja al panel principal
        mainPanel.add(panelMenu, BorderLayout.CENTER);

        add(mainPanel);

        // Eventos de botones
        btnPersonas.addActionListener(e -> {
            new PersonaFrame().setVisible(true);
            dispose();
        });

        btnCursos.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Aquí iría la gestión de cursos (en construcción)");
        });

        btnProfesores.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Aquí iría la gestión de profesores (en construcción)");
        });
    }

}

