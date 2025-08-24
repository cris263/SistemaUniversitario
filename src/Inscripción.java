public class Inscripción {
    private Curso curso;
    private int ano;
    private int semestre;
    private Estudiante estudiante;

    public Inscripción(Curso curso, int ano, int semestre,  Estudiante estudiante) {
        this.curso = curso;
        this.ano = ano;
        this.semestre = semestre;
        this.estudiante = estudiante;
    }
    public Curso getCurso() {
        return curso;
    }
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        this.ano = ano;
    }
    public int getSemestre() {
        return semestre;
    }
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
    public Estudiante getEstudiante() {
        return estudiante;
    }
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    @Override
    public String toString() {
        return "Curso ="+ curso + "anio" + ano + "semestre" + semestre + "estudiante" + estudiante;
    }
}
