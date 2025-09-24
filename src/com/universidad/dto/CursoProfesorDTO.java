package com.universidad.dto;

public class CursoProfesorDTO {
    private Long profesorId;
    private String profesorNombre;
    private Long cursoId;
    private String cursoNombre;
    private Integer anio;
    private Integer semestre;

    // Constructor vacío
    public CursoProfesorDTO() {}

    // Constructor completo
    public CursoProfesorDTO(Long profesorId, String profesorNombre,
                            Long cursoId, String cursoNombre,
                            Integer anio, Integer semestre) {
        this.profesorId = profesorId;
        this.profesorNombre = profesorNombre;
        this.cursoId = cursoId;
        this.cursoNombre = cursoNombre;
        this.anio = anio;
        this.semestre = semestre;
    }

    // Getters y Setters
    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public String getProfesorNombre() { return profesorNombre; }
    public void setProfesorNombre(String profesorNombre) { this.profesorNombre = profesorNombre; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getCursoNombre() { return cursoNombre; }
    public void setCursoNombre(String cursoNombre) { this.cursoNombre = cursoNombre; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CursoProfesorDTO dto = new CursoProfesorDTO();

        public Builder profesorId(Long profesorId) {
            dto.profesorId = profesorId;
            return this;
        }

        public Builder profesorNombre(String profesorNombre) {
            dto.profesorNombre = profesorNombre;
            return this;
        }

        public Builder cursoId(Long cursoId) {
            dto.cursoId = cursoId;
            return this;
        }

        public Builder cursoNombre(String cursoNombre) {
            dto.cursoNombre = cursoNombre;
            return this;
        }

        public Builder anio(Integer anio) {
            dto.anio = anio;
            return this;
        }

        public Builder semestre(Integer semestre) {
            dto.semestre = semestre;
            return this;
        }

        public CursoProfesorDTO build() {
            return dto;
        }
    }
}
