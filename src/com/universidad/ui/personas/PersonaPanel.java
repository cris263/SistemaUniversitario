
package com.universidad.ui.personas;

import com.universidad.controller.PersonaController;
import com.universidad.dto.PersonaDTO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class PersonaPanel extends JPanel {
    private PersonaController controller;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombres, txtApellidos, txtEmail;

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public PersonaPanel() {
        this.controller = new PersonaController(this);
        setupUI();
        controller.cargarPersonas();
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelForm = createFormPanel();
        JPanel panelTabla = createTablePanel();
        JPanel panelBotones = createButtonPanel();

        mainPanel.add(panelForm, BorderLayout.NORTH);
        mainPanel.add(panelTabla, BorderLayout.CENTER);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Información de la Persona",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panel.add(txtId, gbc);

        // Nombres
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Nombres: *"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombres = new JTextField();
        panel.add(txtNombres, gbc);

        // Apellidos
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Apellidos: *"), gbc);

        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidos = new JTextField();
        panel.add(txtApellidos, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email: *"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = new JTextField();
        panel.add(txtEmail, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Lista de Personas",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)
        ));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombres", "Apellidos", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(25);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnGuardar = new JButton("💾 Guardar");
        JButton btnActualizar = new JButton("✏️ Actualizar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        JButton btnLimpiar = new JButton("🧹 Limpiar");
        JButton btnRefrescar = new JButton("🔄 Refrescar");

        btnGuardar.addActionListener(e -> guardarPersona());
        btnActualizar.addActionListener(e -> actualizarPersona());
        btnEliminar.addActionListener(e -> eliminarPersona());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnRefrescar.addActionListener(e -> controller.cargarPersonas());

        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnRefrescar);

        return panel;
    }

    // Métodos que serán llamados por el controlador
    public void actualizarTabla(List<PersonaDTO> personas) {
        modeloTabla.setRowCount(0);
        for (PersonaDTO p : personas) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombres(),
                    p.getApellidos(),
                    p.getEmail()
            });
        }
    }

    private boolean validarCampos() {
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarError("El campo 'Nombres' es obligatorio");
            return false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarError("El campo 'Apellidos' es obligatorio");
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            mostrarError("El campo 'Email' es obligatorio");
            return false;
        }
        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            mostrarError("El formato del email no es válido");
            return false;
        }
        return true;
    }

    private void guardarPersona() {
        if (!validarCampos()) return;

        PersonaDTO dto = PersonaDTO.builder()
                .nombres(txtNombres.getText().trim())
                .apellidos(txtApellidos.getText().trim())
                .email(txtEmail.getText().trim())
                .build();

        controller.guardarPersona(dto);
    }

    private void actualizarPersona() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAdvertencia("Seleccione una persona para actualizar");
            return;
        }
        if (!validarCampos()) return;

        PersonaDTO dto = PersonaDTO.builder()
                .id(Long.valueOf(txtId.getText().trim()))
                .nombres(txtNombres.getText().trim())
                .apellidos(txtApellidos.getText().trim())
                .email(txtEmail.getText().trim())
                .build();

        controller.actualizarPersona(dto);
    }

    private void eliminarPersona() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione una persona para eliminar");
            return;
        }

        Long id = Long.parseLong(tabla.getValueAt(fila, 0).toString());
        controller.eliminarPersona(id);
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(tabla.getValueAt(fila, 0).toString());
            txtNombres.setText(tabla.getValueAt(fila, 1).toString());
            txtApellidos.setText(tabla.getValueAt(fila, 2).toString());
            txtEmail.setText(tabla.getValueAt(fila, 3).toString());
        }
    }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        tabla.clearSelection();
    }

    // Métodos para mostrar mensajes - llamados por el controlador
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}