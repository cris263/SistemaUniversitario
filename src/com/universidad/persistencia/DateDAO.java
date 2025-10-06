package com.universidad.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * DAO para manejar operaciones de fecha según el motor de base de datos
 * Usa DB.getConnection() que maneja la base de datos activa
 */
public class DateDAO {
    
    /**
     * Obtiene la fecha actual según el motor de base de datos en uso
     * @return String con la fecha formateada como YYYY-MM-DD
     */
    public String getCurrentDate() {
        String dbType = DB.getActiveDatabaseType();
        
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String sql;
            
            // Seleccionar la consulta SQL según el motor de base de datos
            if ("Oracle".equalsIgnoreCase(dbType)) {
                sql = "SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD') AS fecha FROM dual";
            } else if ("MySQL".equalsIgnoreCase(dbType)) {
                sql = "SELECT DATE_FORMAT(CURDATE(), '%Y-%m-%d') AS fecha";
            } else { // H2 u otros
                sql = "SELECT FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd') AS fecha";
            }
            
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("fecha");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error obteniendo fecha: " + e.getMessage());
        }
        
        // En caso de error, devolver fecha del sistema
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
