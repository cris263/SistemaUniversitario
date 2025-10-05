package com.universidad.ui.cursos;

import com.universidad.dto.CursoDTO;
import com.universidad.controller.CursoController;
import com.universidad.config.AppConfig;

import java.util.List;
import java.util.Scanner;

public class CrearCursoConsola {

    public static void main(String[] args) {
        // Obtenemos el controlador desde la configuración
        CursoController controller = AppConfig.crearCursoController();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Menú Curso ===");
            System.out.println("1. Crear curso");
            System.out.println("2. Eliminar curso");
            System.out.println("3. Listar cursos");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del curso: ");
                    String nombre = sc.nextLine();

                    CursoDTO nuevo = controller.crearCurso(nombre);
                    if (nuevo != null) {
                        System.out.println("✅ Curso creado: " + nuevo);
                    } else {
                        System.out.println("⚠ No se pudo crear el curso.");
                    }

                    break;

                case 2:
                    System.out.print("ID del curso a eliminar: ");
                    Long id = Long.parseLong(sc.nextLine());
                    controller.eliminarCurso(id);
                    System.out.println("❌ Curso eliminado (si existía)");
                    break;

                case 3:
                    System.out.println("📋 Cursos existentes:");
                    List<CursoDTO> cursos = controller.listarCursos();
                    if (cursos.isEmpty()) {
                        System.out.println("No hay cursos registrados.");
                    } else {
                        for (CursoDTO c : cursos) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 4:
                    System.out.println("Saliendo...");
                    sc.close();
                    return;

                default:
                    System.out.println("Opción inválida");
            }
        }
    }
}

