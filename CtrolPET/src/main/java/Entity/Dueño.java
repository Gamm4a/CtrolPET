package Entity;


import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class Dueño {
    ObjectId id_dueño;
    String nombre;
    String ap_materno;
    String ap_paterno;
    Instant fch_nacimiento;
    String telefono;
    List<Mascota> mascotas;
    Direccion direccion;

    public Dueño(ObjectId id,String nombre, String ap_materno, String ap_paterno, Instant fch_nacimiento, String telefono, List<Mascota> mascotas, Direccion direccion) {
        this.id_dueño=id;
        this.nombre = nombre;
        this.ap_materno = ap_materno;
        this.ap_paterno = ap_paterno;
        this.fch_nacimiento = fch_nacimiento;
        this.telefono = telefono;
        this.mascotas = mascotas;
        this.direccion = direccion;
    }

    public Dueño(){

    }

    public ObjectId getId_dueño() {
        return id_dueño;
    }

    public void setId_dueño(ObjectId id_dueño) {
        this.id_dueño = id_dueño;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAp_materno() {
        return ap_materno;
    }

    public void setAp_materno(String ap_materno) {
        this.ap_materno = ap_materno;
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public void setAp_paterno(String ap_paterno) {
        this.ap_paterno = ap_paterno;
    }

    public Instant getFch_nacimiento() {
        return fch_nacimiento;
    }

    public void setFch_nacimiento(Instant fch_nacimiento) {
        this.fch_nacimiento = fch_nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
