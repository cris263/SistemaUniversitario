
package com.universidad.dto;

public class PersonaDTO {
    protected Long id;
    protected String nombres;
    protected String apellidos;
    protected String email;

    public PersonaDTO() {}

    public PersonaDTO(Long id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    // Getters y Setters
    public Long getId() { return id; }  // Cambiado de long a Long
    public void setId(Long id) { this.id = id; }  // Cambiado de long a Long

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PersonaDTO dto = new PersonaDTO();

        public Builder id(Long id) {
            dto.id = id;
            return this;
        }

        public Builder nombres(String nombres) {
            dto.nombres = nombres;
            return this;
        }

        public Builder apellidos(String apellidos) {
            dto.apellidos = apellidos;
            return this;
        }

        public Builder email(String email) {
            dto.email = email;
            return this;
        }

        public PersonaDTO build() {
            return dto;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nombres: %s, Apellidos: %s, Email: %s",
                id, nombres, apellidos, email);
    }
}
