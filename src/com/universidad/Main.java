package com.universidad;

import com.universidad.modelo.Persona;
import com.universidad.persistencia.DatabaseConnection;
import com.universidad.persistencia.PersonaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Main extends JFrame {
    private PersonaDAO personaDAO;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombres, txtApellidos, txtEmail;

    public Main() {
        personaDAO = new PersonaDAO();

        setTitle("CRUD Personas");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel superior con formulario
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 5, 5));
        txtId = new JTextField();
        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        txtEmail = new JTextField();

        panelForm.add(new JLabel("ID"));
        panelForm.add(new JLabel("Nombres"));
        panelForm.add(new JLabel("Apellidos"));
        panelForm.add(new JLabel("Email"));
        panelForm.add(txtId);
        panelForm.add(txtNombres);
        panelForm.add(txtApellidos);
        panelForm.add(txtEmail);

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombres", "Apellidos", "Email"}, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnCargar = new JButton("Cargar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCargar);

        // Eventos
        btnGuardar.addActionListener(e -> guardarPersona());
        btnEliminar.addActionListener(e -> eliminarPersona());
        btnActualizar.addActionListener(e -> actualizarPersona());
        btnCargar.addActionListener(e -> cargarPersonas());

        // Layout general
        setLayout(new BorderLayout());
        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // cargar inicial
        cargarPersonas();
    }

    private void guardarPersona() {
        try {
            Persona persona = new Persona(
                Double.parseDouble(txtId.getText()),
                txtNombres.getText(),
                txtApellidos.getText(),
                txtEmail.getText()
            ) {};
            personaDAO.guardarPersona(persona);
            limpiarCampos();
            cargarPersonas();
        } catch (SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarPersona() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            Double id = Double.parseDouble(tabla.getValueAt(fila, 0).toString());
            try {
                personaDAO.eliminarPersona(id);
                cargarPersonas();
            } catch (SQLException ex) {
                mostrarError(ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una persona para eliminar");
        }
    }

    private void actualizarPersona() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            try {
                Double id = Double.parseDouble(txtId.getText());
                Persona persona = new Persona(
                    id,
                    txtNombres.getText(),
                    txtApellidos.getText(),
                    txtEmail.getText()
                ) {};
                // actualización simple: eliminar + insertar de nuevo
                personaDAO.eliminarPersona(id);
                personaDAO.guardarPersona(persona);
                limpiarCampos();
                cargarPersonas();
            } catch (SQLException ex) {
                mostrarError(ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una persona para actualizar");
        }
    }

    private void cargarPersonas() {
        try {
            modeloTabla.setRowCount(0);
            List<Persona> personas = personaDAO.obtenerTodasLasPersonas();
            for (Persona p : personas) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getNombres(), p.getApellidos(), p.getEmail()});
            }
        } catch (SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, "Error: " + mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase(); // crea tablas
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
