package com.universidad.controller;

import com.universidad.dto.CursoDTO;
import com.universidad.mapper.CursoMapper;
import com.universidad.modelo.Curso;
import com.universidad.persistencia.CursoDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CursoController {
    private final CursoDAO cursoDAO;

    public CursoController() {
        this.cursoDAO = new CursoDAO();
    }

    // Usar solo el metodo existe en tu dao actual
    public List<CursoDTO> listarCursos() throws SQLException {
        return cursoDAO.listarCursos().stream()
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> listarCursosActivos() throws SQLException {
        return cursoDAO.listarCursos().stream()
                .filter(Curso::getActivo)
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Métodos adicionales que podrías implementar después en el DAO
    // public void crearCurso(CursoDTO cursoDTO) throws SQLException {
    //     Curso curso = CursoMapper.toEntity(cursoDTO);
    //     cursoDAO.guardarCurso(curso);
    // }

    // public void actualizarCurso(CursoDTO cursoDTO) throws SQLException {
    //     Curso curso = CursoMapper.toEntity(cursoDTO);
    //     cursoDAO.actualizarCurso(curso);
    // }

    // public void eliminarCurso(Long id) throws SQLException {
    //     cursoDAO.eliminarCurso(id);
    // }
}