package com.universidad.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * DAO para manejar operaciones de fecha según el motor de base de datos
 */
public class DateDAO {
    
    /**
     * Obtiene la fecha actual según el motor de base de datos en uso
     * @return String con la fecha formateada como YYYY-MM-DD
     */
    public String getCurrentDate() {
        DatabaseManager dbManager = DatabaseFactory.getActiveDatabaseManager();
        
        if (dbManager == null) {
            // Si no hay base de datos activa, usar la fecha del sistema
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String sql;
            String dbName = dbManager.getName().toUpperCase();
            
            // Seleccionar la consulta SQL según el motor de base de datos
            if (dbName.contains("ORACLE")) {
                sql = "SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD') AS fecha FROM dual";
            } else if (dbName.contains("MYSQL")) {
                sql = "SELECT DATE_FORMAT(CURDATE(), '%Y-%m-%d') AS fecha";
            } else { // H2 u otros
                sql = "SELECT FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd') AS fecha";
            }
            
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("fecha");
            }
            
        } catch (SQLException e) {
            System.err.println("Error obteniendo fecha: " + e.getMessage());
        }
        
        // En caso de error, devolver fecha del sistema
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
