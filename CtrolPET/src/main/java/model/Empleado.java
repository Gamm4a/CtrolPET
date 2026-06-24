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
    ObjectId id_Empleado;
    String nombre;
    String ap_paterno;
    String ap_materno;
    String telefono;
    Puesto puesto;
    List<Horario> horarios;

}
