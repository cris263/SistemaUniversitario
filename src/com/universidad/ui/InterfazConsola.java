package com.universidad.ui;

import java.util.Scanner;

/**
 * Implementación de interfaz de consola que se ejecuta en un hilo separado
 */
public class InterfazConsola implements InterfazUsuario {
    private Scanner scanner;
    private volatile boolean activa = true;
    
    @Override
    public void inicializar() {
        System.out.println(" Inicializando interfaz de consola...");
        scanner = new Scanner(System.in);
    }
    
    @Override
    public void mostrarMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" SISTEMA UNIVERSITARIO - MODO CONSOLA");
        System.out.println("=".repeat(50));
        System.out.println("1. Gestionar Personas");
        System.out.println("2. Gestionar Estudiantes"); 
        System.out.println("3. Gestionar Profesores");
        System.out.println("4. Gestionar Cursos");
        System.out.println("5. Gestionar Inscripciones");
        System.out.println("0. Salir");
        System.out.println("=".repeat(50));
        System.out.print("Seleccione una opción: ");
    }
    
    @Override
    public void cerrar() {
        activa = false;
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("Interfaz de consola cerrada");
    }
    
    @Override
    public String getTipo() {
        return "CONSOLA";
    }
    
    @Override
    public void run() {
        System.out.println(" Hilo de interfaz de consola iniciado");
        inicializar();
        
        while (activa) {
            try {
                mostrarMenu();
                
                if (scanner.hasNextInt()) {
                    int opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    
                    switch (opcion) {
                        case 1:
                            System.out.println(" Gestionando Personas...");
                            Thread.sleep(1000);
                            break;
                        case 2:
                            System.out.println(" Gestionando Estudiantes...");
                            Thread.sleep(1000);
                            break;
                        case 3:
                            System.out.println(" Gestionando Profesores...");
                            Thread.sleep(1000);
                            break;
                        case 4:
                            System.out.println(" Gestionando Cursos...");
                            Thread.sleep(1000);
                            break;
                        case 5:
                            System.out.println(" Gestionando Inscripciones...");
                            Thread.sleep(1000);
                            break;
                        case 0:
                            System.out.println(" Saliendo del sistema...");
                            cerrar();
                            break;
                        default:
                            System.out.println(" Opción inválida. Intente nuevamente.");
                    }
                } else {
                    System.out.println(" Por favor ingrese un número válido.");
                    scanner.nextLine(); // Limpiar input inválido
                }
                
                if (activa) {
                    Thread.sleep(500); // Pequeña pausa entre interacciones
                }
                
            } catch (InterruptedException e) {
                System.out.println(" Hilo de consola interrumpido: " + e.getMessage());
                cerrar();
            } catch (Exception e) {
                System.out.println(" Error en interfaz de consola: " + e.getMessage());
            }
        }
        
        System.out.println(" Hilo de interfaz de consola terminado");
    }
}