package com.universidad.modelo;

public class Profesor extends Persona {
    private String tipoContrato;
    
    public Profesor() {}
    
    public Profesor(long id, String nombres, String apellidos, String email, String tipoContrato) {
        super(id, nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }
    
    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }
    
    @Override
    public String toString() {
        return String.format("Profesor - %s, Tipo Contrato: %s", super.toString(), tipoContrato);
    }
}