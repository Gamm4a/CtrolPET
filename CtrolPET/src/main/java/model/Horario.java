package model;

import model.Enum.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Horario {
    private DiaSemana dia;
    private LocalTime hora_entrada;
    private LocalTime hora_salida;

}
