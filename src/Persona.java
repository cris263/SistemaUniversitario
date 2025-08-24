public class Persona {
    // Atributos
    private double id;
    private String nombres;
    private String apellidos;
    private String email;

    // Constructor
    public Persona(double id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    // Getters y Setters
    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método toString()
    @Override
    public String toString() {
        return "Persona [ID=" + id +
                ", Nombres=" + nombres +
                ", Apellidos=" + apellidos +
                ", Email=" + email + "]";
    }
}