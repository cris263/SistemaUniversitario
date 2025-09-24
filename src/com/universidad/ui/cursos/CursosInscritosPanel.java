/*package com.universidad.ui.cursos;

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

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}*/
package com.universidad.ui.cursos;

import com.universidad.controller.InscripcionController;
import com.universidad.dto.InscripcionDTO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CursosInscritosPanel extends JPanel {
    private InscripcionController controller;
    private JTable tabla;
    private DefaultTableModel modeloTablaCursosIns;

    public CursosInscritosPanel() {
        // Inicializar el controlador pasando esta vista
        this.controller = new InscripcionController(this);
        setupUI();
        // Cargar datos iniciales
        controller.cargarInscripciones();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel cursoInscripcionTable = createCursosInscritosTablePanel();
        JPanel panelBotones = createButtonPanel();

        add(cursoInscripcionTable, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel createCursosInscritosTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Cursos Inscritos",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12)));

        modeloTablaCursosIns = new DefaultTableModel(
                new String[] { "Curso ID", "Curso Nombre", "Estudiante ID", "Estudiante Nombre", "Año", "Semestre" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTablaCursosIns);
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
        JButton btnCrear = new JButton("➕ Crear");
        JButton btnActualizar = new JButton("✏️ Actualizar");

        // Conectar acciones con el controlador
        btnRefrescar.addActionListener(e -> controller.cargarInscripciones());
        btnEliminar.addActionListener(e -> eliminarInscripcion());
        btnCrear.addActionListener(e -> abrirFormularioCrear());
        btnActualizar.addActionListener(e -> abrirFormularioActualizar());

        panel.add(btnRefrescar);
        panel.add(btnEliminar);
        panel.add(btnCrear);
        panel.add(btnActualizar);

        return panel;
    }

    // Metodo que llama el controlador

    public void actualizarTabla(List<InscripcionDTO> inscripciones) {
        modeloTablaCursosIns.setRowCount(0); // Limpiar tabla

        for (InscripcionDTO ins : inscripciones) {
            modeloTablaCursosIns.addRow(new Object[] {
                    ins.getCursoId(),
                    ins.getCursoNombre(),
                    ins.getEstudianteId(),
                    ins.getEstudianteNombre(),
                    ins.getAnio(),
                    ins.getSemestre()
            });
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

            // Delegar al controlador
            controller.eliminarInscripcion(cursoId, estudianteId, anio, semestre);
        } catch (Exception ex) {
            mostrarError("Error al procesar eliminación: " + ex.getMessage());
        }
    }

    private void abrirFormularioCrear() {
        InscribirFrame inscribirFrame = new InscribirFrame();
        inscribirFrame.setVisible(true);

        // Refrescar cuando se cierre el formulario
        inscribirFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                controller.cargarInscripciones();
            }
        });
    }

    private void abrirFormularioActualizar() {
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

            InscribirFrame actualizarFrame = new InscribirFrame(true, estudianteId, cursoId, anio, semestre);
            actualizarFrame.setVisible(true);

            actualizarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    controller.cargarInscripciones();
                }
            });
        } catch (Exception ex) {
            mostrarError("Error al abrir actualización: " + ex.getMessage());
        }
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