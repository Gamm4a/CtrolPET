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
    private String nombre;
    private String especie;
    private String raza;
    private Instant fch_nacimiento;
    private String descripcion;

}
