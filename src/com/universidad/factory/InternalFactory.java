package com.universidad.factory;

public class InternalFactory {

    private static InternalFactory internalFactory;

    private InternalFactory() {
    }

    public static InternalFactory crearInternalFactory(){
        if (internalFactory == null) {
            internalFactory = new InternalFactory();
        }
        return internalFactory;
    }

    public ServiceFactory services() {
        return new ServiceFactory(this.DAOs());
    }

    public DAOFactory DAOs() {
        return  new DAOFactory();
    }

   
}
