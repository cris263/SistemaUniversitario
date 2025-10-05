package com.universidad.config;

import com.universidad.controller.CursoController;
import com.universidad.servicio.CursoManager;
import com.universidad.ui.cursos.CursoConsolaObserver;

public class AppConfig {

    public static CursoController crearCursoController() {
        // Creamos el manager
        CursoManager cursoManager = new CursoManager();

        // Registrar observer
        cursoManager.attach(new CursoConsolaObserver());

        // Creamos el controlador
        return new CursoController(cursoManager);
    }
}

