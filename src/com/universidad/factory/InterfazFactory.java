package com.universidad.factory;

/**
 * Interfaz genérica para todas las fábricas del sistema
 * Define el contrato básico que deben cumplir las fábricas
 * @param <T> Tipo de enum que define los tipos disponibles
 * @param <R> Tipo de objeto que produce la fábrica
 */
public interface InterfazFactory<T extends Enum<T>, R> {
    
    <U extends R> U crear(T tipo, Class<U> claseEsperada);
   
    default boolean soporta(T tipo) {
        try {
            return tipo != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    T[] getTiposSoportados();
}
