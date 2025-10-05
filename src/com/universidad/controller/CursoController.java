package com.universidad.controller;

import com.universidad.dto.CursoDTO;
import com.universidad.servicio.CursoManager;
import com.universidad.mapper.CursoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CursoController {
    private final CursoManager cursoManager;

    public CursoController(CursoManager cursoManager) {
        this.cursoManager = cursoManager;
    }

    public CursoDTO crearCurso(String nombre) {
        return CursoMapper.toDTO(cursoManager.crearCurso(nombre));
    }

    public void eliminarCurso(Long id) {
        cursoManager.eliminarCurso(id);
    }

    public List<CursoDTO> listarCursos() {
        return cursoManager.getCursos()
                .stream()
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> listarCursosActivos() {
        return cursoManager.getCursosActivos()
                .stream()
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
