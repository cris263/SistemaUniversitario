package com.universidad.config;

import com.universidad.controller.CursoController;
import com.universidad.servicio.CursoManager;
import com.universidad.ui.cursos.CursoConsolaObserver;
import com.universidad.ui.cursos.CursoUIAdapter;

public class AppConfig {

    public static CursoController crearCursoController() {
        // Creamos el manager
        CursoManager cursoManager = new CursoManager();

        // Creamos el observer de UI
        CursoConsolaObserver consolaObserver = new CursoConsolaObserver();

        // Registramos usando el adaptador
        cursoManager.attach(new CursoUIAdapter(consolaObserver));

        // Creamos el controlador
        return new CursoController(cursoManager);
    }
}

