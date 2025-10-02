package com.universidad.factory;

import com.universidad.ui.InterfazConsola;
import com.universidad.ui.InterfazEscritorio;
import com.universidad.ui.InterfazUsuario;

public class InterfaceFactory {

    private enum TipoInterfaz {
        CONSOLA, ESCRITORIO
    }

    public InterfaceFactory(){}

    public InterfazUsuario creaInterfaz(TipoInterfaz tipo){
        switch (tipo) {
            case CONSOLA:
                return new InterfazConsola();
            case ESCRITORIO:
                return new InterfazEscritorio();
            default:
                return new InterfazEscritorio();
        }
    }
    
    // Sobrecarga para aceptar String
    public InterfazUsuario creaInterfaz(String tipo){
        switch (tipo.toLowerCase()) {
            case "consola":
                return new InterfazConsola();
            case "escritorio":
                return new InterfazEscritorio();
            default:
                return new InterfazEscritorio();
        }
    }

    public TipoInterfaz[] obtenerTipos(){
        return TipoInterfaz.values();
    }
     
}
