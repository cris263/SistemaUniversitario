package com.universidad.util;

import com.universidad.ui.InterfazUsuario;

public class ThreadManager {
     public static Thread crearHilo(InterfazUsuario interfaz) {

        Thread hilo = new Thread(interfaz);
        hilo.setName("Hilo-" + interfaz.getClass().getSimpleName());
        hilo.start();
        
        return hilo;
    }
}
