package com.universidad.ui.profesores;

import com.universidad.controller.CursoProfesorController;
import com.universidad.dto.CursoProfesorDTO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CursosProfesoresPanel extends JPanel {
    private CursoProfesorController controller;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public CursosProfesoresPanel(CursoProfesorController cursoProfesorController) {
        // Necesitarás pasar el servicio CursosProfesores aquí
        this.controller = cursoProfesorController;
        setupUI();
        cargarAsignaciones();
    }
    
    private void cargarAsignaciones() {
        try {
            if (controller != null) {
                List<CursoProfesorDTO> asignaciones = controller.cargarAsignaciones();
                actualizarTabla(asignaciones);
            }
        } catch (Exception ex) {
            mostrarError("Error al cargar asignaciones: " + ex.getMessage());
        }
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
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)));

        modeloTabla = new DefaultTableModel(
                new String[] { "Curso ID", "Curso Nombre", "Profesor ID", "Profesor Nombre", "Año", "Semestre" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        btnRefrescar.addActionListener(e -> cargarAsignaciones());
        btnEliminar.addActionListener(e -> eliminarAsignacion());
        btnAsignar.addActionListener(e -> abrirFormularioAsignar());
        btnActualizar.addActionListener(e -> abrirFormularioActualizar());

        panel.add(btnRefrescar);
        panel.add(btnEliminar);
        panel.add(btnAsignar);
        panel.add(btnActualizar);

        return panel;
    }

    public void actualizarTabla(List<CursoProfesorDTO> asignaciones) {
        modeloTabla.setRowCount(0);

        for (CursoProfesorDTO cp : asignaciones) {
            modeloTabla.addRow(new Object[] {
                    cp.getCursoId(),
                    cp.getCursoNombre(),
                    cp.getProfesorId(),
                    cp.getProfesorNombre(),
                    cp.getAnio(),
                    cp.getSemestre()
            });
        }
    }

    private void eliminarAsignacion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione una asignación para eliminar");
            return;
        }

        try {
            Long cursoId = Long.valueOf(tabla.getValueAt(fila, 0).toString());
            Long profesorId = Long.valueOf(tabla.getValueAt(fila, 2).toString());
            int anio = Integer.valueOf(tabla.getValueAt(fila, 4).toString());
            int semestre = Integer.valueOf(tabla.getValueAt(fila, 5).toString());

            // Delegar al controlador
            boolean exito = controller.eliminarAsignacion(profesorId, cursoId, anio, semestre);
            if (exito) {
                mostrarExito("Asignación eliminada correctamente");
                cargarAsignaciones();
            }
        } catch (Exception ex) {
            mostrarError("Error al procesar eliminación: " + ex.getMessage());
        }
    }

    private void abrirFormularioAsignar() {
        try {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            InscribirProfesorDialog dialog = new InscribirProfesorDialog(parentFrame, controller);
            dialog.setVisible(true);

            // Refrescar después de cerrar
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    cargarAsignaciones();
                }
            });
        } catch (Exception ex) {
            mostrarError("Error al abrir formulario: " + ex.getMessage());
        }
    }

    private void abrirFormularioActualizar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione una asignación para actualizar");
            return;
        }

        try {
            Long cursoId = Long.valueOf(tabla.getValueAt(fila, 0).toString());
            Long profesorId = Long.valueOf(tabla.getValueAt(fila, 2).toString());
            int anio = Integer.valueOf(tabla.getValueAt(fila, 4).toString());
            int semestre = Integer.valueOf(tabla.getValueAt(fila, 5).toString());

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Crear DTO para la edición
            CursoProfesorDTO dtoParaEditar = CursoProfesorDTO.builder()
                    .cursoId(cursoId)
                    .profesorId(profesorId)
                    .anio(anio)
                    .semestre(semestre)
                    .build();

            InscribirProfesorDialog dialog = new InscribirProfesorDialog(
                    parentFrame, controller, true, dtoParaEditar);
            dialog.setVisible(true);

            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    cargarAsignaciones();
                }
            });
        } catch (Exception ex) {
            mostrarError("Error al abrir actualización: " + ex.getMessage());
        }
    }

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