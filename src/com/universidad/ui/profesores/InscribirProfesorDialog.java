package com.universidad.ui.profesores;

import com.universidad.controller.CursoController;
import com.universidad.controller.CursoProfesorController;
import com.universidad.controller.ProfesorController;
import com.universidad.dto.CursoDTO;
import com.universidad.dto.CursoProfesorDTO;
import com.universidad.dto.ProfesorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class InscribirProfesorDialog extends JDialog {
    private CursoProfesorController cursoProfesorController;
    private ProfesorController profesorController;
    private CursoController cursoController;

    private JTable profesoresTable;
    private JTable cursosTable;
    private JLabel lblSeleccion;
    private JTextField txtAnio;
    private JTextField txtSemestre;
    private JButton btnAccion;
    private JButton btnCancelar;

    // Variables para modo edición
    private boolean modoEdicion = false;
    private CursoProfesorDTO dtoOriginal;

    // Constructor para CREAR
    public InscribirProfesorDialog(JFrame parent, CursoProfesorController controller) {
        this(parent, controller, false, null);
    }

    // Constructor para EDITAR
    public InscribirProfesorDialog(JFrame parent, CursoProfesorController controller,
                                   boolean editar, CursoProfesorDTO dtoParaEditar) {
        super(parent, editar ? "Actualizar Asignación Profesor" : "Asignar Profesor a Curso", true);

        this.cursoProfesorController = controller;
        this.profesorController = new ProfesorController();
        this.cursoController = new CursoController();
        this.modoEdicion = editar;
        this.dtoOriginal = dtoParaEditar;

        setSize(900, 650);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        initComponents();
        setupLayout();
        cargarDatos();

        if (editar && dtoParaEditar != null) {
            configurarModoEdicion();
        }
    }

    private void initComponents() {
        lblSeleccion = new JLabel(modoEdicion ? "Editando asignación" : "Selecciona un profesor y un curso.");
        txtAnio = new JTextField(6);
        txtSemestre = new JTextField(4);
        btnAccion = new JButton(modoEdicion ? "Actualizar" : "Asignar");
        btnCancelar = new JButton("Cancelar");

        btnAccion.addActionListener(e -> {
            if (modoEdicion) {
                actualizar();
            } else {
                asignar();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profesores", crearPanelProfesores());
        tabbedPane.addTab("Cursos", crearPanelCursos());

        add(tabbedPane, BorderLayout.CENTER);
        add(crearPanelAsignacion(), BorderLayout.SOUTH);
    }

    private void configurarModoEdicion() {
        if (dtoOriginal != null) {
            txtAnio.setText(String.valueOf(dtoOriginal.getAnio()));
            txtSemestre.setText(String.valueOf(dtoOriginal.getSemestre()));
            lblSeleccion.setText("Editando asignación de profesor " + dtoOriginal.getProfesorId() +
                    " a curso " + dtoOriginal.getCursoId());
        }
    }

    private JPanel crearPanelProfesores() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Nombres", "Apellidos", "Email", "Tipo Contrato" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        profesoresTable = new JTable(model);
        profesoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(profesoresTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Nombre", "Activo" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cursosTable = new JTable(model);
        cursosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(cursosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatos() {
        cargarProfesores();
        cargarCursos();
    }

    private void cargarProfesores() {
        try {
            List<ProfesorDTO> profesores = profesorController.listarProfesores();
            DefaultTableModel model = (DefaultTableModel) profesoresTable.getModel();
            model.setRowCount(0);

            for (ProfesorDTO p : profesores) {
                model.addRow(new Object[] {
                        p.getId(),
                        p.getNombres(),
                        p.getApellidos(),
                        p.getEmail(),
                        p.getTipoContrato()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error cargando profesores: " + ex.getMessage());
        }
    }

    private void cargarCursos() {
        try {
            List<CursoDTO> cursos = cursoController.listarCursosActivos();
            DefaultTableModel model = (DefaultTableModel) cursosTable.getModel();
            model.setRowCount(0);

            for (CursoDTO c : cursos) {
                model.addRow(new Object[]{
                        c.getId(),
                        c.getNombre(),
                        c.getActivo() ? "Sí" : "No"
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error cargando cursos: " + ex.getMessage());
        }
    }

//    private void cargarCursos() {
//        try {
//            // Necesitarías crear un CursoController para esto
//            // Por ahora uso el método directo del DAO
//            com.universidad.persistencia.CursoDAO cursoDAO = new com.universidad.persistencia.CursoDAO();
//            List<com.universidad.modelo.Curso> cursos = cursoDAO.listarCursos();
//
//            DefaultTableModel model = (DefaultTableModel) cursosTable.getModel();
//            model.setRowCount(0);
//
//            for (com.universidad.modelo.Curso c : cursos) {
//                model.addRow(new Object[] {
//                        c.getId(),
//                        c.getNombre(),
//                        c.getActivo() ? "Sí" : "No"
//                });
//            }
//        } catch (SQLException ex) {
//            mostrarError("Error cargando cursos: " + ex.getMessage());
//        }
//    }

    private JPanel crearPanelAsignacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de Asignación"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta de selección
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lblSeleccion, gbc);

        // Campos de año y semestre
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        panel.add(txtAnio, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Semestre:"), gbc);
        gbc.gridx = 3;
        panel.add(txtSemestre, gbc);

        // Botones
        gbc.gridy = 2;
        gbc.gridx = 1;
        panel.add(btnAccion, gbc);

        gbc.gridx = 2;
        panel.add(btnCancelar, gbc);

        return panel;
    }

    private void asignar() {
        if (!validarFormulario()) return;

        int rowProf = profesoresTable.getSelectedRow();
        int rowCur = cursosTable.getSelectedRow();

        if (rowProf == -1 || rowCur == -1) {
            mostrarError("Debe seleccionar un profesor y un curso.");
            return;
        }

        try {
            Long profesorId = (Long) profesoresTable.getValueAt(rowProf, 0);
            Long cursoId = (Long) cursosTable.getValueAt(rowCur, 0);
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int semestre = Integer.parseInt(txtSemestre.getText().trim());

            // Crear DTO para la nueva asignación
            CursoProfesorDTO nuevoDTO = CursoProfesorDTO.builder()
                    .profesorId(profesorId)
                    .cursoId(cursoId)
                    .anio(anio)
                    .semestre(semestre)
                    .build();

            // Delegar al controlador
            cursoProfesorController.asignarProfesor(nuevoDTO);

            lblSeleccion.setText("Asignación creada: Profesor " + profesorId + " a Curso " + cursoId);
            limpiarFormulario();

        } catch (NumberFormatException ex) {
            mostrarError("Año y semestre deben ser números válidos.");
        }
    }

    private void actualizar() {
        if (!validarFormulario()) return;
        if (dtoOriginal == null) return;

        try {
            int nuevoAnio = Integer.parseInt(txtAnio.getText().trim());
            int nuevoSemestre = Integer.parseInt(txtSemestre.getText().trim());

            // Delegar al controlador
            cursoProfesorController.actualizarAsignacion(
                    dtoOriginal.getProfesorId(),
                    dtoOriginal.getCursoId(),
                    dtoOriginal.getAnio(),
                    dtoOriginal.getSemestre(),
                    nuevoAnio,
                    nuevoSemestre
            );

            dispose();
        } catch (NumberFormatException ex) {
            mostrarError("Año y semestre deben ser números válidos.");
        }
    }

    private boolean validarFormulario() {
        String anioText = txtAnio.getText().trim();
        String semestreText = txtSemestre.getText().trim();

        if (anioText.isEmpty() || semestreText.isEmpty()) {
            mostrarError("Debe ingresar año y semestre.");
            return false;
        }

        try {
            int anio = Integer.parseInt(anioText);
            int semestre = Integer.parseInt(semestreText);

            if (anio < 2020 || anio > 2030) {
                mostrarError("El año debe estar entre 2020 y 2030.");
                return false;
            }

            if (semestre < 1 || semestre > 2) {
                mostrarError("El semestre debe ser 1 o 2.");
                return false;
            }

            return true;
        } catch (NumberFormatException ex) {
            mostrarError("Año y semestre deben ser números válidos.");
            return false;
        }
    }

    private void limpiarFormulario() {
        txtAnio.setText("");
        txtSemestre.setText("");
        profesoresTable.clearSelection();
        cursosTable.clearSelection();
        lblSeleccion.setText("Selecciona un profesor y un curso.");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}