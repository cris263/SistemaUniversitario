package com.universidad.ui.profesores;

import com.universidad.modelo.CursoProfesor;
import com.universidad.persistencia.CursoProfesorDAO;
import com.universidad.servicio.CursosProfesores;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CursosProfesoresPanel extends JPanel {
    private CursosProfesores cursosProfesores;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public CursosProfesoresPanel() {
        this.cursosProfesores = new CursosProfesores();
        setupUI();
        cargarCursosProfesores();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel tablaPanel = createTablePanel();
        JPanel botonesPanel = createButtonPanel();

        add(tablaPanel, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Cursos - Profesores",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)));

        modeloTabla = new DefaultTableModel(
                new String[] { "Curso ID", "Curso Nombre", "Profesor ID", "Profesor Nombre" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnRefrescar = new JButton("🔄 Refrescar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        JButton btnAsignar = new JButton("➕ Asignar");
        JButton btnActualizar = new JButton("✏️ Actualizar");

        btnRefrescar.addActionListener(e -> cargarCursosProfesores());
        btnEliminar.addActionListener(e -> eliminarRelacion());

        // ✅ BOTÓN ASIGNAR - Abrir dialog para nueva asignación
        btnAsignar.addActionListener(e -> {
            try {
                // Obtener el JFrame padre (this debe ser un componente dentro de un JFrame)
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

                // Crear y mostrar el dialog
                InscribirProfesorDialog dialog = new InscribirProfesorDialog(parentFrame);
                dialog.setVisible(true);

                // Refrescar la tabla después de cerrar el dialog
                cargarCursosProfesores();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir el formulario de asignación: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ✅ BOTÓN ACTUALIZAR - Abrir dialog para editar asignación existente
        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                mostrarAdvertencia("Seleccione una relación para actualizar");
                return;
            }

            try {
                // Obtener valores de la fila seleccionada
                // Ajusta estos índices según las columnas de tu tabla
                Long cursoId = Long.valueOf(tabla.getValueAt(fila, 0).toString()); // Columna Curso ID
                Long profesorId = Long.valueOf(tabla.getValueAt(fila, 2).toString()); // Columna Profesor ID
                int anio = Integer.valueOf(tabla.getValueAt(fila, 4).toString()); // Columna Año
                int semestre = Integer.valueOf(tabla.getValueAt(fila, 5).toString()); // Columna Semestre

                // Obtener el JFrame padre
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

                // Crear dialog en modo edición
                InscribirProfesorDialog dialog = new InscribirProfesorDialog(
                        parentFrame,
                        true, // modoEdicion = true
                        profesorId, // profesorIdOriginal
                        cursoId, // cursoIdOriginal
                        anio, // anioOriginal
                        semestre // semestreOriginal
                );

                dialog.setVisible(true);

                // Refrescar la tabla después de cerrar el dialog
                cargarCursosProfesores();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir el formulario de actualización: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnRefrescar);
        panel.add(btnEliminar);
        panel.add(btnAsignar);
        panel.add(btnActualizar);

        return panel;
    }

    private void cargarCursosProfesores() {
        try {
            modeloTabla.setRowCount(0);
            List<CursoProfesor> lista = cursosProfesores.cargarDatos();
            for (CursoProfesor cp : lista) {
                modeloTabla.addRow(new Object[] {
                        cp.getCurso().getId(),
                        cp.getCurso().getNombre(),
                        cp.getProfesor().getId(),
                        cp.getProfesor().getNombres() + " " + cp.getProfesor().getApellidos()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar cursos-profesores: " + ex.getMessage());
        }
    }

    private void eliminarRelacion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione una relación para eliminar");
            return;
        }

        try {
            Long cursoId = Long.valueOf(tabla.getValueAt(fila, 0).toString());
            Long profesorId = Long.valueOf(tabla.getValueAt(fila, 2).toString());

            // cursosProfesores.eliminar(cursoId, profesorId);
            mostrarExito("Relación eliminada correctamente");
            cargarCursosProfesores();
        } catch (Exception ex) {
            mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
