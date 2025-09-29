package com.universidad.persistencia;

public class TestDatabasesSimple {
    public static void main(String[] args) {
        System.out.println("PRUEBA SIMPLE DE BASES DE DATOS");
        System.out.println("==================================\n");
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
        
        
        
    }
    
    public static void testSpecificDatabase(String dbType) {
        System.out.println("🔍 Probando " + dbType.toUpperCase() + "...");
        
        try {
            DatabaseManager db = DatabaseFactory.createDatabase(dbType);
            boolean success = db.initialize();
            
            if (success) {
                System.out.println("✅ " + db.getName() + " funciona correctamente");
            } else {
                System.out.println("❌ " + db.getName() + " tuvo problemas");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error probando " + dbType + ": " + e.getMessage());
        }
    }
 
    public static void testOnlyH2() {
        System.out.println("🧪 PRUEBA RÁPIDA - SOLO H2");
        System.out.println("==========================\n");
        
        testSpecificDatabase("h2");
    }
}