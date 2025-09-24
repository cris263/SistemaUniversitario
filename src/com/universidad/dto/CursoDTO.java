package com.universidad.dto;

import com.universidad.modelo.Programa;

public class CursoDTO {
    private Long id;
    private String nombre;
    private Programa programa;  // Incluir Programa según tu modelo
    private Boolean activo;

    // Constructor vacío
    public CursoDTO() {}

    // Constructor completo
    public CursoDTO(Long id, String nombre, Programa programa, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Programa getPrograma() { return programa; }
    public void setPrograma(Programa programa) { this.programa = programa; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CursoDTO dto = new CursoDTO();

        public Builder id(Long id) {
            dto.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            dto.nombre = nombre;
            return this;
        }

        public Builder programa(Programa programa) {
            dto.programa = programa;
            return this;
        }

        public Builder activo(Boolean activo) {
            dto.activo = activo;
            return this;
        }

        public CursoDTO build() {
            return dto;
        }
    }

    @Override
    public String toString() {
        return String.format("CursoDTO - ID: %d, Nombre: %s, Programa: %s, Activo: %s",
                id, nombre,
                programa != null ? programa.getNombre() : "N/A",
                activo);
    }
}