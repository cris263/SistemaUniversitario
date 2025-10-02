package com.universidad;

import com.universidad.factory.ExternalFactory;
import com.universidad.persistencia.DatabaseFactory;
import com.universidad.persistencia.DatabaseManager;
import com.universidad.util.ThreadManager;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        DatabaseManager db = DatabaseFactory.createDatabase("h2");
        db.initialize();

        ExternalFactory extFactory = ExternalFactory.craeExternalFactory();

        ThreadManager.crearHilo(extFactory.crearInterfazFactory().creaInterfaz("consola"));
        ThreadManager.crearHilo(extFactory.crearInterfazFactory().creaInterfaz("escritorio"));
    }
}