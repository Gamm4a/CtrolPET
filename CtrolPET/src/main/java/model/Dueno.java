package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Dueno {

    @MongoId
    private ObjectId id_dueño;
    private String nombre;
    private String ap_materno;
    private String ap_paterno;
    private Instant fch_nacimiento;
    private String telefono;
    private List<Mascota> mascotas;
    private Direccion direccion;

}
