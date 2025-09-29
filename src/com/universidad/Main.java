package com.universidad;

import com.universidad.factory.ExternalFactory;
import com.universidad.persistencia.DatabaseFactory;
import com.universidad.persistencia.DatabaseManager;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        try {
            DatabaseManager db = DatabaseFactory.createDatabase("oracle");
            boolean success = db.initialize();

            if (success) {
                System.out.println("✅ " + db.getName() + " funciona correctamente");
            } else {
                System.out.println("❌ " + db.getName() + " tuvo problemas");
            }

        } catch (Exception e) {
            System.err.println("❌ Error probando : " + e.getMessage());
        }

        // Agregar Shutdown Hook para Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Cerrando aplicación...");
            ExternalFactory.cerrarTodo();
            System.out.println("✅ Aplicación cerrada correctamente");
        }));

        // Verificar si hay argumentos de línea de comandos

        // Usar la fábrica para crear ambas interfaces
        System.out.println("Iniciando con ExternalFactory...");
        ExternalFactory.crearAmbasInterfaces();


    }
}