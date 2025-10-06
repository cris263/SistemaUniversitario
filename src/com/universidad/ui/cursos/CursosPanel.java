package com.universidad.ui.cursos;

import com.universidad.controller.CursoController;
import com.universidad.dto.CursoDTO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel principal de gestión de cursos
 * Responsabilidad: Coordinar componentes y manejar lógica de negocio UI
 * Sigue Clean Architecture separando responsabilidades
 */
public class CursosPanel extends JPanel {
    private final CursoController controller;
    private final CursoFormPanel formPanel;
    private final CursoTablePanel tablePanel;
    private final CursoButtonPanel buttonPanel;

    public CursosPanel(CursoController cursoController) {
        this.controller = cursoController;
        this.formPanel = new CursoFormPanel();
        this.tablePanel = new CursoTablePanel();
        this.buttonPanel = new CursoButtonPanel();
        
        setupUI();
        setupEventHandlers();
        cargarCursos();
    }

    /**
     * Configurar layout principal
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configurar manejadores de eventos
     */
    private void setupEventHandlers() {
        // Eventos de botones
        buttonPanel.onGuardar(this::guardarCurso);
        buttonPanel.onEliminar(this::eliminarCurso);
        buttonPanel.onLimpiar(this::limpiarFormulario);
        buttonPanel.onRefrescar(this::cargarCursos);
        
        // Evento de selección en tabla
        tablePanel.addSelectionListener(this::cargarDatosSeleccionados);
    }

    /**
     * Cargar todos los cursos
     */
    private void cargarCursos() {
        try {
            List<CursoDTO> cursos = controller.listarCursos();
            tablePanel.actualizarTabla(cursos);
        } catch (Exception ex) {
            mostrarError("Error al cargar cursos: " + ex.getMessage());
        }
    }

    /**
     * Guardar curso (crear nuevo)
     */
    private void guardarCurso() {
        if (!formPanel.validar()) {
            mostrarError("El campo 'Nombre' es obligatorio");
            return;
        }

        try {
            String nombre = formPanel.getNombre();
            CursoDTO nuevoCurso = controller.crearCurso(nombre);
            
            if (nuevoCurso != null) {
                mostrarExito("Curso guardado correctamente");
                limpiarFormulario();
                cargarCursos();
            }
        } catch (Exception ex) {
            mostrarError("Error al guardar curso: " + ex.getMessage());
        }
    }

    /**
     * Eliminar curso seleccionado
     */
    private void eliminarCurso() {
        if (!tablePanel.haySeleccion()) {
            mostrarAdvertencia("Seleccione un curso para eliminar");
            return;
        }

        Long id = tablePanel.getSelectedId();
        
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el curso?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.eliminarCurso(id);
                mostrarExito("Curso eliminado correctamente");
                limpiarFormulario();
                cargarCursos();
            } catch (Exception ex) {
                mostrarError("Error al eliminar curso: " + ex.getMessage());
            }
        }
    }

    /**
     * Cargar datos del curso seleccionado en el formulario
     */
    private void cargarDatosSeleccionados() {
        if (tablePanel.haySeleccion()) {
            formPanel.setId(tablePanel.getSelectedId().toString());
            formPanel.setNombre(tablePanel.getSelectedNombre());
            formPanel.setActivo(tablePanel.getSelectedActivo());
        }
    }

    /**
     * Limpiar formulario y selección
     */
    private void limpiarFormulario() {
        formPanel.limpiar();
        tablePanel.limpiarSeleccion();
    }

    // Métodos para mostrar mensajes
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
