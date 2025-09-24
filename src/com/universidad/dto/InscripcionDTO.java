package com.universidad.dto;

public class InscripcionDTO {
    private Long estudianteId;
    private String estudianteNombre;
    private Long cursoId;
    private String cursoNombre;
    private Integer anio;
    private Integer semestre;


    public InscripcionDTO() {}

    // Constructor completo
    public InscripcionDTO(Long estudianteId, String estudianteNombre,
                          Long cursoId, String cursoNombre,
                          Integer anio, Integer semestre) {
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.cursoId = cursoId;
        this.cursoNombre = cursoNombre;
        this.anio = anio;
        this.semestre = semestre;
    }

    // Getters y Setters
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getCursoNombre() { return cursoNombre; }
    public void setCursoNombre(String cursoNombre) { this.cursoNombre = cursoNombre; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InscripcionDTO dto = new InscripcionDTO();

        public Builder estudianteId(Long estudianteId) {
            dto.estudianteId = estudianteId;
            return this;
        }

        public Builder estudianteNombre(String estudianteNombre) {
            dto.estudianteNombre = estudianteNombre;
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

        public InscripcionDTO build() {
            return dto;
        }
    }
}
