import java.util.Date;

public class Programa {

    private double id;
    private String nombre;
    private double duracion;
    private Date registro;
    private Facultad facultad;

    public Programa(double id, String nombre, double duracion, Date registro, Facultad facultad){
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.registro = registro;
        this.facultad = facultad;
    }

    public double getId() {
        return id;
    }
    public void setId(double id) {
        this.id = id;
    }
    public String getNombre(String nombre) {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getDuracion() {
        return duracion;
    }
    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }
    public Date getRegistro() {
        return registro;
    }
    public void setRegistro(Date registro) {
        this.registro = registro;
    }
    public Facultad getFacultad(Facultad facultad) {
        return facultad;
    }


    public String toString(){
        return "Programa [ID=" +id + "Nombres =" + nombre +"Duracion =" + duracion + " registro =" + registro + " facultad =" + facultad + "]" ;
    }





}
