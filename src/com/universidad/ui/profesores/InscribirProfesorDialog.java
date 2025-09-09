package com.universidad.ui.profesores;

import com.universidad.modelo.Curso;
import com.universidad.modelo.Profesor;
import com.universidad.persistencia.CursoDAO;
import com.universidad.persistencia.ProfesorDAO;
import com.universidad.persistencia.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InscribirProfesorDialog extends JDialog {

    private JTable profesoresTable;
    private JTable cursosTable;
    private JLabel lblSeleccion;
    private JTextField txtAnio;
    private JTextField txtSemestre;
    private JButton btnAccion;
    private JButton btnCancelar;

    private boolean modoEdicion = false;
    private Long profesorIdOriginal;
    private Long cursoIdOriginal;
    private int anioOriginal;
    private int semestreOriginal;

    public InscribirProfesorDialog(JFrame parent) {
        this(parent, false, null, null, 0, 0);
    }

    // --- Constructor para EDITAR ---
    public InscribirProfesorDialog(JFrame parent, boolean editar, Long profesorId, Long cursoId, int anio,
            int semestre) {
        super(parent, editar ? "Actualizar Asignación Profesor" : "Asignar Profesor a Curso", true);

        this.modoEdicion = editar;
        this.profesorIdOriginal = profesorId;
        this.cursoIdOriginal = cursoId;
        this.anioOriginal = anio;
        this.semestreOriginal = semestre;

        setSize(900, 650);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        initComponents();
        setupLayout();

        // Si es edición, rellenamos los valores originales
        if (editar) {
            txtAnio.setText(String.valueOf(anio));
            txtSemestre.setText(String.valueOf(semestre));
            lblSeleccion.setText("Editando asignación de profesor " + profesorId + " a curso " + cursoId);
        }
    }

    private void initComponents() {
        lblSeleccion = new JLabel(modoEdicion ? "Editando asignación" : "Selecciona un profesor y un curso.");
        txtAnio = new JTextField(6);
        txtSemestre = new JTextField(4);

        btnAccion = new JButton(modoEdicion ? "✏️ Actualizar" : "➕ Asignar");
        btnAccion.addActionListener(e -> {
            if (modoEdicion) {
                actualizar();
            } else {
                asignar();
            }
        });

        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.addActionListener(e -> dispose());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel superior con tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("👨‍🏫 Profesores", crearPanelProfesores());
        tabbedPane.addTab("📚 Cursos", crearPanelCursos());

        add(tabbedPane, BorderLayout.CENTER);
        add(crearPanelAsignacion(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelProfesores() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Nombres", "Apellidos", "Email", "Tipo Contrato" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };

        profesoresTable = new JTable(model);
        profesoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(profesoresTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Cargar profesores
        try {
            ProfesorDAO dao = new ProfesorDAO();
            List<Profesor> profesores = dao.obtenerTodos(); // Asumiendo que existe este método
            for (Profesor p : profesores) {
                model.addRow(new Object[] {
                        p.getId(),
                        p.getNombres(),
                        p.getApellidos(),
                        p.getEmail(),
                        p.getTipoContrato()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando profesores: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Nombre", "Activo" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };

        cursosTable = new JTable(model);
        cursosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(cursosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Cargar cursos
        try {
            CursoDAO dao = new CursoDAO();
            List<Curso> cursos = dao.listarCursos();
            for (Curso c : cursos) {
                model.addRow(new Object[] {
                        c.getId(),
                        c.getNombre(),
                        c.getActivo() ? "Sí" : "No"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando cursos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JPanel crearPanelAsignacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de Asignación"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta de selección
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
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
        // Validar selecciones
        int rowProf = profesoresTable.getSelectedRow();
        int rowCur = cursosTable.getSelectedRow();

        if (rowProf == -1 || rowCur == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un profesor y un curso.",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar campos
        String anioText = txtAnio.getText().trim();
        String semestreText = txtSemestre.getText().trim();

        if (anioText.isEmpty() || semestreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar año y semestre.",
                    "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int anio = Integer.parseInt(anioText);
            int semestre = Integer.parseInt(semestreText);

            if (anio < 2020 || anio > 2030) {
                JOptionPane.showMessageDialog(this, "El año debe estar entre 2020 y 2030.",
                        "Año inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (semestre < 1 || semestre > 2) {
                JOptionPane.showMessageDialog(this, "El semestre debe ser 1 o 2.",
                        "Semestre inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Long profesorId = (Long) profesoresTable.getValueAt(rowProf, 0);
            Long cursoId = (Long) cursosTable.getValueAt(rowCur, 0);

            // Verificar si ya existe la asignación
            if (yaExisteAsignacion(profesorId, cursoId, anio, semestre)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una asignación de este profesor a este curso en el mismo período.",
                        "Asignación duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Insertar asignación
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, profesorId);
                stmt.setLong(2, cursoId);
                stmt.setInt(3, anio);
                stmt.setInt(4, semestre);
                stmt.executeUpdate();

                lblSeleccion.setText("✅ Asignación creada: Profesor " + profesorId + " a Curso " + cursoId);
                JOptionPane.showMessageDialog(this, "Asignación realizada con éxito.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos
                txtAnio.setText("");
                txtSemestre.setText("");
                profesoresTable.clearSelection();
                cursosTable.clearSelection();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al asignar: " + ex.getMessage(),
                        "Error de BD", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y semestre deben ser números válidos.",
                    "Formato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        String anioText = txtAnio.getText().trim();
        String semestreText = txtSemestre.getText().trim();

        if (anioText.isEmpty() || semestreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar año y semestre.",
                    "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int anio = Integer.parseInt(anioText);
            int semestre = Integer.parseInt(semestreText);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE curso_profesor SET anio = ?, semestre = ? " +
                        "WHERE profesor = ? AND curso = ? AND anio = ? AND semestre = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, anio);
                stmt.setInt(2, semestre);
                stmt.setLong(3, profesorIdOriginal);
                stmt.setLong(4, cursoIdOriginal);
                stmt.setInt(5, anioOriginal);
                stmt.setInt(6, semestreOriginal);

                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Asignación actualizada correctamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la asignación a actualizar.",
                            "No encontrado", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(),
                        "Error de BD", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y semestre deben ser números válidos.",
                    "Formato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean yaExisteAsignacion(Long profesorId, Long cursoId, int anio, int semestre) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM curso_profesor " +
                    "WHERE profesor = ? AND curso = ? AND anio = ? AND semestre = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, profesorId);
            stmt.setLong(2, cursoId);
            stmt.setInt(3, anio);
            stmt.setInt(4, semestre);

            var rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Método para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 100);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Modo CREAR
            new InscribirProfesorDialog(frame).setVisible(true);

            // Para probar modo ACTUALIZAR:
            // new InscribirProfesorDialog(frame, true, 1L, 2L, 2024, 1).setVisible(true);
        });
    }
}