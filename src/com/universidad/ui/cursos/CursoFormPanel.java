package com.universidad.ui.cursos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel de formulario para crear/editar cursos
 * Responsabilidad: Mostrar y capturar datos del formulario
 */
public class CursoFormPanel extends JPanel {
    private JTextField txtId;
    private JTextField txtNombre;
    private JCheckBox chkActivo;

    public CursoFormPanel() {
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Información del Curso",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("ID:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtId = new JTextField(10);
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        add(txtId, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Nombre: *"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombre = new JTextField(30);
        add(txtNombre, gbc);

        // Activo
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Activo:"), gbc);

        gbc.gridx = 1;
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true);
        add(chkActivo, gbc);
    }

    // Getters para obtener los valores del formulario
    public String getId() {
        return txtId.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public boolean isActivo() {
        return chkActivo.isSelected();
    }

    // Setters para cargar datos en el formulario
    public void setId(String id) {
        txtId.setText(id);
    }

    public void setNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    public void setActivo(boolean activo) {
        chkActivo.setSelected(activo);
    }

    // Limpiar formulario
    public void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        chkActivo.setSelected(true);
    }

    // Validación
    public boolean validar() {
        return !getNombre().isEmpty();
    }
}
