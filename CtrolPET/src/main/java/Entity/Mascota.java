package Entity;

import java.time.Instant;

public class Mascota {
    String nombre;
    String especie;
    String raza;
    Instant fch_nacimiento;
    String descripcion;

    public Mascota(String nombre, String especie, String raza, Instant fch_nacimiento, String descripcion) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fch_nacimiento = fch_nacimiento;
        this.descripcion = descripcion;
    }

    public Mascota( ) {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Instant getFch_nacimiento() {
        return fch_nacimiento;
    }

    public void setFch_nacimiento(Instant fch_nacimiento) {
        this.fch_nacimiento = fch_nacimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
