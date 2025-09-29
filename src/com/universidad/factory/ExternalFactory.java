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
    
    // Pool de hilos para gestionar las interfaces
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    
    // Mapa para almacenar las instancias activas
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
    
    public static void crearAmbasInterfaces() {
        System.out.println("ExternalFactory: Creando interfaces de escritorio y consola simultáneamente");
        
        // Crear interfaz de escritorio en un hilo
        crearInterfaz(TipoInterfaz.ESCRITORIO);
        
        // Pequeña pausa para evitar conflictos de inicialización
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Crear interfaz de consola en otro hilo
        crearInterfaz(TipoInterfaz.CONSOLA);
        
        System.out.println("ExternalFactory: Ambas interfaces creadas y ejecutándose en hilos separados");
    }
    
    public static void mostrarEstado() {
        System.out.println("\nESTADO DE LAS INTERFACES:");
        System.out.println("=" + "=".repeat(40));
        
        for (TipoInterfaz tipo : TipoInterfaz.values()) {
            InterfazUsuario interfaz = interfacesActivas.get(tipo);
            Future<?> hilo = hilosActivos.get(tipo);
            
            if (interfaz != null) {
                String estado = (hilo != null && !hilo.isDone()) ? " ACTIVA" : " INACTIVA";
                System.out.println(String.format("%-12s: %s", tipo, estado));
            } else {
                System.out.println(String.format("%-12s:  NO CREADA", tipo));
            }
        }
        System.out.println("=" + "=".repeat(40));
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
