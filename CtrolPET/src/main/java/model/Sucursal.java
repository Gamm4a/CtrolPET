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

    private ObjectId id_sucursal;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String password;
    private List<Empleado> empleados;

}
