package Entity;
import java.util.List;

import org.bson.types.ObjectId;

public class Sucursal {

    ObjectId id_sucursal;
    String nombre;
    String direccion;
    String telefono;
    String correo;
    String password;
    List<Empleado> empleados;

    public Sucursal() {
    }
    
    public Sucursal(ObjectId id_sucursal, String nombre, String direccion, String telefono, String correo,
            String password, List<Empleado> empleados) {
        this.id_sucursal = id_sucursal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.password = password;
        this.empleados = empleados;
    }

    public ObjectId getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(ObjectId id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    


}
