package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Mascota {
    String nombre;
    String especie;
    String raza;
    Instant fch_nacimiento;
    String descripcion;

}
