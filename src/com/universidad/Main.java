package com.universidad;

import com.universidad.modelo.*;
import com.universidad.servicio.*;
import com.universidad.persistencia.DatabaseConnection;

import java.util.Scanner;
import java.util.Date;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();
    private static CursosInscritos cursosInscritos = new CursosInscritos();
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTION UNIVERSITARIA ===");
        System.out.println("Inicializando base de datos...");
        
        // Inicializar base de datos H2
        DatabaseConnection.initializeDatabase();
        
        // Cargar datos existentes de la BD
        cursosInscritos.cargarDatos();
        
        // Crear algunos datos de ejemplo
        crearDatosEjemplo();
        
        // Mostrar menú principal
        mostrarMenu();
    }
    
    private static void crearDatosEjemplo() {
        System.out.println("\n--- Creando datos de ejemplo ---");
        
        // Crear facultad y programa
        Profesor decano = new Profesor(1.0, "Dr. Carlos", "Rodriguez", "carlos@uni.edu.co", "Tiempo Completo");
        Facultad facultadIngenieria = new Facultad(1.0, "Facultad de Ingenieria", decano);
        Programa programaSistemas = new Programa(1.0, "Ingenieria de Sistemas", 10.0, new Date(), facultadIngenieria);
        
        // Crear estudiantes
        Estudiante estudiante1 = new Estudiante(1001.0, "Ana", "Garcia", "ana@email.com", 20241001.0, programaSistemas, true, 4.2);
        Estudiante estudiante2 = new Estudiante(1002.0, "Luis", "Martinez", "luis@email.com", 20241002.0, programaSistemas, true, 3.8);
        
        // Crear profesores
        Profesor profesor1 = new Profesor(2001.0, "Dra. Maria", "Lopez", "maria@uni.edu.co", "Catedra");
        Profesor profesor2 = new Profesor(2002.0, "Ing. Pedro", "Sanchez", "pedro@uni.edu.co", "Tiempo Completo");
        
        // Crear cursos
        Curso curso1 = new Curso(3001, "Programacion I", programaSistemas, true);
        Curso curso2 = new Curso(3002, "Base de Datos", programaSistemas, true);
        Curso curso3 = new Curso(3003, "Algoritmos", programaSistemas, true);
        
        // Agregar personas al sistema
        inscripcionesPersonas.inscribir(estudiante1);
        inscripcionesPersonas.inscribir(estudiante2);
        inscripcionesPersonas.inscribir(profesor1);
        inscripcionesPersonas.inscribir(profesor2);
        
        // Crear inscripciones (esto se guardará en H2)
        Inscripcion inscripcion1 = new Inscripcion(curso1, 2024, 2, estudiante1);
        Inscripcion inscripcion2 = new Inscripcion(curso2, 2024, 2, estudiante1);
        Inscripcion inscripcion3 = new Inscripcion(curso1, 2024, 2, estudiante2);
        
        cursosInscritos.inscribirCurso(inscripcion1);
        cursosInscritos.inscribirCurso(inscripcion2);
        cursosInscritos.inscribirCurso(inscripcion3);
        
        System.out.println("Datos de ejemplo creados exitosamente!\n");
    }
    
    private static void mostrarMenu() {
        int opcion;
        
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestionar Personas");
            System.out.println("2. Gestionar Inscripciones de Cursos");
            System.out.println("3. Crear Nueva Inscripcion");
            System.out.println("4. Mostrar Reportes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    menuPersonas();
                    break;
                case 2:
                    menuInscripciones();
                    break;
                case 3:
                    crearNuevaInscripcion();
                    break;
                case 4:
                    mostrarReportes();
                    break;
                case 0:
                    System.out.println("¡Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente de nuevo.");
            }
        } while (opcion != 0);
        
        scanner.close();
    }
    
    private static void menuPersonas() {
        int opcion;
        
        do {
            System.out.println("\n=== GESTION DE PERSONAS ===");
            System.out.println("1. Listar todas las personas");
            System.out.println("2. Ver persona por posicion");
            System.out.println("3. Contar personas registradas");
            System.out.println("4. Registrar nuevo estudiante");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    listarPersonas();
                    break;
                case 2:
                    verPersonaPorPosicion();
                    break;
                case 3:
                    System.out.println("Total de personas: " + inscripcionesPersonas.cantidadActual());
                    break;
                case 4:
                    registrarNuevoEstudiante();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }
    
    private static void menuInscripciones() {
        int opcion;
        
        do {
            System.out.println("\n=== GESTION DE INSCRIPCIONES ===");
            System.out.println("1. Listar todas las inscripciones");
            System.out.println("2. Ver inscripcion por posicion");
            System.out.println("3. Contar inscripciones");
            System.out.println("4. Recargar desde base de datos");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    listarInscripciones();
                    break;
                case 2:
                    verInscripcionPorPosicion();
                    break;
                case 3:
                    System.out.println("Total de inscripciones: " + cursosInscritos.cantidadActual());
                    break;
                case 4:
                    cursosInscritos.cargarDatos();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }
    
    private static void listarPersonas() {
        System.out.println("\n--- LISTADO DE PERSONAS ---");
        var listado = inscripcionesPersonas.imprimirListado();
        if (listado.isEmpty()) {
            System.out.println("No hay personas registradas.");
        } else {
            for (int i = 0; i < listado.size(); i++) {
                System.out.println(i + ". " + listado.get(i));
            }
        }
    }
    
    private static void listarInscripciones() {
        System.out.println("\n--- LISTADO DE INSCRIPCIONES ---");
        var listado = cursosInscritos.imprimirListado();
        if (listado.isEmpty()) {
            System.out.println("No hay inscripciones registradas.");
        } else {
            for (int i = 0; i < listado.size(); i++) {
                System.out.println(i + ". " + listado.get(i));
            }
        }
    }
    
    private static void verPersonaPorPosicion() {
        System.out.print("Ingrese la posicion (0-" + (inscripcionesPersonas.cantidadActual() - 1) + "): ");
        String posicion = scanner.nextLine();
        String resultado = inscripcionesPersonas.imprimirPosicion(posicion);
        System.out.println("Resultado: " + resultado);
    }
    
    private static void verInscripcionPorPosicion() {
        System.out.print("Ingrese la posicion (0-" + (cursosInscritos.cantidadActual() - 1) + "): ");
        String posicion = scanner.nextLine();
        String resultado = cursosInscritos.imprimirPosicion(posicion);
        System.out.println("Resultado: " + resultado);
    }
    
    private static void registrarNuevoEstudiante() {
        System.out.println("\n--- REGISTRAR NUEVO ESTUDIANTE ---");
        System.out.print("ID: ");
        Double id = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Nombres: ");
        String nombres = scanner.nextLine();
        
        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Código de estudiante: ");
        Double codigo = scanner.nextDouble();
        
        System.out.print("Promedio: ");
        Double promedio = scanner.nextDouble();
        
        // Crear programa de ejemplo
        Facultad facultad = new Facultad(1.0, "Facultad de Ingenieria", null);
        Programa programa = new Programa(1.0, "Ingenieria de Sistemas", 10.0, new Date(), facultad);
        
        Estudiante nuevoEstudiante = new Estudiante(id, nombres, apellidos, email, codigo, programa, true, promedio);
        inscripcionesPersonas.inscribir(nuevoEstudiante);
    }
    
    private static void crearNuevaInscripcion() {
        System.out.println("\n--- CREAR NUEVA INSCRIPCIÓN ---");
        
        // Mostrar estudiantes disponibles
        System.out.println("Estudiantes disponibles:");
        var personas = inscripcionesPersonas.getListado();
        var estudiantes = personas.stream()
            .filter(p -> p instanceof Estudiante)
            .map(p -> (Estudiante) p)
            .toList();
            
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        
        for (int i = 0; i < estudiantes.size(); i++) {
            System.out.println(i + ". " + estudiantes.get(i));
        }
        
        System.out.print("Seleccione estudiante (posicion): ");
        int posEstudiante = scanner.nextInt();
        
        if (posEstudiante < 0 || posEstudiante >= estudiantes.size()) {
            System.out.println("Posicion invalida.");
            return;
        }
        
        Estudiante estudiante = estudiantes.get(posEstudiante);
        
        // Crear curso
        System.out.print("ID del curso: ");
        int cursoId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Nombre del curso: ");
        String nombreCurso = scanner.nextLine();
        
        System.out.print("Anio: ");
        int anio = scanner.nextInt();
        
        System.out.print("Semestre: ");
        int semestre = scanner.nextInt();
        
        Curso curso = new Curso(cursoId, nombreCurso, estudiante.getPrograma(), true);
        Inscripcion nuevaInscripcion = new Inscripcion(curso, anio, semestre, estudiante);
        
        cursosInscritos.inscribirCurso(nuevaInscripcion);
    }
    
    private static void mostrarReportes() {
        System.out.println("\n=== REPORTES DEL SISTEMA ===");
        System.out.println("Total de personas registradas: " + inscripcionesPersonas.cantidadActual());
        System.out.println("Total de inscripciones: " + cursosInscritos.cantidadActual());
        
        // Contar estudiantes y profesores
        var personas = inscripcionesPersonas.getListado();
        long estudiantes = personas.stream().filter(p -> p instanceof Estudiante).count();
        long profesores = personas.stream().filter(p -> p instanceof Profesor).count();
        
        System.out.println("Estudiantes: " + estudiantes);
        System.out.println("Profesores: " + profesores);
        
        System.out.println("\n--- Últimas 3 inscripciones ---");
        var inscripciones = cursosInscritos.imprimirListado();
        int start = Math.max(0, inscripciones.size() - 3);
        for (int i = start; i < inscripciones.size(); i++) {
            System.out.println("• " + inscripciones.get(i));
        }
    }
}
