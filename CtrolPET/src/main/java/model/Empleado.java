package model;

import model.Enum.Puesto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Empleado {
    @MongoId
    private ObjectId id_Empleado;
    private String nombre;
    private String ap_paterno;
    private String ap_materno;
    private String telefono;
    private Puesto puesto;
    private List<Horario> horarios;

}
