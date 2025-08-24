public class Facultad {
    // Atributos
    private int codigo;
    private String nombre;
    private Persona decano; // Relación con Persona

    // Constructor
    public Facultad(int codigo, String nombre, Persona decano) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.decano = decano;
    }

    // Getters y Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Persona getDecano() {
        return decano;
    }

    public void setDecano(Persona decano) {
        this.decano = decano;
    }

    // Método toString sobrescrito
    @Override
    public String toString() {
        return "Facultad {" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", decano=" + decano.getNombres() + " " + decano.getApellidos() +
                '}';
    }
}

