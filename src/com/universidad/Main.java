package com.universidad;

import com.universidad.modelo.Persona;
import com.universidad.persistencia.DatabaseConnection;
import com.universidad.servicio.InscripcionesPersonas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class Main extends JFrame {
    private InscripcionesPersonas inscripcionesPersonas;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombres, txtApellidos, txtEmail;
    
    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Main() {
        inscripcionesPersonas = new InscripcionesPersonas();
        
        // try {
        //     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        // } catch (Exception e) {
        //     // Si no se puede establecer, usar el look and feel por defecto
        // }

        setupUI();
        cargarPersonas();
    }

    private void setupUI() {
        setTitle("Sistema de Gestión de Personas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con márgenes
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel superior con formulario
        JPanel panelForm = createFormPanel();
        
        // Panel central con tabla
        JPanel panelTabla = createTablePanel();
        
        // Panel inferior con botones
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
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Dialog", Font.BOLD, 12)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // ID (solo lectura)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panel.add(txtId, gbc);

        // Nombres
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombres: *"), gbc);
        gbc.gridx = 1;
        txtNombres = new JTextField(15);
        panel.add(txtNombres, gbc);

        // Apellidos
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Apellidos: *"), gbc);
        gbc.gridx = 3;
        txtApellidos = new JTextField(15);
        panel.add(txtApellidos, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email: *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEmail = new JTextField(15);
        panel.add(txtEmail, gbc);

        // Nota de campos obligatorios
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Dialog", Font.ITALIC, 10));
        lblNota.setForeground(Color.GRAY);
        panel.add(lblNota, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Lista de Personas", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Dialog", Font.BOLD, 12)
        ));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombres", "Apellidos", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(25);
        
        // Configurar anchos de columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Listener para cargar datos en el formulario al seleccionar
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

        // Configurar botones
        Dimension buttonSize = new Dimension(120, 35);
        for (JButton btn : new JButton[]{btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar}) {
            btn.setPreferredSize(buttonSize);
        }

        btnGuardar.addActionListener(e -> guardarPersona());
        btnActualizar.addActionListener(e -> actualizarPersona());
        btnEliminar.addActionListener(e -> eliminarPersona());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnRefrescar.addActionListener(e -> cargarPersonas());

        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnRefrescar);

        return panel;
    }

    private boolean validarCampos() {
        // Validar campos obligatorios
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarError("El campo 'Nombres' es obligatorio");
            txtNombres.requestFocus();
            return false;
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarError("El campo 'Apellidos' es obligatorio");
            txtApellidos.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            mostrarError("El campo 'Email' es obligatorio");
            txtEmail.requestFocus();
            return false;
        }

        // Validar formato de email
        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            mostrarError("El formato del email no es válido");
            txtEmail.requestFocus();
            return false;
        }

        // Validar longitud de campos
        if (txtNombres.getText().trim().length() < 2) {
            mostrarError("Los nombres deben tener al menos 2 caracteres");
            txtNombres.requestFocus();
            return false;
        }

        if (txtApellidos.getText().trim().length() < 2) {
            mostrarError("Los apellidos deben tener al menos 2 caracteres");
            txtApellidos.requestFocus();
            return false;
        }

        return true;
    }

    private void guardarPersona() {
        if (!validarCampos()) return;

        try {
            Persona persona = new Persona(
                null,
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtEmail.getText().trim()
            ) {};
            
            inscripcionesPersonas.guardarInformacion(persona);
            mostrarExito("Persona guardada correctamente");
            limpiarCampos();
            cargarPersonas();
        } catch (SQLException ex) {
            mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    private void eliminarPersona() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione una persona para eliminar");
            return;
        }

        String nombres = tabla.getValueAt(fila, 1).toString();
        String apellidos = tabla.getValueAt(fila, 2).toString();
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar a " + nombres + " " + apellidos + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.parseLong(tabla.getValueAt(fila, 0).toString());
                inscripcionesPersonas.eliminar(id);
                mostrarExito("Persona eliminada correctamente");
                limpiarCampos();
                cargarPersonas();
            } catch (SQLException ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void actualizarPersona() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAdvertencia("Seleccione una persona para actualizar");
            return;
        }

        if (!validarCampos()) return;

        try {
            Long id = Long.valueOf(txtId.getText().trim());
            Persona persona = new Persona(
                id, 
                txtNombres.getText().trim(), 
                txtApellidos.getText().trim(), 
                txtEmail.getText().trim()
            ) {};
            
            inscripcionesPersonas.actualizar(persona);
            mostrarExito("Persona actualizada correctamente");
            limpiarCampos();
            cargarPersonas();
        } catch (SQLException ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            mostrarError("ID inválido");
        }
    }

    private void cargarPersonas() {
        try {
            modeloTabla.setRowCount(0);
            List<Persona> personas = inscripcionesPersonas.cargarDatos();
            for (Persona p : personas) {
                modeloTabla.addRow(new Object[]{
                    p.getId(), 
                    p.getNombres(), 
                    p.getApellidos(), 
                    p.getEmail()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar datos: " + ex.getMessage());
        }
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

    private void limpiarCampos() {
        txtId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        tabla.clearSelection();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this, 
            mensaje, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            this, 
            mensaje, 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
            this, 
            mensaje, 
            "Advertencia", 
            JOptionPane.WARNING_MESSAGE
        );
    }

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Error al inicializar la aplicación: " + e.getMessage(), 
                    "Error Fatal", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}