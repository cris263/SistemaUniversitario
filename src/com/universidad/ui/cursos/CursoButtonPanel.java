package com.universidad.ui.cursos;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de botones de acción para cursos
 * Responsabilidad: Mostrar y manejar eventos de botones
 */
public class CursoButtonPanel extends JPanel {
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;

    public CursoButtonPanel() {
        setupUI();
    }

    private void setupUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnGuardar = new JButton("💾 Guardar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnLimpiar = new JButton("🧹 Limpiar");
        btnRefrescar = new JButton("🔄 Refrescar");

        add(btnGuardar);
        add(btnEliminar);
        add(btnLimpiar);
        add(btnRefrescar);
    }

    /**
     * Agregar listener al botón guardar
     */
    public void onGuardar(Runnable action) {
        btnGuardar.addActionListener(e -> action.run());
    }

    /**
     * Agregar listener al botón eliminar
     */
    public void onEliminar(Runnable action) {
        btnEliminar.addActionListener(e -> action.run());
    }

    /**
     * Agregar listener al botón limpiar
     */
    public void onLimpiar(Runnable action) {
        btnLimpiar.addActionListener(e -> action.run());
    }

    /**
     * Agregar listener al botón refrescar
     */
    public void onRefrescar(Runnable action) {
        btnRefrescar.addActionListener(e -> action.run());
    }
   
}
