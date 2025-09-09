package com.universidad.ui.cursos;

import com.universidad.modelo.Inscripcion;
import com.universidad.servicio.CursosInscritos;
import com.universidad.persistencia.InscripcionDAO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CursosInscritosPanel extends JPanel {
    private CursosInscritos cursosInscritos;
    private JTable tabla;
    private DefaultTableModel modeloTablaCursosIns;

    public CursosInscritosPanel() {
        // Crear el servicio con el DAO
        this.cursosInscritos = new CursosInscritos(new InscripcionDAO());

        setupUI();
        cargarInscripciones();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel cursoInscripcionTable = createCursosInscritosTablePanel();
        JPanel panelBotones = createButtonPanel();

        add(cursoInscripcionTable, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel createCursosInscritosTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Cursos Inscritos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)));

        modeloTablaCursosIns = new DefaultTableModel(
                new String[] { "Curso ID", "Curso Nombre", "Estudiante ID", "Estudiante Nombre", "Año", "Semestre" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable
            }
        };

        tabla = new JTable(modeloTablaCursosIns);
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
        JButton btnCrear = new JButton("➕ Crear");
        JButton btnActualizar = new JButton("✏️ Actualizar");

        btnRefrescar.addActionListener(e -> cargarInscripciones());
        btnEliminar.addActionListener(e -> eliminarInscripcion());

        // Crear
        btnCrear.addActionListener(e -> {
            InscribirFrame inscribirFrame = new InscribirFrame();
            inscribirFrame.setVisible(true);
        });

        // Actualizar
        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                mostrarAdvertencia("Seleccione una inscripción para actualizar");
                return;
            }

            try {
                Long cursoId = Long.valueOf(tabla.getValueAt(fila, 0).toString());
                Long estudianteId = Long.valueOf(tabla.getValueAt(fila, 2).toString());
                int anio = Integer.parseInt(tabla.getValueAt(fila, 4).toString());
                int semestre = Integer.parseInt(tabla.getValueAt(fila, 5).toString());

                // Aquí abrimos el frame en modo edición
                InscribirFrame actualizarFrame = new InscribirFrame(true, estudianteId, cursoId, anio, semestre);
                actualizarFrame.setVisible(true);

                // Cuando se cierre, refrescamos tabla
                actualizarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        cargarInscripciones();
                    }
                });

            } catch (Exception ex) {
                mostrarError("Error al abrir actualización: " + ex.getMessage());
            }
        });

        panel.add(btnRefrescar);
        panel.add(btnEliminar);
        panel.add(btnCrear);
        panel.add(btnActualizar);

        return panel;
    }

    private void cargarInscripciones() {
        try {
            modeloTablaCursosIns.setRowCount(0); // limpiar tabla
            List<Inscripcion> inscripciones = cursosInscritos.cargarDatos();
            for (Inscripcion ins : inscripciones) {
                modeloTablaCursosIns.addRow(new Object[] {
                        ins.getCurso().getId(),
                        ins.getCurso().getNombre(),
                        ins.getEstudiante().getId(),
                        ins.getEstudiante().getNombres() + " " + ins.getEstudiante().getApellidos(),
                        ins.getAnio(),
                        ins.getSemestre()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar inscripciones: " + ex.getMessage());
        }
    }

    private void eliminarInscripcion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione una inscripción para eliminar");
            return;
        }

        try {
            Long cursoId = Long.valueOf(tabla.getValueAt(fila, 0).toString());
            Long estudianteId = Long.valueOf(tabla.getValueAt(fila, 2).toString());
            int anio = Integer.parseInt(tabla.getValueAt(fila, 4).toString());
            int semestre = Integer.parseInt(tabla.getValueAt(fila, 5).toString());

            cursosInscritos.eliminar(cursoId, estudianteId, anio, semestre);
            mostrarExito("Inscripción eliminada correctamente");
            cargarInscripciones();
        } catch (Exception ex) {
            mostrarError("Error al eliminar inscripción: " + ex.getMessage());
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
