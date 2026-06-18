package Entity;

public class Direccion {
    String calle;
    String colonia;
    int num_casa;
    String descripcion;

    public Direccion(String calle, String colonia, int num_casa, String descripcion) {
        this.calle = calle;
        this.colonia = colonia;
        this.num_casa = num_casa;
        this.descripcion = descripcion;
    }

    public Direccion() {
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public int getNum_casa() {
        return num_casa;
    }

    public void setNum_casa(int num_casa) {
        this.num_casa = num_casa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
