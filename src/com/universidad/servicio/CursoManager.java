package com.universidad.servicio;

import com.universidad.modelo.Curso;
import com.universidad.modelo.ObservadorCurso;
import com.universidad.persistencia.CursoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CursoManager implements ObservableCurso{

    private List<Curso> cursos;
    private List<ObservadorCurso> observadores;
    private CursoDAO dao;

    // 🔹 Constructor sin ProgramaManager
    public CursoManager() {
        this.cursos = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.dao = new CursoDAO();

        // Intentar cargar cursos desde BD, solo si hay DB disponible
        try {
            List<Curso> cursosBD = dao.listarCursos();
            if (cursosBD != null) {
                this.cursos.addAll(cursosBD);
            }
        } catch (SQLException e) {
            System.err.println("⚠ No se pudieron cargar cursos desde la base de datos: " + e.getMessage());
        }
    }

    // --- Observer ---
    public void attach(ObservadorCurso obs) {
        if (!observadores.contains(obs)) observadores.add(obs);
    }

    public void detach(ObservadorCurso obs) {
        observadores.remove(obs);
    }


    public void notificar(String mensaje) {
        for (ObservadorCurso obs : observadores) {
            obs.actualizar(mensaje);
        }
    }

    // --- CRUD simplificado ---
    public Curso crearCurso(String nombre) {
        Curso curso = new Curso(null, nombre, null, true); // Programa = null por ahora
        try {
            curso = dao.crearCurso(curso); // Persistir en BD
            cursos.add(curso); // Agregar a lista interna
            notificar("✅ Curso creado: " + curso.getNombre());
        } catch (SQLException e) {
            System.err.println("❌ No se pudo crear el curso: " + nombre);
            e.printStackTrace();
        }
        return curso;
    }

    public List<Curso> getCursos() {
        return new ArrayList<>(cursos);
    }

    public Curso buscarPorId(Long id) {
        for (Curso c : cursos) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public boolean eliminarCurso(Long id) {
        try {
            Curso curso = cursos.stream()
                    .filter(c -> c.getId() != null && c.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (curso == null) {
                System.err.println("⚠ Curso no encontrado con ID: " + id);
                return false;
            }

            dao.eliminarCurso(id); // Eliminar de la BD
            cursos.remove(curso);  // Eliminar de la lista local
            notificar("❌ Curso eliminado: " + curso.getNombre());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ No se pudo eliminar el curso con ID: " + id);
            return false;
        }
    }

    public List<Curso> getCursosActivos() {
        return cursos.stream()
                .filter(Curso::getActivo)
                .collect(Collectors.toList());
    }
}
