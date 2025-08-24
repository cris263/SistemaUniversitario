public class Estudiante extends Persona {
    // Atributos propios
    private double codigo;
    private String programa;
    private boolean activo;
    private double promedio;

    // Constructor
    public Estudiante(double id, String nombres, String apellidos, String email,
                      double codigo, String programa, boolean activo, double promedio) {
        super(id, nombres, apellidos, email); // Llama al constructor de Persona
        this.codigo = codigo;
        this.programa = programa;
        this.activo = activo;
        this.promedio = promedio;
    }

    // Getters y Setters
    public double getCodigo() { return codigo; }
    public void setCodigo(double codigo) { this.codigo = codigo; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public double getPromedio() { return promedio; }
    public void setPromedio(double promedio) { this.promedio = promedio; }

    // toString()
    @Override
    public String toString() {
        return "Estudiante [" + super.toString() +
                ", Código=" + codigo +
                ", Programa=" + programa +
                ", Activo=" + activo +
                ", Promedio=" + promedio + "]";
    }
}
