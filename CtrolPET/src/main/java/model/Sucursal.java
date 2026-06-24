package model;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Sucursal {

    ObjectId id_sucursal;
    String nombre;
    String direccion;
    String telefono;
    String correo;
    String password;
    List<Empleado> empleados;

}
