public class Profesor extends Persona {
    // Atributo propio
    private String tipoContrato;

    // Constructor
    public Profesor(double id, String nombres, String apellidos, String email,
                    String tipoContrato) {
        super(id, nombres, apellidos, email); // Constructor de Persona
        this.tipoContrato = tipoContrato;
    }

    // Getter y Setter
    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    // toString()
    @Override
    public String toString() {
        return "Profesor [" + super.toString() +
                ", TipoContrato=" + tipoContrato + "]";
    }
}

