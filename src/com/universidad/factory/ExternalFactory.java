package com.universidad.factory;

import com.universidad.ui.InterfazUsuario;
import com.universidad.ui.InterfazEscritorio;
import com.universidad.ui.InterfazConsola;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExternalFactory {

    private static ExternalFactory externalFactory;

    private ExternalFactory() {}

    public static ExternalFactory craeExternalFactory(){
        if(externalFactory == null){
            externalFactory = new ExternalFactory();
        }
        return externalFactory;
    }

    public InterfaceFactory crearInterfazFactory(){
        return new InterfaceFactory();
    }

    public ControllerFactory crearControllerFactory() {
        return new ControllerFactory();
    }
}
