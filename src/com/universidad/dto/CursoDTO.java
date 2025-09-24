package com.universidad.dto;

public class CursoDTO {
    private Long id;
    private String nombre;
    private Boolean activo;

    // Constructor vacío
    public CursoDTO() {}

    // Constructor completo
    public CursoDTO(Long id, String nombre, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

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

        public Builder activo(Boolean activo) {
            dto.activo = activo;
            return this;
        }

        public CursoDTO build() {
            return dto;
        }
    }
}