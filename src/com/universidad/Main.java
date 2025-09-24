package com.universidad;

import com.universidad.persistencia.DatabaseConnection;
import com.universidad.ui.Menu;

import javax.swing.*;


public class Main extends JFrame {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });

    }


}