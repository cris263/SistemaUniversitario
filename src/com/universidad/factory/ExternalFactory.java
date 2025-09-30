package com.universidad.factory;

import com.universidad.ui.InterfazUsuario;
import com.universidad.ui.InterfazEscritorio;
import com.universidad.ui.InterfazConsola;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExternalFactory {
    
    // Enum para tipos de interfaz
    public enum TipoInterfaz {
        ESCRITORIO, CONSOLA
    }
    
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    
    private static final Map<TipoInterfaz, InterfazUsuario> interfacesActivas = new HashMap<>();
    
    private static final Map<TipoInterfaz, Future<?>> hilosActivos = new HashMap<>();
    
    
    public static InterfazUsuario crearInterfaz(TipoInterfaz tipo) {
        
        InterfazUsuario interfaz;
        
        switch (tipo) {
            case ESCRITORIO:
                interfaz = new InterfazEscritorio();
                break;
            case CONSOLA:
                interfaz = new InterfazConsola();
                break;
            default:
                throw new IllegalArgumentException("Tipo de interfaz no soportado: " + tipo);
        }
        
        // Almacenar la interfaz
        interfacesActivas.put(tipo, interfaz);
        
        // Ejecutar en un hilo separado
        Future<?> futuro = executorService.submit(interfaz);
        hilosActivos.put(tipo, futuro);
        
        System.out.println("ExternalFactory: Interfaz " + tipo + " creada y ejecutándose en hilo separado");
        
        return interfaz;
    }
    
    public static InterfazUsuario obtenerInterfaz(TipoInterfaz tipo) {
        return interfacesActivas.get(tipo);
    }
    
    public static void cerrarInterfaz(TipoInterfaz tipo) {
        InterfazUsuario interfaz = interfacesActivas.get(tipo);
        if (interfaz != null) {
            System.out.println("ExternalFactory: Cerrando interfaz " + tipo);
            interfaz.cerrar();
            
            Future<?> hilo = hilosActivos.get(tipo);
            if (hilo != null && !hilo.isDone()) {
                hilo.cancel(true);
            }
            
            interfacesActivas.remove(tipo);
            hilosActivos.remove(tipo);
        }
    }
    
    public static void cerrarTodo() {
        System.out.println(" ExternalFactory: Cerrando todas las interfaces...");
        
        for (TipoInterfaz tipo : TipoInterfaz.values()) {
            cerrarInterfaz(tipo);
        }
        
        executorService.shutdown();
        System.out.println(" ExternalFactory: Todas las interfaces cerradas y pool de hilos terminado");
    }
}
