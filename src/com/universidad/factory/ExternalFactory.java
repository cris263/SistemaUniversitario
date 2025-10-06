package com.universidad.factory;

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
        return ControllerFactory.crearControllerFactory();
    }
}
