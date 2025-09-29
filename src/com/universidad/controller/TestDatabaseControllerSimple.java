package com.universidad.controller;

import com.universidad.factory.ControllerFactory;

/**
 * Clase de prueba simple para DatabaseController
 */
public class TestDatabaseControllerSimple {
    
    public static void main(String[] args) {
        System.out.println("🧪 PRUEBA SIMPLE DE DATABASE CONTROLLER");
        System.out.println("=" + "=".repeat(40));
        
        // Crear controller usando Factory
        ControllerFactory factory = new ControllerFactory();
        DatabaseController dbController = factory.crear(
            ControllerFactory.TipoController.DATABASE, 
            DatabaseController.class
        );
        
        // Test 1: Ver base de datos inicial
        System.out.println("\n📋 Base de datos inicial: " + dbController.obtenerBaseDatosActual());
        
        // Test 2: Cambiar a H2
        System.out.println("\n🔄 Cambiando a H2...");
        boolean cambioH2 = dbController.cambiarBaseDatos("h2");
        System.out.println("Resultado: " + (cambioH2 ? "✅" : "❌"));
        System.out.println("Base actual: " + dbController.obtenerBaseDatosActual());
        
        // Test 3: Cambiar a MySQL
        System.out.println("\n🔄 Cambiando a MySQL...");
        boolean cambioMySQL = dbController.cambiarBaseDatos("mysql");
        System.out.println("Resultado: " + (cambioMySQL ? "✅" : "❌"));
        System.out.println("Base actual: " + dbController.obtenerBaseDatosActual());
        
        // Test 4: Cambiar a Oracle
        System.out.println("\n🔄 Cambiando a Oracle...");
        boolean cambioOracle = dbController.cambiarBaseDatos("oracle");
        System.out.println("Resultado: " + (cambioOracle ? "✅" : "❌"));
        System.out.println("Base actual: " + dbController.obtenerBaseDatosActual());
        
        // Test 5: Probar nombre inválido
        System.out.println("\n🔄 Probando nombre inválido...");
        boolean cambioInvalido = dbController.cambiarBaseDatos("inexistente");
        System.out.println("Resultado: " + (cambioInvalido ? "✅" : "❌"));
        System.out.println("Base actual: " + dbController.obtenerBaseDatosActual());
        
        System.out.println("\n🏁 Pruebas completadas");
        System.out.println("=" + "=".repeat(40));
    }
}