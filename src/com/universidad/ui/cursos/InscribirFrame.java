package com.universidad.ui.cursos;

import com.universidad.controller.EstudianteController;
import com.universidad.controller.CursoController;
import com.universidad.dto.EstudianteDTO;
import com.universidad.dto.CursoDTO;
import com.universidad.persistencia.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InscribirFrame extends JFrame {
    private EstudianteController estudianteController;
    private CursoController cursoController;

    private JTable estudiantesTable;
    private JTable cursosTable;
    private JLabel lblSeleccion;
    private JTextField txtAnio;
    private JTextField txtSemestre;
    private JButton btnAccion;

    // Variables para modo edición
    private boolean modoEdicion = false;
    private Long estudianteIdOriginal;
    private Long cursoIdOriginal;
    private int anioOriginal;
    private int semestreOriginal;

    public InscribirFrame() {
        this(false, null, null, 0, 0);
    }

    // Constructor compatible con tu código existente
    public InscribirFrame(boolean editar, Long estudianteId, Long cursoId, int anio, int semestre) {
        this.modoEdicion = editar;
        this.estudianteIdOriginal = estudianteId;
        this.cursoIdOriginal = cursoId;
        this.anioOriginal = anio;
        this.semestreOriginal = semestre;

        // Inicializar controladores
        this.estudianteController = new EstudianteController();
        this.cursoController = new CursoController();

        setTitle(editar ? "Actualizar Inscripción" : "Gestión de Universidad - Inscripciones");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
        cargarDatos();

        if (editar) {
            configurarModoEdicion();
        }
    }

    private void initComponents() {
        lblSeleccion = new JLabel(modoEdicion ? "Editando inscripción" : "Selecciona un estudiante y un curso.");
        txtAnio = new JTextField(6);
        txtSemestre = new JTextField(4);
        btnAccion = new JButton(modoEdicion ? "Actualizar" : "Inscribir");

        btnAccion.addActionListener(e -> {
            if (modoEdicion) {
                actualizar();
            } else {
                inscribir();
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Estudiantes", crearPanelEstudiantes());
        tabbedPane.addTab("Cursos", crearPanelCursos());

        add(tabbedPane, BorderLayout.CENTER);
        add(crearPanelInscripcion(), BorderLayout.SOUTH);
    }

    private void configurarModoEdicion() {
        txtAnio.setText(String.valueOf(anioOriginal));
        txtSemestre.setText(String.valueOf(semestreOriginal));
        lblSeleccion.setText("Editando inscripción de estudiante " + estudianteIdOriginal +
                " en curso " + cursoIdOriginal);
    }

    private JPanel crearPanelEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nombres", "Apellidos", "Email", "Código", "Activo", "Promedio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        estudiantesTable = new JTable(model);
        estudiantesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(estudiantesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Activo"}, 0) {
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
        cargarEstudiantes();
        cargarCursos();
    }

    private void cargarEstudiantes() {
        try {
            List<EstudianteDTO> estudiantes = estudianteController.listarEstudiantes();
            DefaultTableModel model = (DefaultTableModel) estudiantesTable.getModel();
            model.setRowCount(0);

            for (EstudianteDTO e : estudiantes) {
                model.addRow(new Object[]{
                        e.getId(),
                        e.getNombres(),
                        e.getApellidos(),
                        e.getEmail(),
                        e.getCodigo(),
                        e.getActivo() ? "Sí" : "No",
                        e.getPromedio()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error cargando estudiantes: " + ex.getMessage());
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

    private JPanel crearPanelInscripcion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de Inscripción"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta de selección
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lblSeleccion, gbc);

        // Campos de año y semestre
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        panel.add(txtAnio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Semestre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSemestre, gbc);

        // Botón de acción
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        panel.add(btnAccion, gbc);

        return panel;
    }

    private void inscribir() {
        if (!validarFormulario()) return;

        int rowEst = estudiantesTable.getSelectedRow();
        int rowCur = cursosTable.getSelectedRow();

        if (rowEst == -1 || rowCur == -1) {
            mostrarError("Debe seleccionar un estudiante y un curso.");
            return;
        }

        try {
            Long estudianteId = (Long) estudiantesTable.getValueAt(rowEst, 0);
            Long cursoId = (Long) cursosTable.getValueAt(rowCur, 0);
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int semestre = Integer.parseInt(txtSemestre.getText().trim());

            // Mantener SQL directo como en tu código original
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cursoId);
                stmt.setLong(2, estudianteId);
                stmt.setInt(3, anio);
                stmt.setInt(4, semestre);
                stmt.executeUpdate();

                lblSeleccion.setText("Inscripción creada: Estudiante " + estudianteId + " en Curso " + cursoId);
                mostrarExito("Inscripción realizada con éxito.");
                limpiarFormulario();
            }

        } catch (NumberFormatException ex) {
            mostrarError("Año y semestre deben ser números válidos.");
        } catch (SQLException ex) {
            mostrarError("Error al inscribir: " + ex.getMessage());
        }
    }

    private void actualizar() {
        if (!validarFormulario()) return;

        try {
            int nuevoAnio = Integer.parseInt(txtAnio.getText().trim());
            int nuevoSemestre = Integer.parseInt(txtSemestre.getText().trim());

            // Mantener SQL directo como en tu código original
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE inscripcion SET anio = ?, semestre = ? " +
                        "WHERE curso = ? AND estudiante = ? AND anio = ? AND semestre = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, nuevoAnio);
                stmt.setInt(2, nuevoSemestre);
                stmt.setLong(3, cursoIdOriginal);
                stmt.setLong(4, estudianteIdOriginal);
                stmt.setInt(5, anioOriginal);
                stmt.setInt(6, semestreOriginal);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    mostrarExito("Inscripción actualizada correctamente.");
                    dispose();
                } else {
                    mostrarError("No se encontró la inscripción a actualizar.");
                }
            }

        } catch (NumberFormatException ex) {
            mostrarError("Año y semestre deben ser números válidos.");
        } catch (SQLException ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
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
        estudiantesTable.clearSelection();
        cursosTable.clearSelection();
        lblSeleccion.setText("Selecciona un estudiante y un curso.");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

}