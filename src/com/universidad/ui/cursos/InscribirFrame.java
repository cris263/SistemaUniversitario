package com.universidad.ui.cursos;

import com.universidad.modelo.Curso;
import com.universidad.modelo.Estudiante;
import com.universidad.persistencia.CursoDAO;
import com.universidad.persistencia.EstudianteDAO;
import com.universidad.persistencia.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InscribirFrame extends JFrame {

    private JTable estudiantesTable;
    private JTable cursosTable;
    private JLabel lblSeleccion;
    private JTextField txtAnio;
    private JTextField txtSemestre;
    private JButton btnAccion;

    private boolean modoEdicion = false;
    private Long estudianteIdOriginal;
    private Long cursoIdOriginal;
    private int anioOriginal;
    private int semestreOriginal;

    public InscribirFrame() {
        this(false, null, null, 0, 0);
    }

    // --- Constructor para EDITAR ---
    public InscribirFrame(boolean editar, Long estudianteId, Long cursoId, int anio, int semestre) {
        this.modoEdicion = editar;
        this.estudianteIdOriginal = estudianteId;
        this.cursoIdOriginal = cursoId;
        this.anioOriginal = anio;
        this.semestreOriginal = semestre;

        setTitle(editar ? "Actualizar Inscripción" : "Gestión de Universidad - Inscripciones");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Estudiantes", crearPanelEstudiantes());
        tabbedPane.addTab("Cursos", crearPanelCursos());

        add(tabbedPane, BorderLayout.CENTER);
        add(crearPanelInscripcion(), BorderLayout.SOUTH);

        // si es edición, rellenamos los valores originales
        if (editar) {
            txtAnio.setText(String.valueOf(anio));
            txtSemestre.setText(String.valueOf(semestre));
            lblSeleccion.setText("Editando inscripción de estudiante " + estudianteId + " en curso " + cursoId);
        }
    }

    private JPanel crearPanelEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nombres", "Apellidos", "Email", "Código", "Activo", "Promedio"}, 0
        );

        estudiantesTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(estudiantesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            EstudianteDAO dao = new EstudianteDAO();
            List<Estudiante> estudiantes = dao.listarEstudiantes();
            for (Estudiante e : estudiantes) {
                model.addRow(new Object[]{
                        e.getId(),
                        e.getNombres(),
                        e.getApellidos(),
                        e.getEmail(),
                        e.getCodigo(),
                        e.getActivo(),
                        e.getPromedio()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando estudiantes: " + ex.getMessage());
        }

        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Activo"}, 0
        );

        cursosTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(cursosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            CursoDAO dao = new CursoDAO();
            List<Curso> cursos = dao.listarCursos();
            for (Curso c : cursos) {
                model.addRow(new Object[]{
                        c.getId(),
                        c.getNombre(),
                        c.getActivo()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando cursos: " + ex.getMessage());
        }

        return panel;
    }

    private JPanel crearPanelInscripcion() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        lblSeleccion = new JLabel(modoEdicion ? "Editando inscripción" : "Selecciona un estudiante y un curso.");
        txtAnio = new JTextField(6);
        txtSemestre = new JTextField(4);

        btnAccion = new JButton(modoEdicion ? "✏️ Actualizar" : "➕ Inscribir");
        btnAccion.addActionListener(e -> {
            if (modoEdicion) {
                actualizar();
            } else {
                inscribir();
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panel.add(lblSeleccion, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Año:"), gbc);

        gbc.gridx = 1;
        panel.add(txtAnio, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Semestre:"), gbc);

        gbc.gridx = 1;
        panel.add(txtSemestre, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.gridheight = 2;
        panel.add(btnAccion, gbc);

        return panel;
    }

    private void inscribir() {
        int rowEst = estudiantesTable.getSelectedRow();
        int rowCur = cursosTable.getSelectedRow();

        if (rowEst == -1 || rowCur == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante y un curso.");
            return;
        }

        Long estudianteId = (Long) estudiantesTable.getValueAt(rowEst, 0);
        Long cursoId = (Long) cursosTable.getValueAt(rowCur, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, cursoId);
            stmt.setLong(2, estudianteId);
            stmt.setInt(3, Integer.parseInt(txtAnio.getText().trim()));
            stmt.setInt(4, Integer.parseInt(txtSemestre.getText().trim()));
            stmt.executeUpdate();

            lblSeleccion.setText("Inscripción creada: Estudiante " + estudianteId + " en Curso " + cursoId);
            JOptionPane.showMessageDialog(this, "Inscripción realizada con éxito.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al inscribir: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE inscripcion SET anio = ?, semestre = ? " +
                         "WHERE curso = ? AND estudiante = ? AND anio = ? AND semestre = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtAnio.getText().trim()));
            stmt.setInt(2, Integer.parseInt(txtSemestre.getText().trim()));
            stmt.setLong(3, cursoIdOriginal);
            stmt.setLong(4, estudianteIdOriginal);
            stmt.setInt(5, anioOriginal);
            stmt.setInt(6, semestreOriginal);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Inscripción actualizada correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la inscripción a actualizar.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Modo CREAR
            new InscribirFrame().setVisible(true);

            // --- Para probar modo ACTUALIZAR ---
            // new InscribirFrame(true, 1L, 2L, 2024, 1).setVisible(true);
        });
    }
}
