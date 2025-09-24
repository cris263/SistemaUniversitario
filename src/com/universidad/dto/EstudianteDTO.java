package com.universidad.dto;

import com.universidad.modelo.Programa;

public class EstudianteDTO extends PersonaDTO {
    private Double codigo;
    private Programa programa;
    private Boolean activo;
    private Double promedio;

    public EstudianteDTO() {}

    public EstudianteDTO(Long id, String nombres, String apellidos, String email,
                         Double codigo, Programa programa, Boolean activo, Double promedio) {
        super(id, nombres, apellidos, email);
        this.codigo = codigo;
        this.programa = programa;
        this.activo = activo;
        this.promedio = promedio;
    }

    // Getters y Setters
    public Double getCodigo() { return codigo; }
    public void setCodigo(Double codigo) { this.codigo = codigo; }

    public Programa getPrograma() { return programa; }
    public void setPrograma(Programa programa) { this.programa = programa; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Double getPromedio() { return promedio; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }

    @Override
    public String toString() {
        return String.format("Estudiante - %s, Código: %.0f, Programa: %s, Activo: %s, Promedio: %.2f",
                super.toString(), codigo,
                programa != null ? programa.getNombre() : "N/A",
                activo, promedio);
    }
}
