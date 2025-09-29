package com.universidad.ui.database;

import com.universidad.controller.DatabaseController;
import javax.swing.*;
import java.awt.*;

/**
 * Panel para gestionar la selección de base de datos
 */
public class DatabasePanel extends JPanel {
    
    private DatabaseController databaseController;
    private JLabel labelBaseDatosActual;
    private JButton btnH2, btnMySQL, btnOracle;
    
    public DatabasePanel() {
        this.databaseController = new DatabaseController();
        initComponents();
        actualizarEstado();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        
        // Panel superior - Título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(245, 245, 245));
        JLabel titulo = new JLabel("🗄️ Gestión de Base de Datos");
        titulo.setFont(new Font("Dialog", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelTitulo.add(titulo);
        
        // Panel central - Estado actual y botones
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Estado actual
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel panelEstado = new JPanel();
        panelEstado.setBackground(Color.WHITE);
        panelEstado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel labelTitulo = new JLabel("Base de Datos Actual:");
        labelTitulo.setFont(new Font("Dialog", Font.BOLD, 14));
        labelBaseDatosActual = new JLabel("NINGUNA");
        labelBaseDatosActual.setFont(new Font("Dialog", Font.BOLD, 16));
        labelBaseDatosActual.setForeground(new Color(0, 100, 0));
        
        panelEstado.setLayout(new FlowLayout());
        panelEstado.add(labelTitulo);
        panelEstado.add(labelBaseDatosActual);
        
        panelCentral.add(panelEstado, gbc);
        
        // Botones de bases de datos
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Botón H2
        gbc.gridx = 0;
        btnH2 = crearBotonBaseDatos("H2 Database", "💾", new Color(52, 152, 219));
        btnH2.addActionListener(e -> cambiarBaseDatos("h2"));
        panelCentral.add(btnH2, gbc);
        
        // Botón MySQL
        gbc.gridx = 1;
        btnMySQL = crearBotonBaseDatos("MySQL", "🐬", new Color(231, 76, 60));
        btnMySQL.addActionListener(e -> cambiarBaseDatos("mysql"));
        panelCentral.add(btnMySQL, gbc);
        
        // Botón Oracle
        gbc.gridx = 2;
        btnOracle = crearBotonBaseDatos("Oracle", "🔴", new Color(255, 87, 34));
        btnOracle.addActionListener(e -> cambiarBaseDatos("oracle"));
        panelCentral.add(btnOracle, gbc);
        
        // Panel inferior - Información
        JPanel panelInfo = new JPanel();
        panelInfo.setBackground(new Color(245, 245, 245));
        JLabel labelInfo = new JLabel("<html><center>💡 Selecciona una base de datos para usar en el sistema<br/>Los cambios se aplicarán inmediatamente</center></html>");
        labelInfo.setFont(new Font("Dialog", Font.ITALIC, 12));
        labelInfo.setForeground(Color.GRAY);
        labelInfo.setHorizontalAlignment(SwingConstants.CENTER);
        panelInfo.add(labelInfo);
        
        // Agregar paneles al layout principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInfo, BorderLayout.SOUTH);
    }
    
    private JButton crearBotonBaseDatos(String texto, String emoji, Color color) {
        JButton boton = new JButton(emoji + " " + texto);
        boton.setFont(new Font("Dialog", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(150, 60));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    private void cambiarBaseDatos(String nombreBaseDatos) {
        // Mostrar indicador de carga
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        deshabilitarBotones();
        
        // Cambiar en un hilo separado para no bloquear la UI
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return databaseController.cambiarBaseDatos(nombreBaseDatos);
            }
            
            @Override
            protected void done() {
                try {
                    boolean exitoso = get();
                    
                    if (exitoso) {
                        mostrarMensaje("✅ Cambio exitoso a " + nombreBaseDatos.toUpperCase(), 
                                     JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        mostrarMensaje("❌ Error al cambiar a " + nombreBaseDatos.toUpperCase(), 
                                     JOptionPane.ERROR_MESSAGE);
                    }
                    
                    actualizarEstado();
                    
                } catch (Exception e) {
                    mostrarMensaje("❌ Error inesperado: " + e.getMessage(), 
                                 JOptionPane.ERROR_MESSAGE);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                    habilitarBotones();
                }
            }
        };
        
        worker.execute();
    }
    
    private void actualizarEstado() {
        String baseDatosActual = databaseController.obtenerBaseDatosActual();
        labelBaseDatosActual.setText(baseDatosActual);
        
        // Actualizar color según la BD
        switch (baseDatosActual) {
            case "H2":
                labelBaseDatosActual.setForeground(new Color(52, 152, 219));
                break;
            case "MYSQL":
                labelBaseDatosActual.setForeground(new Color(231, 76, 60));
                break;
            case "ORACLE":
                labelBaseDatosActual.setForeground(new Color(255, 87, 34));
                break;
            default:
                labelBaseDatosActual.setForeground(Color.GRAY);
        }
    }
    
    private void deshabilitarBotones() {
        btnH2.setEnabled(false);
        btnMySQL.setEnabled(false);
        btnOracle.setEnabled(false);
    }
    
    private void habilitarBotones() {
        btnH2.setEnabled(true);
        btnMySQL.setEnabled(true);
        btnOracle.setEnabled(true);
    }
    
    private void mostrarMensaje(String mensaje, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, "Base de Datos", tipo);
    }
}