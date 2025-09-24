package com.universidad.dto;

public class ProfesorDTO extends PersonaDTO {
    private String tipoContrato;
    int a = 0;

    public ProfesorDTO() {}

    public ProfesorDTO(Long id, String nombres, String apellidos, String email, String tipoContrato) {
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
