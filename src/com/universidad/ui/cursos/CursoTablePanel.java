package com.universidad.ui.cursos;

import com.universidad.dto.CursoDTO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de tabla para mostrar lista de cursos
 * Responsabilidad: Mostrar cursos en tabla y manejar selección
 */
public class CursoTablePanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public CursoTablePanel() {
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Lista de Cursos",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)
        ));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Actualizar tabla con lista de cursos
     */
    public void actualizarTabla(List<CursoDTO> cursos) {
        modeloTabla.setRowCount(0);
        for (CursoDTO c : cursos) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getActivo() ? "Sí" : "No"
            });
        }
    }

    /**
     * Agregar listener para selección de fila
     */
    public void addSelectionListener(Runnable listener) {
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                listener.run();
            }
        });
    }

    /**
     * Obtener ID del curso seleccionado
     */
    public Long getSelectedId() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            return Long.parseLong(tabla.getValueAt(fila, 0).toString());
        }
        return null;
    }

    /**
     * Obtener nombre del curso seleccionado
     */
    public String getSelectedNombre() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            return tabla.getValueAt(fila, 1).toString();
        }
        return null;
    }

    /**
     * Obtener estado activo del curso seleccionado
     */
    public boolean getSelectedActivo() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            return tabla.getValueAt(fila, 2).toString().equals("Sí");
        }
        return true;
    }

    /**
     * Verificar si hay una fila seleccionada
     */
    public boolean haySeleccion() {
        return tabla.getSelectedRow() >= 0;
    }

    /**
     * Limpiar selección
     */
    public void limpiarSeleccion() {
        tabla.clearSelection();
    }
}
