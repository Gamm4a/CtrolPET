package Entity;

import Entity.Enum.Puesto;
import org.bson.types.ObjectId;

import java.util.List;

public class Empleado {
    ObjectId id_Empleado;
    String nombre;
    String ap_paterno;
    String ap_materno;
    String telefono;
    Puesto puesto;
    List<Horario> horarios;

    public Empleado(ObjectId id,String nombre, String ap_paterno, String ap_materno, String telefono, Puesto puesto, List<Horario> horarios) {
        this.id_Empleado=id;
        this.nombre = nombre;
        this.ap_paterno = ap_paterno;
        this.ap_materno = ap_materno;
        this.telefono = telefono;
        this.puesto = puesto;
        this.horarios = horarios;
    }

    public Empleado() {
    }

    public ObjectId getId_Empleado() {
        return id_Empleado;
    }

    public void setId_Empleado(ObjectId id_Empleado) {
        this.id_Empleado = id_Empleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public void setAp_paterno(String ap_paterno) {
        this.ap_paterno = ap_paterno;
    }

    public String getAp_materno() {
        return ap_materno;
    }

    public void setAp_materno(String ap_materno) {
        this.ap_materno = ap_materno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
